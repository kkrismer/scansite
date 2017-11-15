package edu.mit.scansite.server.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.HistogramDao;
import edu.mit.scansite.server.dataaccess.file.ImageInOut;
import edu.mit.scansite.server.dataaccess.file.ProteinScanResultFileWriter;
import edu.mit.scansite.server.domains.DomainLocator;
import edu.mit.scansite.server.domains.DomainLocatorException;
import edu.mit.scansite.server.domains.DomainLocatorFactory;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.server.images.proteins.ProteinPlotPainter;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.DomainPosition;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.LightWeightLocalization;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Localization;
import edu.mit.scansite.shared.transferobjects.LocalizationType;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import edu.mit.scansite.shared.transferobjects.ScanResults;
import edu.mit.scansite.shared.transferobjects.Taxon;
import edu.mit.scansite.shared.util.ScansiteAlgorithms;
import edu.mit.scansite.shared.util.ScansiteScoring;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinScanFeature {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ProteinScanFeature() {
	}

	/**
	 * Runs a protein scan using the given parameters.
	 * 
	 * @return A protein scan result containing the results of the scan.
	 * @throws DataAccessException
	 *             Is thrown if a database access related error occurs.
	 */
	public ProteinScanResult doProteinScan(LightWeightProtein protein, MotifSelection motifSelection,
			HistogramStringency stringency, boolean showDomains, String histogramDataSource, String histogramTaxon,
			DataSource localizationDataSource, boolean doCreateFiles, boolean publicOnly, String realPath)
			throws DataAccessException {

		DaoFactory factory = ServiceLocator.getDaoFactory();
		ArrayList<DomainPosition> domainPositions = null;

		List<Motif> motifs = factory.getMotifDao().getSelectedMotifs(motifSelection, publicOnly);
		Map<Motif, LightWeightLocalization> motifLocalizations = null;
		Localization proteinLocalization = null;
		if (localizationDataSource != null) {
			if (locatableMotifs(motifs)) {
				motifLocalizations = factory.getLocalizationDao().retrieveLocalizationsForMotifs(localizationDataSource,
						motifs);
			}
			proteinLocalization = factory.getLocalizationDao().retrieveLocalization(localizationDataSource, protein);
		}

		if (showDomains) {
			try {
				DomainLocator domainLocator = DomainLocatorFactory.getDomainLocator();
				domainLocator.init();
				domainPositions = domainLocator.getDomainPositions(realPath, protein.getSequence());
			} catch (DomainLocatorException e) {
				logger.error(e.getMessage(), e);
				domainPositions = null;
			}
		}

		ScansiteAlgorithms alg = new ScansiteAlgorithms();
		Double[] saValues = alg.calculateSurfaceAccessibility(protein.getSequence());
		DataSource ds = factory.getDataSourceDao().get(histogramDataSource);
		Taxon t = factory.getTaxonDao().getByName(histogramTaxon, ds);
		HistogramDao histDao = factory.getHistogramDao();
		ScansiteScoring scoring = new ScansiteScoring();

		ArrayList<ScanResultSite> hits = new ArrayList<ScanResultSite>();
		if (!motifs.isEmpty()) {
			if (locatableMotifs(motifs)) {
				List<ServerHistogram> sHists = histDao.getHistograms(motifs, ds, t.getId());
				for (ServerHistogram sh : sHists) {
					double maxScore = sh.getScore(stringency.getPercentileValue());
					ArrayList<ScanResultSite> sites = scoring.scoreProtein(sh.getMotif(), protein, maxScore);
					for (ScanResultSite site : sites) {
						if (motifLocalizations != null) {
							if (motifLocalizations.containsKey(sh.getMotif())) {
								site.setMotifLocalization(motifLocalizations.get(sh.getMotif()));
							} else {
								site.setMotifLocalization(
										new LightWeightLocalization(new LocalizationType("unknown / NA"), 0));
							}
						} else {
							site.setMotifLocalization(
									new LightWeightLocalization(new LocalizationType("unknown / NA"), 0));
						}
						site.setLocalizationDataSource(localizationDataSource);
						site.setPercentile(sh.getPercentile(site.getScore()));
						site.setSurfaceAccessValue(saValues[site.getPosition()]);
					}
					hits.addAll(sites);
				}
			} else { // user motifs
				double maxScore = stringency.getPercentileValue(); // used to be
																	// MAX_SCORING_SCORE
				for (Motif motif : motifs) {
					ArrayList<ScanResultSite> sites = scoring.scoreProtein(motif, protein, maxScore);
					for (ScanResultSite site : sites) {
						site.setSurfaceAccessValue(saValues[site.getPosition()]);
					}
					hits.addAll(sites);
				}
			}
		}

		ProteinPlotPainter ppPainter = new ProteinPlotPainter(protein.toString(), protein.getSequence(), saValues,
				showDomains, domainPositions);
		ppPainter.applyHits(hits);

		ScanResults results = new ScanResults();
		if (doCreateFiles) {
			String imagePath = FilePaths.getProteinPlotFilePath(realPath);
			ImageInOut iio = new ImageInOut();
			iio.saveImage(ppPainter.getBufferedImage(), imagePath);
			String clientImagePath = imagePath.replace(realPath, "");
			if (clientImagePath.startsWith("/") || clientImagePath.startsWith("\\")) {
				clientImagePath = clientImagePath.substring(1);
			}
			results.setImagePath(clientImagePath);
		}

		results.setOrthologyDataSources(findOrthologyDataSource(factory, protein.getDataSource()));
		results.setLocalizationDataSource(localizationDataSource);
		results.setProteinLocalization(proteinLocalization);
		results.setHistogramThreshold(stringency);
		results.setHits(hits);
		results.setProtein(protein);
		results.setShowDomains(showDomains);
		results.setHistogramDataSourceSelection(ds);
		results.setHistogramTaxonName(histogramTaxon);
		results.setDomainPositions(domainPositions);
		results.setMotifSelection(motifSelection);

		if (doCreateFiles) {
			try { // write result file for user download
				ProteinScanResultFileWriter writer = new ProteinScanResultFileWriter();
				results.setResultFilePath(writer.writeResults(realPath, hits).replace(realPath, ""));
			} catch (Exception e) {
				logger.error("Error writing result file for protein scan: " + e.toString(), e);
			}
		}

		return new ProteinScanResult(results);
	}

	private boolean locatableMotifs(List<Motif> motifs) {
		for (Motif motif : motifs) {
			if (motif.getId() > 0 && motif.getIdentifiers() != null && !motif.getIdentifiers().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private List<DataSource> findOrthologyDataSource(DaoFactory factory, DataSource dataSource) {
		try {
			if (dataSource != null) {
				return factory.getIdentifierDao()
						.getCompatibleOrthologyDataSourcesForIdentifierType(dataSource.getIdentifierType());
			} else {
				return null;
			}
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	// /**
	// * Runs a protein scan using the given parameters. This method is intended
	// to
	// * be used by a webservice.
	// *
	// * @param proteinName
	// * A protein name. This parameter either has to be a protein
	// * accession number if a datasourceShortName is given, or any other
	// * name, if a proteinSequence is given.
	// * @param proteinSequence
	// * A protein sequence.
	// * @param datasourceShortName
	// * The shortname of an existing datasource.
	// * @param motifShortNames
	// * A list of existing motifshortnames. These motifs will be used to
	// * scan the given protein.
	// * @param motifClass
	// * A motif-class. The given shortnames must describe motifs from the
	// * given class.
	// * @param stringency
	// * A stringency value. If a usermotif is given, no stringency is used
	// * (because there is no reference histogram available). Instead a
	// * maximum cutoff score of 5 is used as threshold.
	// * @return A protein scan result containing the results of the scan.
	// * @throws DataAccessException
	// * Is thrown if a database access related error occurs.
	// */
	// public ProteinScanResult doProteinScan(boolean isDatabaseProtein,
	// String proteinName, String proteinSequence, String datasourceShortName,
	// ArrayList<String> motifShortNames, MotifClass motifClass,
	// HistogramStringency stringency) throws DataAccessException {
	// return doProteinScan(
	// isDatabaseProtein,
	// proteinName,
	// proteinSequence,
	// datasourceShortName,
	// motifShortNames,
	// motifClass,
	// false,
	// null,
	// stringency,
	// null,
	// false,
	// null,
	// ScansiteConstants.HIST_DEFAULT_DATASOURCE_SHORTS[ScansiteConstants.HIST_DEFAULT_INDEX],
	// ScansiteConstants.HIST_DEFAULT_TAXON_NAMES[ScansiteConstants.HIST_DEFAULT_INDEX],
	// false, "");
	// }
}
