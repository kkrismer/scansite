package edu.mit.scansite.server.updater;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.HistogramDao;
import edu.mit.scansite.server.dataaccess.TaxonDao;
import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DropTableCommand;
import edu.mit.scansite.server.dataaccess.commands.RenameTableCommand;
import edu.mit.scansite.server.dataaccess.commands.annotation.CreateAnnotationsTableCommand;
import edu.mit.scansite.server.dataaccess.commands.protein.CreateProteinsTableCommand;
import edu.mit.scansite.server.dataaccess.commands.taxon.CreateTaxaTableCommand;
import edu.mit.scansite.server.dataaccess.file.DirectoryManagement;
import edu.mit.scansite.server.dataaccess.file.ImageInOut;
import edu.mit.scansite.server.images.histograms.HistogramMaker;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.server.updater.transliterator.FileTransliterator;
import edu.mit.scansite.server.updater.transliterator.ProteinTransliteratorDbWriter;
import edu.mit.scansite.server.updater.transliterator.ScansiteProteinFileTransliterator;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class ProteinDbUpdater extends DbUpdater {

	@Override
	protected FileTransliterator getScansiteFileTransliterator(
			String errorFilePath) throws ScansiteUpdaterException {
		BufferedWriter errorWriter;
		try {
			errorWriter = new BufferedWriter(new FileWriter(new File(errorFilePath)));
			ProteinTransliteratorDbWriter writer =
					new ProteinTransliteratorDbWriter(errorWriter, dataSourceMetaInfo);
			ScansiteProteinFileTransliterator transliterator =
					new ScansiteProteinFileTransliterator(getReaders(), writer);
			return transliterator;
		} catch (IOException e) {
			throw new ScansiteUpdaterException(
					"Error accessing temporary files", e);
		}
	}

	@Override
	protected void createTables() throws ScansiteUpdaterException {
		createTaxonTable();
		createProteinTable();
		createAnnotationTable();
	}

	@Override
	protected void renameTables() throws ScansiteUpdaterException {
		try {
			CommandConstants cmdConst = CommandConstants
					.instance(ServiceLocator.getDbConstantsProperties());
			RenameTableCommand cmd = new RenameTableCommand(
					ServiceLocator.getDbAccessProperties(),
					ServiceLocator.getDbConstantsProperties(), null,
					null);
			renameTaxonTables(cmd, cmdConst);
			renameAnnotationTables(cmd, cmdConst);
			renameProteinTables(cmd, cmdConst);
		} catch (DataAccessException e) {
			logger.error("RENAMING TABLES FAILED... ["
					+ dataSourceMetaInfo.getDataSource().getShortName() + "]: "
					+ e.getMessage());
		}
	}

	@Override
	protected void dropOldTables() throws ScansiteUpdaterException {
		try {
			DropTableCommand cmd = new DropTableCommand(
					ServiceLocator.getDbAccessProperties(),
					ServiceLocator.getDbConstantsProperties(), null);
			CommandConstants cmdConst = CommandConstants
					.instance(ServiceLocator.getDbConstantsProperties());
			cmd.setTableName(CommandConstants.getOldTable(cmdConst
					.getAnnotationsTableName(dataSourceMetaInfo.getDataSource())));
			try {
				cmd.execute();
			} catch (Exception e) {
				logger.info("dropping old annotation table failed, there is no old annotation table of "
						+ dataSourceMetaInfo.getDataSource().getShortName());
			}
			cmd.setTableName(CommandConstants.getOldTable(cmdConst
					.getTaxaTableName(dataSourceMetaInfo.getDataSource())));
			try {
				cmd.execute();
			} catch (Exception e) {
				logger.info("dropping old taxa table failed, there is no old taxa table of "
						+ dataSourceMetaInfo.getDataSource().getShortName());
			}
			cmd.setTableName(CommandConstants.getOldTable(cmdConst
					.getProteinsTableName(dataSourceMetaInfo.getDataSource())));
			try {
				cmd.execute();
			} catch (Exception e) {
				logger.info("dropping old protein table failed, there is no old protein table of "
						+ dataSourceMetaInfo.getDataSource().getShortName());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	protected void dropTempTables() throws ScansiteUpdaterException {
		try {
			DropTableCommand cmd = new DropTableCommand(
					ServiceLocator.getDbAccessProperties(),
					ServiceLocator.getDbConstantsProperties(), null);
			CommandConstants cmdConst = CommandConstants
					.instance(ServiceLocator.getDbConstantsProperties());
			cmd.setTableName(CommandConstants.getTempTable(cmdConst
					.getAnnotationsTableName(dataSourceMetaInfo.getDataSource())));
			try {
				cmd.execute();
			} catch (Exception e) {
				logger.error("dropping temp annotation table of data source "
						+ dataSourceMetaInfo.getDataSource().getShortName()
						+ " failed: " + e.getMessage());
			}
			cmd.setTableName(CommandConstants.getTempTable(cmdConst
					.getTaxaTableName(dataSourceMetaInfo.getDataSource())));
			try {
				cmd.execute();
			} catch (Exception e) {
				logger.error("dropping temp taxa table of data source "
						+ dataSourceMetaInfo.getDataSource().getShortName()
						+ " failed: " + e.getMessage());
			}
			cmd.setTableName(CommandConstants.getTempTable(cmdConst
					.getProteinsTableName(dataSourceMetaInfo.getDataSource())));
			try {
				cmd.execute();
			} catch (Exception e) {
				logger.error("dropping temp protein table of data source "
						+ dataSourceMetaInfo.getDataSource().getShortName()
						+ " failed: " + e.getMessage());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	protected void updateHistogramData() throws ScansiteUpdaterException {
		try {
			HistogramDao histDao = fac.getHistogramDao();
			TaxonDao taxonDao = fac.getTaxonDao();
			DataSource dataSource = fac.getDataSourceDao().get(
					dataSourceMetaInfo.getDataSource().getShortName());

			List<ServerHistogram> hists = histDao.getHistograms(null,
					dataSource, -1);
			DirectoryManagement.prepareHistogramDirectory();
			for (ServerHistogram hist : hists) {
				Motif m = hist.getMotif();
				if (dataSource.getShortName().equalsIgnoreCase(
						hist.getDataSource().getShortName())) {
					Taxon tOld = null;
					try {
						taxonDao.setUseTempTablesForUpdate(false);
						tOld = taxonDao.getById(hist.getTaxon().getId(),
								hist.getDataSource());
					} catch (Exception e) {
						throw new ScansiteUpdaterException(e);
					}
					taxonDao.setUseTempTablesForUpdate(true);
					Taxon tNew = taxonDao.getByName(tOld.getName(),
							hist.getDataSource());
					hist.setTaxon(tNew);

					// create new histogram
					long systimeMs = System.currentTimeMillis();
					String path = FilePaths.getHistogramFilePath("", null,
							systimeMs);
					HistogramMaker hMaker = new HistogramMaker();
					ServerHistogram serverHistogram;
					try {
						serverHistogram = hMaker.makeServerHistogram(m,
								dataSource, tNew, true);
					} catch (DataAccessException e) {
						logger.error(e.getMessage(), e);
						throw new ScansiteUpdaterException(e.getMessage(), e);
					}
					try {
						ImageInOut imageIO = new ImageInOut();
						imageIO.saveImage(serverHistogram.getDbHistogramPlot(),
								path);
						serverHistogram.getDbEditHistogramPlot();
						serverHistogram.setPlot(imageIO.getImage(path));
					} catch (DataAccessException e) {
						logger.error(e.getMessage(), e);
						throw new ScansiteUpdaterException(e.getMessage(), e);
					}
					serverHistogram.setMotif(m);
					serverHistogram.setDataSource(dataSource);
					serverHistogram.setTaxon(tNew);
					serverHistogram.setImageFilePath(path);

					// delete old histogram
					try {
						if (tOld != null) {
							histDao.deleteHistograms(m, dataSource, tOld);
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}

					// save new
					histDao.add(serverHistogram);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ScansiteUpdaterException(e.getMessage(), e);
		}
	}

	private void createTaxonTable() throws ScansiteUpdaterException {
		try {
			CreateTaxaTableCommand cmd = new CreateTaxaTableCommand(
					ServiceLocator.getDbAccessProperties(),
					ServiceLocator.getDbConstantsProperties(),
					dataSourceMetaInfo.getDataSource());
			cmd.execute();
		} catch (Exception e) {
			logger.error("Cannot create taxa table: " + e.getMessage(), e);
			throw new ScansiteUpdaterException("Cannot create taxa table", e);
		}
	}

	private void createAnnotationTable() throws ScansiteUpdaterException {
		try {
			CreateAnnotationsTableCommand cmd = new CreateAnnotationsTableCommand(
					ServiceLocator.getDbAccessProperties(),
					ServiceLocator.getDbConstantsProperties(),
					dataSourceMetaInfo.getDataSource());
			cmd.execute();
		} catch (Exception e) {
			logger.error("Cannot create annotations table: " + e.getMessage(),
					e);
			throw new ScansiteUpdaterException(
					"Cannot create annotations table", e);
		}
	}

	private void createProteinTable() throws ScansiteUpdaterException {
		try {
			CreateProteinsTableCommand cmd = new CreateProteinsTableCommand(
					ServiceLocator.getDbAccessProperties(),
					ServiceLocator.getDbConstantsProperties(),
					dataSourceMetaInfo.getDataSource());
			cmd.execute();
		} catch (Exception e) {
			logger.error("Cannot create proteins table: " + e.getMessage(), e);
			throw new ScansiteUpdaterException("Cannot create proteins table",
					e);
		}
	}

	private void renameProteinTables(RenameTableCommand cmd,
			CommandConstants cmdConst) throws ScansiteUpdaterException {
		cmd.setFromTableName(cmdConst.getProteinsTableName(dataSourceMetaInfo
				.getDataSource()));
		cmd.setToTableName(CommandConstants.getOldTable(cmdConst
				.getProteinsTableName(dataSourceMetaInfo.getDataSource())));
		try {
			cmd.execute();
		} catch (Exception e) {
			logger.info("renaming old protein table failed, there is no old protein table of "
					+ dataSourceMetaInfo.getDataSource().getShortName());
		}
		cmd.setFromTableName(CommandConstants.getTempTable(cmdConst
				.getProteinsTableName(dataSourceMetaInfo.getDataSource())));
		cmd.setToTableName(cmdConst.getProteinsTableName(dataSourceMetaInfo
				.getDataSource()));
		try {
			cmd.execute();
		} catch (Exception e) {
			logger.error(
					"renaming temp protein table failed, there is no temp protein table of "
							+ dataSourceMetaInfo.getDataSource().getShortName(),
					e);
		}
	}

	private void renameAnnotationTables(RenameTableCommand cmd,
			CommandConstants cmdConst) throws ScansiteUpdaterException {
		cmd.setFromTableName(cmdConst
				.getAnnotationsTableName(dataSourceMetaInfo.getDataSource()));
		cmd.setToTableName(CommandConstants.getOldTable(cmdConst
				.getAnnotationsTableName(dataSourceMetaInfo.getDataSource())));
		try {
			cmd.execute();
		} catch (Exception e) {
			logger.info("renaming old annotation table failed, there is no old annotation table of "
					+ dataSourceMetaInfo.getDataSource().getShortName());
		}
		cmd.setFromTableName(CommandConstants.getTempTable(cmdConst
				.getAnnotationsTableName(dataSourceMetaInfo.getDataSource())));
		cmd.setToTableName(cmdConst.getAnnotationsTableName(dataSourceMetaInfo
				.getDataSource()));
		try {
			cmd.execute();
		} catch (Exception e) {
			logger.error(
					"renaming temp annotation table failed, there is no temp annotation table of "
							+ dataSourceMetaInfo.getDataSource().getShortName(),
					e);
			throw new ScansiteUpdaterException(e.getMessage(), e);
		}
	}

	private void renameTaxonTables(RenameTableCommand cmd,
			CommandConstants cmdConst) throws ScansiteUpdaterException {
		cmd.setFromTableName(cmdConst.getTaxaTableName(dataSourceMetaInfo
				.getDataSource()));
		cmd.setToTableName(CommandConstants.getOldTable(cmdConst
				.getTaxaTableName(dataSourceMetaInfo.getDataSource())));
		try {
			cmd.execute();
		} catch (Exception e) {
			logger.info("renaming old taxon table failed, there is no old taxon table of "
					+ dataSourceMetaInfo.getDataSource().getShortName());
		}
		cmd.setFromTableName(CommandConstants.getTempTable(cmdConst
				.getTaxaTableName(dataSourceMetaInfo.getDataSource())));
		cmd.setToTableName(cmdConst.getTaxaTableName(dataSourceMetaInfo
				.getDataSource()));
		try {
			cmd.execute();
		} catch (Exception e) {
			logger.error(
					"renaming temp taxon table failed, there is no temp taxon table of "
							+ dataSourceMetaInfo.getDataSource().getShortName(),
					e);
			throw new ScansiteUpdaterException(e.getMessage(), e);
		}
	}
}
