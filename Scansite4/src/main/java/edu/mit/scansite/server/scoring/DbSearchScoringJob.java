package edu.mit.scansite.server.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.scansite.shared.transferobjects.DatabaseSearchMultipleRestriction;
import edu.mit.scansite.shared.transferobjects.DatabaseSearchScanResultSite;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.util.ScansiteScoring;

/**
 * A scoring job that is used for paralle database search scoring.
 * 
 * @author tobieh
 * @param <T>
 *            Dependent on whether the scorer is used for multiple motif search
 *            or single motif search: Either ScanResultSite or
 *            CombinedScanResultSite.
 */
public class DbSearchScoringJob extends ScoringJob {

	private List<Motif> motifs;
	private List<Double> motifThresholds;
	private Protein[] proteins;
	private boolean isMultiple;
	private DatabaseSearchMultipleRestriction restrictions;

	private ScansiteScoring scoring = new ScansiteScoring();
	private Map<Double, Integer> score2Counts;
	private ArrayList<DatabaseSearchScanResultSite> sites = new ArrayList<DatabaseSearchScanResultSite>();
	private int siteCount = 0;
	private int combinedSiteCount = 0;

	public DbSearchScoringJob(List<Motif> motifs, List<Double> motifThresholds,
			Protein[] partition, boolean isMultiple,
			DatabaseSearchMultipleRestriction restrictions) {
		super();
		this.motifs = motifs;
		this.motifThresholds = motifThresholds;
		this.proteins = partition;
		this.isMultiple = isMultiple;
		this.restrictions = restrictions;
	}

	@Override
	protected void doRun() {
		score2Counts = new HashMap<Double, Integer>();
		if (motifs.size() > 0) {
			List<DatabaseSearchScanResultSite> hits = null;
			if (isMultiple) {
				hits = scoring.scoreProteinsDbSearchMultiMotif(motifs,
						proteins, motifThresholds, restrictions);
			} else { // single motif search, only first in list is used (in case
						// there are more than just one)
				Motif m = motifs.get(0);
				hits = scoring.scoreProteinsDbSearch(m, proteins,
						motifThresholds.get(0));
			}
			for (DatabaseSearchScanResultSite site : hits) {
				double score = scoring.roundScore(site.getScore());
				Integer count = score2Counts.get(score);
				score2Counts.put(score, (count == null) ? 1 : count + 1);
			}
			combinedSiteCount += hits.size();
			siteCount = scoring.getSiteCount();
			sites.addAll(hits);
		}
	}

	public ArrayList<DatabaseSearchScanResultSite> getSites() {
		return sites;
	}

	public Map<Double, Integer> getScore2Counts() {
		return score2Counts;
	}

	public int getSiteCount() {
		return siteCount;
	}

	public int getCombinedSiteCount() {
		return combinedSiteCount;
	}
}
