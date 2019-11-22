package edu.mit.scansite.server.features;

import java.util.ArrayList;
import java.util.List;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.HistogramDao;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.OrganismClass;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import edu.mit.scansite.shared.transferobjects.Taxon;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.util.ScansiteAlgorithms;
import edu.mit.scansite.shared.util.ScansiteScoring;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class PhosphoSitesFeature {

	public PhosphoSitesFeature() {
	}

	private String histogramTaxonName = ScansiteConstants.HIST_DEFAULT_TAXON_NAMES[ScansiteConstants.HIST_DEFAULT_INDEX];
	private String histogramDatasourceShortName = ScansiteConstants.HIST_DEFAULT_DATASOURCE_SHORTS[ScansiteConstants.HIST_DEFAULT_INDEX];
	private List<ServerHistogram> mammalianServerHistograms = new ArrayList<ServerHistogram>();
	private List<ServerHistogram> yeastServerHistograms = new ArrayList<ServerHistogram>();
	private List<ServerHistogram> otherServerHistograms = new ArrayList<ServerHistogram>();

	public List<ScanResultSite> checkPositionSpecificPhosphoSites(Protein protein, int position,
			HistogramStringency stringency, User user) throws DataAccessException {
		return checkPositionSpecificPhosphoSites(protein, position, stringency, null, protein.getOrganismClass(), user);
	}

	public List<ScanResultSite> checkPositionSpecificPhosphoSites(Protein protein, int position,
			HistogramStringency stringency, LightWeightMotifGroup motifGroup, User user) throws DataAccessException {
		return checkPositionSpecificPhosphoSites(protein, position, stringency, motifGroup, protein.getOrganismClass(),
				user);
	}

	public List<ScanResultSite> checkPositionSpecificPhosphoSites(Protein protein, int position,
			HistogramStringency stringency, OrganismClass motifOrganismClass, User user) throws DataAccessException {
		return checkPositionSpecificPhosphoSites(protein, position, stringency, null, motifOrganismClass, user);
	}

	public List<ScanResultSite> checkPositionSpecificPhosphoSites(Protein protein, int position,
			HistogramStringency stringency, LightWeightMotifGroup motifGroup, OrganismClass motifOrganismClass,
			User user) throws DataAccessException {
		DaoFactory daoFac = ServiceLocator.getDaoFactory();
		ScansiteAlgorithms alg = new ScansiteAlgorithms();
		ScansiteScoring scoring = new ScansiteScoring();
		List<ScanResultSite> hits = new ArrayList<ScanResultSite>();
		List<ServerHistogram> sHists;

		Double[] saValues = alg.calculateSurfaceAccessibility(protein.getSequence());

		DataSource ds = daoFac.getDataSourceDao().get(histogramDatasourceShortName);
		Taxon t = daoFac.getTaxonDao().getByName(histogramTaxonName, ds);
		HistogramDao histDao = daoFac.getHistogramDao();

		switch (motifOrganismClass) {
		case FUNGI:
			// lazy evaluation
			if (yeastServerHistograms.size() == 0) {
				List<Motif> yeastMotifs;
				if (motifGroup != null) {
					yeastMotifs = daoFac.getMotifDao().getByGroup(motifGroup, MotifClass.YEAST, user);
				} else {
					yeastMotifs = daoFac.getMotifDao().getAll(MotifClass.YEAST, user);
				}

				yeastServerHistograms = histDao.getHistograms(yeastMotifs, ds, t.getId());
			}
			sHists = yeastServerHistograms;
			break;
		case MAMMALIA:
			// lazy evaluation
			if (mammalianServerHistograms.size() == 0) {
				List<Motif> mammalianMotifs;
				if (motifGroup != null) {
					mammalianMotifs = daoFac.getMotifDao().getByGroup(motifGroup, MotifClass.MAMMALIAN, user);
				} else {
					mammalianMotifs = daoFac.getMotifDao().getAll(MotifClass.MAMMALIAN, user);
				}

				mammalianServerHistograms = histDao.getHistograms(mammalianMotifs, ds, t.getId());
			}
			sHists = mammalianServerHistograms;
			break;
		default:
			// lazy evaluation
			if (otherServerHistograms.size() == 0) {
				List<Motif> otherMotifs;
				if (motifGroup != null) {
					otherMotifs = daoFac.getMotifDao().getByGroup(motifGroup, MotifClass.OTHER, user);
				} else {
					otherMotifs = daoFac.getMotifDao().getAll(MotifClass.OTHER, user);
				}

				otherServerHistograms = histDao.getHistograms(otherMotifs, ds, t.getId());
			}
			sHists = otherServerHistograms;
			break;
		}

		for (ServerHistogram sh : sHists) {
			double maxScore = sh.getScore(stringency.getPercentileValue());
			double score = scoring.calculateSiteScore(position, protein.getSequence().toCharArray(), sh.getMotif());

			if (score >= 0.0 && score <= maxScore) {
				ScanResultSite site = new ScanResultSite(sh.getMotif(), new LightWeightProtein(protein), null, null,
						position, scoring.roundScore(score));
				site.setPercentile(sh.getPercentile(site.getScore()));
				site.setSurfaceAccessValue(saValues[site.getPosition()]);
				hits.add(site);
			}
		}
		return hits;
	}
}
