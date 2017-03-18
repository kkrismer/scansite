package edu.mit.scansite.server.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.scansite.shared.transferobjects.DatabaseSearchScanResultSite;
import edu.mit.scansite.shared.transferobjects.DatabaseSearchMultipleRestriction;
import edu.mit.scansite.shared.transferobjects.HistogramDataPoint;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.util.ScansiteAlgorithms;

/**
 * @author tobieh
 * @param <T>
 *            Dependent on whether the scorer is used for multiple motif search
 *            or single motif search: Either ScanResultSite or
 *            CombinedScanResultSite.
 */
public class ParallelDbSearchScorer extends ParallelScorer<DbSearchScoringJob> {

	private List<Motif> motifs;
	private List<Double> motifThresholds;
	private List<Protein> proteins;
	private boolean isMultiple = false;
	private DatabaseSearchMultipleRestriction restrictions = null;

	private Map<Double, Integer> scoringCounts = new HashMap<Double, Integer>();
	private int totalSiteCount = 0;
	private int combinedSiteCount = 0;
	private double median = 0;
	private double medianAbsDev = 0;
	private ArrayList<HistogramDataPoint> histogramDatapointList;
	private ArrayList<DatabaseSearchScanResultSite> sites = new ArrayList<DatabaseSearchScanResultSite>();

	public ParallelDbSearchScorer(List<Motif> motifs,
			List<Double> motifThresholds, List<Protein> proteins,
			boolean isMultiple, DatabaseSearchMultipleRestriction restrictions) {
		super();
		this.motifs = motifs;
		this.isMultiple = isMultiple;
		this.motifThresholds = motifThresholds;
		this.restrictions = restrictions;
		this.proteins = proteins;
	}

	@Override
	protected void doPrepareJobs() {
		Protein[] ps = proteins.toArray(new Protein[] {});
		int nrOfProts = ps.length;
		int countPerPartition = nrOfProts / THREAD_COUNT;
		int partitionOverhead = nrOfProts % THREAD_COUNT;
		int partitionOffset = 0;
		for (int i = 0; i < THREAD_COUNT; ++i) {
			int partitionSize = countPerPartition;
			if (partitionOverhead > 0) {
				--partitionOverhead;
				++partitionSize;
			}
			Protein[] partition = new Protein[partitionSize];
			System.arraycopy(ps, partitionOffset, partition, 0, partitionSize);
			partitionOffset += partitionSize;

			addScoringJob(new DbSearchScoringJob(motifs, motifThresholds,
					partition, isMultiple, restrictions));
		}
	}

	@Override
	protected void doSaveJobResults(DbSearchScoringJob job) {
		saveSites(job.getSites());
		saveScoreCounts(job.getScore2Counts());
		totalSiteCount += job.getSiteCount();
		combinedSiteCount += job.getCombinedSiteCount();
	}

	private synchronized void saveSites(
			ArrayList<DatabaseSearchScanResultSite> sites) {
		this.sites.addAll(sites);
	}

	@Override
	protected void doAfterScoring() {
		calculateMedianAndMAD();
		saveHistogramDatapoints();
	}

	private void saveHistogramDatapoints() {
		ScansiteAlgorithms sa = new ScansiteAlgorithms();
		histogramDatapointList = sa.getHistogramBins(scoringCounts);
	}

	private void calculateMedianAndMAD() {
		// prepare score list for median and MAD calculation
		List<Double> scores = new ArrayList<Double>();
		for (Double score : scoringCounts.keySet()) {
			for (int i = 0; i < scoringCounts.get(score); ++i) {
				scores.add(score);
			}
		}
		median = ScansiteAlgorithms.median(scores);
		medianAbsDev = ScansiteAlgorithms.medianAbsDev(scores, median);
		scores.clear();
	}

	private void saveScoreCounts(Map<Double, Integer> score2Counts) {
		for (Double score : score2Counts.keySet()) {
			addScoringCount(score, score2Counts.get(score));
		}
	}

	private synchronized void addScoringCount(Double score, Integer count) {
		if (scoringCounts.containsKey(score)) {
			putScoringCount(score, scoringCounts.get(score) + count);
		} else {
			putScoringCount(score, count);
		}
	}

	private synchronized void putScoringCount(Double score, Integer count) {
		scoringCounts.put(score, count);
	}

	public Map<Double, Integer> getScoringCounts() {
		return scoringCounts;
	}

	public int getProteinCount() {
		return proteins.size();
	}

	public int getTotalSiteCount() {
		return totalSiteCount;
	}

	public double getMedian() {
		return median;
	}

	public double getMedianAbsDev() {
		return medianAbsDev;
	}

	public ArrayList<HistogramDataPoint> getHistogramDataPointList() {
		return histogramDatapointList;
	}

	public ArrayList<DatabaseSearchScanResultSite> getSites() {
		return sites;
	}

	public int getCombinedSiteCount() {
		return combinedSiteCount;
	}

}
