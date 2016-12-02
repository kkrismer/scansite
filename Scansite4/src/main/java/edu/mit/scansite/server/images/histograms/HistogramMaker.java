package edu.mit.scansite.server.images.histograms;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.ProteinDao;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.scoring.ParallelHistogramScorer;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * A class that creates histograms.
 * 
 * @author tobieh
 */
public class HistogramMaker {
	private static final Logger logger = LoggerFactory
			.getLogger(HistogramMaker.class);

	/**
	 * Makes a ServerHistogram. Motif, Datasource and Taxon have to be available
	 * in the database!
	 * 
	 * @param motif
	 *            A motif.
	 * @param datasource
	 *            A datasource.
	 * @param taxon
	 *            A taxon. Please <b>note</b> that <i>ALL of its sub-taxa</i>
	 *            are also included in the protein query.
	 * @return A histogram with all its fields set.
	 * @throws DataAccessException
	 *             Is thrown if the database can not be accessed.
	 */
	public ServerHistogram makeServerHistogram(Motif motif,
			DataSource dataSource, Taxon taxon) throws DataAccessException {
		return makeServerHistogram(motif, dataSource, taxon, false,
				ServiceLocator.getInstance().getDaoFactory());
	}

	public ServerHistogram makeServerHistogram(Motif motif,
			DataSource dataSource, Taxon taxon, boolean useTempTables)
			throws DataAccessException {
		return makeServerHistogram(motif, dataSource, taxon, useTempTables,
				ServiceLocator.getInstance().getDaoFactory());
	}

	public ServerHistogram makeServerHistogram(Motif motif,
			DataSource dataSource, Taxon taxon, boolean useTempTables,
			DbConnector dbConnector) throws DataAccessException {
		return makeServerHistogram(motif, dataSource, taxon, useTempTables,
				ServiceLocator.getInstance().getDaoFactory(dbConnector));
	}

	public ServerHistogram makeServerHistogram(Motif motif,
			DataSource dataSource, Taxon taxon, boolean useTempTables,
			DaoFactory factory) throws DataAccessException {
		try {
			long start = System.currentTimeMillis();
			
			ServerHistogram histogram = new ServerHistogram(motif, dataSource,
					taxon);
			ProteinDao pDao = factory.getProteinDao();
			pDao.setUseTempTablesForUpdate(useTempTables);
			List<Protein> proteins = pDao.getAll(taxon, dataSource, false);


			ParallelHistogramScorer scorer = new ParallelHistogramScorer(motif,
					proteins);
			scorer.doScoring();
			histogram.setProteinsScored(scorer.getProteinCount());
			histogram.setSitesScored(scorer.getSiteCount());
			histogram.setMedian(scorer.getMedian());
			histogram.setMedianAbsDev(scorer.getMedianAbsDev());
			histogram.addDataPoints(scorer.getHistogramDataPointList());

			long end = System.currentTimeMillis();
			logger.info("scored all proteins in data source "
					+ dataSource.getDisplayName() + " for histogram of "
					+ motif.getDisplayName() + " motif in " +
					(end - start) / 1000.0 + "s");

			return histogram;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}
}
