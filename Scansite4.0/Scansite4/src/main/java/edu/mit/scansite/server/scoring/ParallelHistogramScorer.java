package edu.mit.scansite.server.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.scansite.shared.transferobjects.HistogramDataPoint;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.util.ScansiteAlgorithms;

/**
 * @author Tobieh
 */
public class ParallelHistogramScorer extends
		ParallelScorer<HistogramScoringJob> {

	private Motif motif;
	private List<Protein> proteins;

	private Map<Double, Integer> scoringCounts = new HashMap<Double, Integer>();
	private int proteinCount = 0;
	private int siteCount = 0;
	private double median = 0;
	private double medianAbsDev = 0;
	private List<HistogramDataPoint> histogramDatapointList;

	public ParallelHistogramScorer(Motif motif, List<Protein> proteins) {
		super();
		this.motif = motif;
		this.proteins = proteins;
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
		return proteinCount;
	}

	public int getSiteCount() {
		return siteCount;
	}

	public double getMedian() {
		return median;
	}

	public double getMedianAbsDev() {
		return medianAbsDev;
	}

	public List<HistogramDataPoint> getHistogramDataPointList() {
		return histogramDatapointList;
	}

	@Override
	protected void doAfterScoring() {
		calculateMedianAndMAD();
		saveHistogramDatapoints();
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

			HistogramScoringJob job = new HistogramScoringJob(motif, partition);
			addScoringJob(job);
		}
	}

	@Override
	protected void doSaveJobResults(HistogramScoringJob job) {
			HistogramScoringJob j = job;
			saveScoreCounts(j.getScore2Counts());
			proteinCount += j.getProteinCount();
			siteCount += j.getSiteCount();
	}
}
