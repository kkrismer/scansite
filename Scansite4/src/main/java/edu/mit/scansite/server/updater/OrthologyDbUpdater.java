package edu.mit.scansite.server.updater;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DropTableCommand;
import edu.mit.scansite.server.dataaccess.commands.RenameTableCommand;
import edu.mit.scansite.server.dataaccess.commands.orthology.CreateOrthologyTableCommand;
import edu.mit.scansite.server.updater.transliterator.FileTransliterator;
import edu.mit.scansite.server.updater.transliterator.OrthologyTransliteratorDbWriter;
import edu.mit.scansite.server.updater.transliterator.ScansiteOrthologyFileTransliterator;
import edu.mit.scansite.shared.DatabaseException;

/**
 * @author Konstantin Krismer
 */
public abstract class OrthologyDbUpdater extends DbUpdater {

	@Override
	protected FileTransliterator getScansiteFileTransliterator(
			String errorFilePath) throws ScansiteUpdaterException {
		BufferedWriter errorWriter;
		try {
			errorWriter = new BufferedWriter(new FileWriter(new File(
					errorFilePath)));
			OrthologyTransliteratorDbWriter writer = new OrthologyTransliteratorDbWriter(
					errorWriter, dataSourceMetaInfo, dbConnector);
			ScansiteOrthologyFileTransliterator transliterator = new ScansiteOrthologyFileTransliterator(
					getReaders(), writer);
			return transliterator;
		} catch (IOException e) {
			throw new ScansiteUpdaterException(
					"Error accessing temporary files", e);
		}
	}

	@Override
	protected void createTables() throws ScansiteUpdaterException {
		try {
			CreateOrthologyTableCommand cmd = new CreateOrthologyTableCommand(
					ServiceLocator.getInstance().getDbAccessFile(),
					ServiceLocator.getInstance().getDbConstantsFile(),
					dbConnector, dataSourceMetaInfo.getDataSource());
			cmd.execute();
		} catch (Exception e) {
			logger.error("Cannot create orthology table: " + e.getMessage(), e);
			throw new ScansiteUpdaterException("Cannot create orthology table",
					e);
		}
	}

	@Override
	protected void renameTables() throws ScansiteUpdaterException {
		try {
			CommandConstants cmdConst = CommandConstants
					.instance(ServiceLocator.getInstance().getDbConstantsFile());
			RenameTableCommand cmd = new RenameTableCommand(ServiceLocator
					.getInstance().getDbAccessFile(), ServiceLocator
					.getInstance().getDbConstantsFile(), dbConnector,
					cmdConst.getOrthologsTableName(dataSourceMetaInfo
							.getDataSource()),
					CommandConstants.getOldTable(cmdConst
							.getOrthologsTableName(dataSourceMetaInfo
									.getDataSource())));

			// renaming existing orthology table to old orthology table
			try {
				cmd.execute();
			} catch (Exception e) {
				logger.info("there is no old orthology table of data source "
						+ dataSourceMetaInfo.getDataSource().getShortName());
			}

			// renaming temp orthology table to orthology table
			cmd.setFromTableName(CommandConstants.getTempTable(cmdConst
					.getOrthologsTableName(dataSourceMetaInfo.getDataSource())));
			cmd.setToTableName(cmdConst
					.getOrthologsTableName(dataSourceMetaInfo.getDataSource()));
			cmd.execute();
		} catch (DatabaseException e) {
			logger.error(
					"renaming temp orthology tables to standard orthology tables failed ("
							+ dataSourceMetaInfo.getDataSource().getShortName()
							+ "): " + e.getMessage(), e);
		}
	}

	@Override
	protected void dropOldTables() throws ScansiteUpdaterException {
		try {
			DropTableCommand cmd = new DropTableCommand(ServiceLocator
					.getInstance().getDbAccessFile(), ServiceLocator
					.getInstance().getDbConstantsFile(), dbConnector, null);
			CommandConstants cmdConst = CommandConstants
					.instance(ServiceLocator.getInstance().getDbConstantsFile());
			cmd.setTableName(CommandConstants.getOldTable(cmdConst
					.getOrthologsTableName(dataSourceMetaInfo.getDataSource())));
			cmd.execute();
		} catch (DatabaseException e) {
			logger.info("dropping old orthology table failed, there is no old table of "
					+ dataSourceMetaInfo.getDataSource().getShortName());
		}
	}

	@Override
	protected void dropTempTables() throws ScansiteUpdaterException {
		try {
			DropTableCommand cmd = new DropTableCommand(ServiceLocator
					.getInstance().getDbAccessFile(), ServiceLocator
					.getInstance().getDbConstantsFile(), dbConnector, null);
			CommandConstants cmdConst = CommandConstants
					.instance(ServiceLocator.getInstance().getDbConstantsFile());
			cmd.setTableName(CommandConstants.getTempTable(cmdConst
					.getOrthologsTableName(dataSourceMetaInfo.getDataSource())));
			cmd.execute();
		} catch (DatabaseException e) {
			logger.error("dropping temp orthology table failed of data source "
					+ dataSourceMetaInfo.getDataSource().getShortName() + ": "
					+ e.getMessage(), e);
		}
	}

	@Override
	protected void updateHistogramData() throws ScansiteUpdaterException {
		// nothing to do here
	}
}
