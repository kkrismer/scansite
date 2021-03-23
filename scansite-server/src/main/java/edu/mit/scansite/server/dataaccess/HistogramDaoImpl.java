package edu.mit.scansite.server.dataaccess;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.commands.histogram.HistogramAddCommand;
import edu.mit.scansite.server.dataaccess.commands.histogram.HistogramDataAddCommand;
import edu.mit.scansite.server.dataaccess.commands.histogram.HistogramDataDeleteCommand;
import edu.mit.scansite.server.dataaccess.commands.histogram.HistogramDeleteCommand;
import edu.mit.scansite.server.dataaccess.commands.histogram.HistogramGetAllCommand;
import edu.mit.scansite.server.dataaccess.commands.histogram.HistogramGetThresholdCommand;
import edu.mit.scansite.server.dataaccess.commands.histogram.HistogramUpdateCommand;
import edu.mit.scansite.server.dataaccess.commands.histogram.HistogramGetAllCommand.GetHistogramMode;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramDataPoint;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramDaoImpl extends DaoImpl implements HistogramDao {

	public HistogramDaoImpl(Properties dbAccessConfig, Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.HistogramDao#add(edu.mit.scansite.
	 * server.images.histograms.ServerHistogram)
	 */
	@Override
	public void add(ServerHistogram histogram) throws DataAccessException {
		Motif motif = histogram.getMotif();
		if (motif.getId() <= 0) {
			int id = ServiceLocator.getDaoFactory().getMotifDao().addMotif(motif);
			motif.setId(id);
			histogram.setMotif(motif);
		}

		try {
			HistogramAddCommand cmd = new HistogramAddCommand(dbConstantsConfig, histogram);

			cmd.execute();
			try {
				File f = new File(histogram.getImageFilePath());
				f.delete();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			ArrayList<HistogramDataPoint> scores = histogram.getDataPoints();
			for (HistogramDataPoint p : scores) {
				HistogramDataAddCommand cmdData = new HistogramDataAddCommand(dbAccessConfig, dbConstantsConfig,
						histogram, p.getScore(), (int) p.getAbsFreq());
				cmdData.execute();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.HistogramDao#deleteHistograms(edu.
	 * mit.scansite.shared.transferobjects.Motif,
	 * edu.mit.scansite.shared.transferobjects.DataSource,
	 * edu.mit.scansite.shared.transferobjects.Taxon)
	 */
	@Override
	public void deleteHistograms(Motif motif, DataSource dataSource, Taxon taxon) throws DataAccessException {
		if (motif != null && motif.getId() > 0) {
			HistogramDataDeleteCommand cmd1 = new HistogramDataDeleteCommand(dbAccessConfig, dbConstantsConfig,
					motif.getId(), (dataSource != null) ? dataSource.getId() : -1,
					(taxon != null) ? taxon.getId() : -1);
			HistogramDeleteCommand cmd2 = new HistogramDeleteCommand(dbAccessConfig, dbConstantsConfig, motif.getId(),
					(dataSource != null) ? dataSource.getId() : -1, (taxon != null) ? taxon.getId() : -1);
			try {
				cmd1.execute();
				cmd2.execute();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new DataAccessException(e.getMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.HistogramDao#getHistograms(java.util
	 * .List, edu.mit.scansite.shared.transferobjects.DataSource, int)
	 */
	@Override
	public List<ServerHistogram> getHistograms(List<Motif> motifs, DataSource dataSource, int taxonId)
			throws DataAccessException {
		List<ServerHistogram> hists = new ArrayList<ServerHistogram>();
		try {
			HistogramGetAllCommand cmd = new HistogramGetAllCommand(dbAccessConfig, dbConstantsConfig, motifs,
					dataSource, taxonId, GetHistogramMode.GENERAL_INFO_ONLY);
			hists = cmd.execute();
			// get histogramData
			cmd = new HistogramGetAllCommand(dbAccessConfig, dbConstantsConfig, motifs, dataSource, taxonId,
					GetHistogramMode.DATA_ONLY);
			List<ServerHistogram> tempHists = cmd.execute();

			DaoFactory daoFac = ServiceLocator.getDaoFactory();
			Map<Integer, DataSource> datasources = new HashMap<Integer, DataSource>();
			Map<Integer, Taxon> taxa = new HashMap<Integer, Taxon>();
			int tId = -1;
			int dsId = -1;
			DataSource currentDataSource = null;
			Taxon t = null;
			for (ServerHistogram h : hists) {
				tId = h.getTaxon().getId();
				dsId = h.getDataSource().getId();
				if (datasources.containsKey(dsId)) {
					currentDataSource = datasources.get(dsId);
				} else {
					currentDataSource = daoFac.getDataSourceDao().get(dsId);
					datasources.put(dsId, currentDataSource);
				}
				if (taxa.containsKey(tId)) {
					t = taxa.get(tId);
				} else {
					t = daoFac.getTaxonDao().getById(tId, currentDataSource);
					if (t == null) {
						throw new DataAccessException(
								"Invalid taxon id in histogram data entry (taxon ID: " + tId + ")");
					}
					taxa.put(tId, t);
				}

				h.setTaxon(t);
				h.setDataSource(currentDataSource);
				if (motifs == null || motifs.isEmpty()) {
					Motif m = daoFac.getMotifDao().getById(h.getMotif().getId());
					h.setMotif(m);
				}

				ServerHistogram currentDataHist = null;
				for (ServerHistogram dh : tempHists) {
					currentDataHist = dh;
					if (h.getMotif().getId() == dh.getMotif().getId() && dsId == dh.getDataSource().getId()
							&& tId == dh.getTaxon().getId()) {
						h.addDataPoints(dh.getDataPoints());
						break;
					}
				}
				tempHists.remove(currentDataHist);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return hists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.HistogramDao#getHistogram(int,
	 * java.lang.String, edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public ServerHistogram getHistogram(int motifId, String taxonName, DataSource dataSource)
			throws DataAccessException {
		if (motifId > 0 && taxonName != null && dataSource != null) {
			ArrayList<Motif> motifs = new ArrayList<Motif>();
			Taxon t = ServiceLocator.getDaoFactory().getTaxonDao().getByName(taxonName, dataSource);
			motifs.add(ServiceLocator.getDaoFactory().getMotifDao().getById(motifId));
			List<ServerHistogram> hists = getHistograms(motifs, dataSource, t.getId());
			if (hists.size() == 1) {
				ServerHistogram h = hists.get(0);
				return h;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.HistogramDao#updateHistogram(edu.mit
	 * .scansite.server.images.histograms.ServerHistogram, boolean)
	 */
	@Override
	public void updateHistogram(ServerHistogram hist, boolean updateData) throws DataAccessException {
		try {
			HistogramUpdateCommand cmd = new HistogramUpdateCommand(dbConstantsConfig, hist);
			cmd.execute();
			if (updateData) {
				// remove old data
				HistogramDataDeleteCommand delData = new HistogramDataDeleteCommand(dbAccessConfig, dbConstantsConfig,
						hist.getMotif().getId(), hist.getDataSource().getId(), hist.getTaxon().getId());
				delData.execute();

				// add new data
				ArrayList<HistogramDataPoint> scores = hist.getDataPoints();
				for (HistogramDataPoint p : scores) {
					HistogramDataAddCommand cmdData = new HistogramDataAddCommand(dbAccessConfig, dbConstantsConfig,
							hist, p.getScore(), (int) p.getAbsFreq());
					cmdData.execute();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.HistogramDao#getThresholdValue(int,
	 * int, edu.mit.scansite.shared.transferobjects.DataSource,
	 * edu.mit.scansite.shared.transferobjects.HistogramStringency)
	 */
	@Override
	public Double getThresholdValue(int motifId, int taxonId, DataSource dataSource, HistogramStringency stringency)
			throws DataAccessException {
		if (motifId > 0 && taxonId > 0 && dataSource != null && stringency != null) {
			try {
				HistogramGetThresholdCommand cmd = new HistogramGetThresholdCommand(dbAccessConfig, dbConstantsConfig,
						motifId, taxonId, dataSource, stringency);
				return cmd.execute();
			} catch (DatabaseException e) {
				logger.error(e.getMessage(), e);
				throw new DataAccessException(e.getMessage(), e);
			}
		}
		return null;
	}
}
