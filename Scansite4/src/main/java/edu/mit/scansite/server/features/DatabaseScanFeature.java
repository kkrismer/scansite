package edu.mit.scansite.server.features;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.dataaccess.file.DatabaseSearchResultFileWriter;
import edu.mit.scansite.server.dataaccess.file.ImageInOut;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.server.scoring.ParallelDbSearchScorer;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanResult;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.DatabaseSearchScanResultSite;
import edu.mit.scansite.shared.transferobjects.HistogramDataPoint;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DatabaseScanFeature {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private DbConnector dbConnector;

	public DatabaseScanFeature(DbConnector dbConnector) {
		this.dbConnector = dbConnector;
	}

	public DatabaseScanResult doDatabaseSearch(MotifSelection motifSelection,
			DataSource dataSource, RestrictionProperties restrictionProperties,
			int outputListSize, boolean doCreateFiles, boolean publicOnly, String realPath)
			throws DataAccessException {
		DaoFactory factory = ServiceLocator.getInstance().getDaoFactory(
				dbConnector);

		DatabaseScanResult result = new DatabaseScanResult();
		result.setMotifSelection(motifSelection);
		result.setDataSource(dataSource);
		result.setRestrictionProperties(restrictionProperties);
		result.setOutputListSize(outputListSize);
		result.setSuccess(false); // set to true after everything is done!

		int totalNrOfProteinsInDb = factory.getProteinDao().getProteinCount(
				dataSource);
		List<Protein> proteins = factory.getProteinDao().get(dataSource,
				restrictionProperties, true, true);
		int nrOfProteinsRetrieved = proteins.size();

		if (nrOfProteinsRetrieved > 0) {
			// get motifs from db
			List<Motif> motifs = factory.getMotifDao().getSelectedMotifs(
					motifSelection, publicOnly);
			List<Double> motifScoreThresholds = scoreMotifs(motifs);

			ParallelDbSearchScorer scorer = new ParallelDbSearchScorer(motifs,
					motifScoreThresholds, proteins, false, null);
			try {
				scorer.doScoring();
			} catch (Exception e1) {
				logger.error("Error scoring proteins for database search: "
						+ e1.toString());
				return new DatabaseScanResult(
						"Error scoring proteins for database search");
			}
			double median = scorer.getMedian();
			double medianAbsDev = scorer.getMedianAbsDev();
			ArrayList<HistogramDataPoint> histDpList = scorer
					.getHistogramDataPointList();
			int protCount = scorer.getProteinCount();
			int totalSiteCount = scorer.getTotalSiteCount();
			int combinedSiteCount = scorer.getCombinedSiteCount();
			ArrayList<DatabaseSearchScanResultSite> sites = scorer.getSites();

			try {
				String histTaxonName = (restrictionProperties.getSpeciesRegEx() != null && !restrictionProperties
						.getSpeciesRegEx().isEmpty()) ? restrictionProperties
						.getSpeciesRegEx() : restrictionProperties
						.getOrganismClass().getDisplayName();
				Motif histMotif = null;
				if (motifs.size() > 1) {
					histMotif = new Motif();
					histMotif.setDisplayName("Multiple Motifs");
					histMotif.setShortName("Multiple Motifs");
				} else if (motifs.size() == 1) {
					histMotif = motifs.get(0);
				} else { // cannot really happen!
					histMotif = new Motif();
					histMotif.setDisplayName("None");
					histMotif.setShortName("None");
				}
				ServerHistogram sHist = new ServerHistogram(histMotif,
						dataSource, new Taxon(histTaxonName));
				sHist.setDataPoints(histDpList);
				sHist.setMedian(median);
				sHist.setMedianAbsDev(medianAbsDev);
				sHist.setProteinsScored(protCount);
				sHist.setSitesScored(combinedSiteCount);
				BufferedImage hist = sHist.getDbHistogramPlot();
				if (doCreateFiles) {
					ImageInOut iio = new ImageInOut();
					String filePath = FilePaths.getHistogramFilePath(realPath, null,
							System.nanoTime());
					iio.saveImage(hist, filePath);
					result.setHistogramBasePath(filePath.replace(realPath, ""));
				}
			} catch (Exception e) {
				logger.error("Error creating reference histogram: "
						+ e.toString());
			}

			Collections.sort(sites);

			List<String> motifDisplayNames = new LinkedList<String>();
			for (Motif motif : motifs) {
				motifDisplayNames.add(motif.getDisplayName());
			}
			result.setMotifDisplayNames(motifDisplayNames);

			ArrayList<DatabaseSearchScanResultSite> temp = new ArrayList<DatabaseSearchScanResultSite>();
			if (outputListSize <= 0) {
				outputListSize = sites.size();
			}
			for (int i = 0; i < outputListSize && i < sites.size(); ++i) {
				temp.add(sites.get(i));
			}
			sites.clear();
			sites = temp;

			result.setDbSearchSites(sites);
			result.setTotalNrOfSites(totalSiteCount);
			result.setNrOfCombinedSites(combinedSiteCount);
			result.setNrOfProteinsFound(protCount);
			result.setTotalNrOfProteinsInDb(totalNrOfProteinsInDb);
			result.setMedian(median);
			result.setMedianAbsDev(medianAbsDev);
			result.setSuccess(true);
			Runtime.getRuntime().gc();

			if (doCreateFiles) {
				try {
					DatabaseSearchResultFileWriter writer = new DatabaseSearchResultFileWriter(
							motifs);
					result.setResultFilePath(writer.writeResults(realPath, sites).replace(realPath, ""));
				} catch (Exception e) {
					logger.error("Error writing result file: " + e.toString(),
							e);
				}
			}

			return result;
		} else {
			return new DatabaseScanResult(
					"No proteins meet specified restrictions");
		}
	}

	private List<Double> scoreMotifs(List<Motif> motifs)
			throws DataAccessException {
		DaoFactory factory = ServiceLocator.getInstance().getDaoFactory(
				dbConnector);
		DataSource defaultHistogramDataSource = factory
				.getDataSourceDao()
				.get(ScansiteConstants.HIST_DEFAULT_DATASOURCE_SHORTS[ScansiteConstants.HIST_DEFAULT_INDEX]);
		Taxon defaultHistogramTaxon = factory
				.getTaxonDao()
				.getByName(
						ScansiteConstants.HIST_DEFAULT_TAXON_NAMES[ScansiteConstants.HIST_DEFAULT_INDEX],
						defaultHistogramDataSource);
		HistogramStringency stringency = HistogramStringency.STRINGENCY_HIGH;

		List<Double> motifScoreThresholds = new ArrayList<Double>();
		for (Motif motif : motifs) {
			if (motif.getId() > 0) {
				motifScoreThresholds.add(factory.getHistogramDao()
						.getThresholdValue(motif.getId(),
								defaultHistogramTaxon.getId(),
								defaultHistogramDataSource, stringency));
			} else {
				motifScoreThresholds
						.add(ScansiteConstants.MAX_SCORING_SCORE_DBSEARCH_USERMOTIF);
			}
		}
		return motifScoreThresholds;
	}
}
