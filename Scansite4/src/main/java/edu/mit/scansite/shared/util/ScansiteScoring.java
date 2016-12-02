package edu.mit.scansite.shared.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.AminoAcid;
import edu.mit.scansite.shared.transferobjects.DatabaseSearchMultipleRestriction;
import edu.mit.scansite.shared.transferobjects.DatabaseSearchScanResultSite;
import edu.mit.scansite.shared.transferobjects.DistanceRestrictionRelation;
import edu.mit.scansite.shared.transferobjects.HistogramDataPoint;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;

/**
 * @author Tobieh
 */
public class ScansiteScoring {
	private int proteinCount = 0;
	private int siteCount = 0;

	/**
	 * Maps a given score to a histogram-Score.
	 * 
	 * @param score
	 *            A sequence's actual score.
	 * @param totalNumber
	 *            The total number of entries displayed in the histogram
	 *            (integral of the histogram). If the number <0, a
	 *            data-independent default value is used.
	 * @return The histogram-score.
	 */
	public double getHistogramScore(double score, int totalNumber) {
		double SWS = 0; // ScoringWindowSize
		if (totalNumber < 1000) {
			SWS = ScansiteConstants.HISTOGRAM_SCORE_WINDOW_SIZE;
		} else {
			SWS = ScansiteConstants.HIST_SCORE_MAX / (Math.sqrt(totalNumber)); // for
																				// less
																				// bins:
																				// divide
																				// by
																				// ((2
																				// *
																				// ScansiteAlgorithms.log2(totalNumber)
																				// +
																				// 1);
		}
		return SWS * ((int) (score / SWS)); // return left side (lower boundary)
											// of bin
	}

	/**
	 * Creates a list of HistogramDataPoints.
	 * 
	 * @return A list of HistogramDataPoints, derived from a map as it is
	 *         returned by scoreAllProteins.
	 */
	public ArrayList<HistogramDataPoint> getHistogramDataPointList(
			Map<Double, Integer> scoreToCount) {
		ArrayList<Double> scores = new ArrayList<Double>(scoreToCount.keySet());
		Collections.sort(scores);
		ArrayList<HistogramDataPoint> dps = new ArrayList<HistogramDataPoint>();
		for (Double score : scores) {
			dps.add(new HistogramDataPoint(score, scoreToCount.get(score)));
		}
		return dps;
	}

	/**
	 * Calculates a score for a given position in a sequence.
	 * 
	 * @param position
	 *            The position within the sequence.
	 * @param sequence
	 *            The sequence as character array.
	 * @param motif
	 *            The motif that is used for calculating the score.
	 * @return The sequence's position's score.
	 */
	public double calculateSiteScore(int position, char[] sequence, Motif motif) {
		double log2Sum = 0;
		int zeroCount = 0;
		int degeneratePositions = motif.getDegeneratePositionsCount();
		char paddedSeq[] = getSiteWindow(sequence, position);

		for (int windowPosition = 0; windowPosition < ScansiteConstants.WINDOW_SIZE; ++windowPosition) {
			// Find this residue's element number in the matrix array
			char currentAa = paddedSeq[windowPosition];
			// position out of the sequence bounds ?
			if (!AminoAcid.isAa(currentAa)) {
				++zeroCount;
			} else {
				double score = 0.0;
				double currentMatrixValue = motif.getValue(currentAa,
						windowPosition);
				if (currentMatrixValue != ScansiteConstants.FIXED_SITE_SCORE) { // Don't
																				// score
																				// the
																				// fixed
																				// residue(s)
					if (currentMatrixValue == 0) {
						// typically, "0" values in matrices occur at the
						// terminals (in rows that are not all-zero).
						// this indicates that this residue is invalid in this
						// position.
						return -1; // negative scores mean "invalid"
					} else { // Score this position
						score = ScansiteAlgorithms.log2(currentMatrixValue);
					}
				}
				log2Sum += score;
			}
		}

		// Average the nonzero scores
		double rawScore = log2Sum
				/ ((double) (degeneratePositions - zeroCount));

		// normalize by the optimal score for this motif. 0 = best.
		double optScore = motif.getOptimalScore();
		double finalScore = (optScore - rawScore) / optScore;

		return finalScore;
	}

	public char[] getSiteWindow(char[] seq, int position) {
		int wholeSeqLen = seq.length;
		int halfWindow = ScansiteConstants.HALF_WINDOW;
		int startIdx = Math.max(0, position - halfWindow);
		int endIdx = Math.min(seq.length - 1, position + halfWindow);
		char[] window = new char[ScansiteConstants.WINDOW_SIZE];
		System.arraycopy(seq, startIdx, window,
				Math.abs(position - startIdx - halfWindow), endIdx - startIdx
						+ 1);
		if (position - halfWindow < 0) {
			int nrPadChars = Math.abs(position - halfWindow + 1); // +1 for the
																	// start-char
			int i = 0;
			for (; i < nrPadChars; ++i) {
				window[i] = '@';
			}
			window[i] = AminoAcid._N.getOneLetterCode();
		}
		if (position + halfWindow >= wholeSeqLen) {
			int i = wholeSeqLen - position + halfWindow;
			window[i++] = AminoAcid._C.getOneLetterCode();
			for (; i < ScansiteConstants.WINDOW_SIZE; ++i) {
				window[i] = '@';
			}

		}
		return window;
	}

	/**
	 * Calculates the given motifs optimal score and sets this score and the
	 * number of degenerate positions.
	 * 
	 * @param m
	 *            The motif that's optimal score is calculated and set.
	 */
	public void calculateOptimalScore(Motif m) {
		final int WINDOW_SIZE = ScansiteConstants.WINDOW_SIZE;
		AminoAcid[] aas = m.getAminoAcidArray();
		double rowSum[] = new double[WINDOW_SIZE];
		double rowMax[] = new double[WINDOW_SIZE];
		double maxLog2s[] = new double[WINDOW_SIZE];
		int maxIndices[] = new int[WINDOW_SIZE];

		// init arrays
		for (int i = 0; i < WINDOW_SIZE; ++i) {
			rowSum[i] = 0;
			rowMax[i] = 0;
			maxLog2s[i] = 0;
			maxIndices[i] = -1;
		}

		int degenerateCount = WINDOW_SIZE;
		int zeroCount = 0;
		double log2Sum = 0;
		int firstFixedIndex = -1;
		int lastFixedIndex = -1;

		// find maximums in rows, calculate row sums, log2 sums per row and
		for (int row = 0; row < WINDOW_SIZE; ++row) {
			for (int aaIndex = 0; aaIndex < aas.length; ++aaIndex) {
				AminoAcid aa = aas[aaIndex];
				if (!AminoAcid.isIgnoredForScoring(aa)) {
					double currentValue = m.getValue(aa, row);
					if (!AminoAcid.isTerminal(aa)) {
						rowSum[row] += currentValue;
						if (currentValue > rowMax[row]) {
							rowMax[row] = currentValue;
							maxIndices[row] = row;
						}
					}
				}
			}

			// update degenerateCount, zeroCount and the log2s
			if (isFixedSite(rowMax[row])) {
				--degenerateCount;
			} else if (rowMax[row] == 0) {
				++zeroCount;
			} else {
				maxLog2s[row] = ScansiteAlgorithms.log2(rowMax[row]);
				log2Sum += maxLog2s[row];
			}

			// record the indices of the first and last fixed site
			if (isFixedSite(rowMax[row])
					|| isFixedSite(m.getValue(AminoAcid._C, row))
					|| isFixedSite(m.getValue(AminoAcid._N, row))) {
				if (firstFixedIndex == -1) {
					firstFixedIndex = row;
				}
				lastFixedIndex = row;
			}
		}

		double optScore = log2Sum / ((double) (degenerateCount - zeroCount));
		double deg = 0;

		// Calculate optimal scores for sites that contain terminals considering
		// only sites that
		// contain all the fixed residues. The loop also calculates the full
		// window score.
		// Compare score to the current optimal score (for a full window) and
		// maintain the highest score.
		for (int i = -1; i < firstFixedIndex; ++i) {// N-terminal must be before
													// the first fixed residue
			for (int j = lastFixedIndex + 1; j <= WINDOW_SIZE; ++j) {// C-terminal
																		// must
																		// be
																		// after
																		// the
																		// last
																		// fixed
																		// residue
				// calculate the optimal score of a site with N-terminal at i
				// and C-terminal at j
				deg = j - i - 1; // the number of presumably degenerate
									// positions in the current window.
				zeroCount = 0; // positions of all-zero rows
				log2Sum = 0.0;

				if (i > -1) { // window starts with a N-terminal
					log2Sum += ScansiteAlgorithms.log2(m.getValue(AminoAcid._N,
							i));
					++deg; // the terminal score is included in the average
				}

				// add max values within the current window to logsum
				for (int k = i + 1; k <= j - 1; ++k) {
					if (isFixedSite(rowMax[k])) { // a fixed residue
						--deg; // decrement number of degenerate positions
					} else if (rowMax[k] == 0) { // all row is zeroes: do not
													// include it in the
													// average.
						++zeroCount;
					} else { // add position to logsum
						log2Sum += ScansiteAlgorithms.log2(rowMax[k]);
					}
				}

				if (j < WINDOW_SIZE) { // window ends with a C-terminal
					log2Sum += ScansiteAlgorithms.log2(m.getValue(AminoAcid._C,
							j));
					++deg; // the terminal score is included in the average
				}

				// calculate score and keep the highest score
				double score = log2Sum / (deg - (double)zeroCount); // optimal
																		// score
																		// for
																		// the
																		// current
																		// window
				if (score > optScore) {
					optScore = score;
				}
			}
		}
		m.setOptimalScore(optScore);
		m.setDegeneratePositionsCount(degenerateCount);
	}

	/**
	 * Checks whether a site is a fixed site (FIXED_SITE_SCORE) or not.
	 * 
	 * @param d
	 *            The motif site's value.
	 * @return TRUE if the site is a fixed site, otherwise FALSE.
	 */
	public boolean isFixedSite(double d) {
		return d == ScansiteConstants.FIXED_SITE_SCORE;
	}

	/**
	 * Resets protein and site count to 0.
	 */
	public void resetCounts() {
		proteinCount = 0;
		siteCount = 0;
	}

	/**
	 * Scores a list of proteins.
	 * 
	 * @param motif
	 *            A motif.
	 * @param proteins
	 *            A list of proteins protein.
	 * @param maxScore
	 *            A maximum score.
	 * @return Returns a list of hits.
	 */
	public ArrayList<ScanResultSite> scoreProteins(Motif motif,
			ArrayList<Protein> proteins, double maxScore) {
		ArrayList<ScanResultSite> hits = new ArrayList<ScanResultSite>();
		for (Protein p : proteins) {
			hits.addAll(scoreProtein(motif, p, maxScore));
		}
		return hits;
	}

	/**
	 * Scores a list of proteins.
	 * 
	 * @param motif
	 *            A motif.
	 * @param proteins
	 *            A list of proteins protein.
	 * @param maxScore
	 *            A maximum score.
	 * @return Returns a list of hits.
	 */
	public ArrayList<DatabaseSearchScanResultSite> scoreProteinsDbSearch(
			Motif motif, Protein[] proteins, double maxScore) {
		ArrayList<DatabaseSearchScanResultSite> hits = new ArrayList<DatabaseSearchScanResultSite>();
		for (Protein p : proteins) {
			for (ScanResultSite s : scoreProtein(motif, p, maxScore)) {
				hits.add(new DatabaseSearchScanResultSite(s, p));
			}
		}
		return hits;
	}

	/**
	 * Scores a list of proteins.
	 * 
	 * @param motif
	 *            A motif.
	 * @param proteins
	 *            A list of proteins protein.
	 * @param motifThresholds
	 *            A maximum score.
	 * @return Returns a list of hits.
	 */
	public List<DatabaseSearchScanResultSite> scoreProteinsDbSearchMultiMotif(
			List<Motif> motifs, Protein[] proteins,
			List<Double> motifThresholds,
			DatabaseSearchMultipleRestriction restrictions) {
		List<DatabaseSearchScanResultSite> resultSites = new ArrayList<DatabaseSearchScanResultSite>();

		// variables for distance restriction search, only initialized if needed
		ArrayList<Motif> motifsLeft = null;
		ArrayList<Motif> motifsRight = null;
		ArrayList<Integer> distanceRestrictions = null;
		ArrayList<DistanceRestrictionRelation> relations = null;
		if (restrictions != null && !restrictions.isGapRestrictionSearch()) {
			motifsLeft = new ArrayList<Motif>();
			motifsRight = new ArrayList<Motif>();
			distanceRestrictions = new ArrayList<Integer>();
			relations = new ArrayList<DistanceRestrictionRelation>();
			for (int i = 0; i < restrictions.getIdxsMotifLeft().size(); ++i) {
				motifsLeft.add(restrictions.getMotif(restrictions
						.getIdxsMotifLeft().get(i), restrictions
						.getIdxsMotifLeftIsDbMotif().get(i)));
				motifsRight.add(restrictions.getMotif(restrictions
						.getIdxsMotifRight().get(i), restrictions
						.getIdxsMotifRightIsDbMotif().get(i)));
				distanceRestrictions.add(restrictions.getResidueRestrictions()
						.get(i));
				relations.add(restrictions.getRestrictionRelations().get(i));
			}
		}

		// do scoring
		for (Protein p : proteins) {
			ArrayList<ScanResultSite> hits = new ArrayList<ScanResultSite>();
			boolean scoreNextProtein = false;
			for (int i = 0; !scoreNextProtein && i < motifs.size(); ++i) {
				Motif motif = motifs.get(i);
				ArrayList<ScanResultSite> motifHits = scoreProtein(motif, p,
						motifThresholds.get(i));
				if (restrictions != null
						&& restrictions.isGapRestrictionSearch()
						&& motifHits.size() > 0) { // keep only best site for
													// each motif
					ScanResultSite best = motifHits.get(0);
					for (ScanResultSite site : motifHits) {
						if (site.getScore() < best.getScore()) {
							best = site;
						}
					}
					motifHits.clear();
					motifHits.add(best);
				}
				if (restrictions != null && motifHits.isEmpty()) {
					scoreNextProtein = true;
				} else {
					hits.addAll(motifHits);
				}
			}
			DatabaseSearchScanResultSite combinedSite = null;
			if (!scoreNextProtein && !hits.isEmpty()) {
				if (restrictions != null
						&& restrictions.isGapRestrictionSearch()) { // GAP
																	// PENALTY
																	// SEARCH
					if (hits.size() == motifs.size()) { // only save combined
														// hits if all selected
														// motifs score in a
														// protein!
						combinedSite = new DatabaseSearchScanResultSite(hits,
								motifs.size(), p, restrictions.getGapPenalty());
						if (combinedSite != null
								&& combinedSite.getCombinedScore() >= 0) {
							resultSites.add(combinedSite);
						}
					}
				} else { // DISTANCE RESTRICTION SEARCH
					// create and filter all combinations of sites
					List<DatabaseSearchScanResultSite> combinedSites = getCombinedSearchSites(
							hits, motifs, p, motifsLeft, motifsRight,
							distanceRestrictions, relations);
					if (combinedSites != null) {
						resultSites.addAll(combinedSites);
					}
				}
			}
		}
		return resultSites;
	}

	private List<DatabaseSearchScanResultSite> getCombinedSearchSites(
			ArrayList<ScanResultSite> hits, List<Motif> motifs,
			Protein protein, ArrayList<Motif> motifsLeft,
			ArrayList<Motif> motifsRight,
			ArrayList<Integer> distanceRestrictions,
			ArrayList<DistanceRestrictionRelation> relations) {
		// 1. map sites to motifs
		HashMap<Motif, ArrayList<ScanResultSite>> m2s = new HashMap<Motif, ArrayList<ScanResultSite>>();
		for (ScanResultSite srs : hits) {
			if (m2s.containsKey(srs.getMotif())) {
				m2s.get(srs.getMotif()).add(srs);
			} else {
				ArrayList<ScanResultSite> tempList = new ArrayList<ScanResultSite>();
				tempList.add(srs);
				m2s.put(srs.getMotif(), tempList);
			}
		}

		// 2. create sites and filter them recursively (only if case every motif
		// has found sites!)
		ArrayList<DatabaseSearchScanResultSite> combinedSites = new ArrayList<DatabaseSearchScanResultSite>();
		if (m2s.keySet().size() == motifs.size()) {
			filterRecursively(motifs.size(), protein, motifsLeft, motifsRight,
					distanceRestrictions, relations, m2s, motifs, 0,
					new ArrayList<ScanResultSite>(), combinedSites);
		}

		return combinedSites;
	}

	private void filterRecursively(final int nMotifs, final Protein protein,
			final ArrayList<Motif> motifsLeft,
			final ArrayList<Motif> motifsRight,
			final ArrayList<Integer> distanceRestrictions,
			final ArrayList<DistanceRestrictionRelation> relations,
			final HashMap<Motif, ArrayList<ScanResultSite>> m2s,
			final List<Motif> motifs, final int i,
			ArrayList<ScanResultSite> temp,
			ArrayList<DatabaseSearchScanResultSite> combinedSites) {
		if (i < motifs.size()) {
			for (ScanResultSite site : m2s.get(motifs.get(i))) {
				temp.add(site);
				boolean isCombinedSite = isCombinedSite(motifsLeft,
						motifsRight, distanceRestrictions, relations, temp,
						temp.size() == nMotifs);
				if (isCombinedSite) {
					if (temp.size() < nMotifs) { // call method recursively for
													// i+1
						filterRecursively(nMotifs, protein, motifsLeft,
								motifsRight, distanceRestrictions, relations,
								m2s, motifs, i + 1,
								new ArrayList<ScanResultSite>(temp),
								combinedSites);
					} else if (temp.size() == nMotifs) { // add combinedsite
						combinedSites.add(new DatabaseSearchScanResultSite(
								new ArrayList<ScanResultSite>(temp), nMotifs,
								protein));
						temp.clear();
					}
				}
				if (!temp.isEmpty()) {
					temp.remove(site);
				}
			}
		}
	}

	private boolean isCombinedSite(ArrayList<Motif> motifsLeft,
			ArrayList<Motif> motifsRight,
			ArrayList<Integer> distanceRestrictions,
			ArrayList<DistanceRestrictionRelation> relations,
			ArrayList<ScanResultSite> list, boolean isTotalList) {
		boolean doReturnTrue = false;
		for (ScanResultSite site : list) {
			if (motifsLeft.contains(site.getMotif())) {
				for (int i = 0; i < motifsLeft.size(); ++i) {
					if (motifsLeft.get(i).equals(site.getMotif())) {
						ScanResultSite otherSite = getSite(motifsRight.get(i),
								list);
						if (otherSite != null) {
							if (!relations.get(i).compareTo(
									Math.abs(site.getPosition()
											- otherSite.getPosition()),
									distanceRestrictions.get(i))) {
								return false;
							}
						} else if (!isTotalList) {
							doReturnTrue = true;
						} else {
							return false;
						}
					}
					if (motifsRight.get(i).equals(site.getMotif())) {
						ScanResultSite otherSite = getSite(motifsLeft.get(i),
								list);
						if (otherSite != null) {
							if (!relations.get(i).compareTo(
									Math.abs(site.getPosition()
											- otherSite.getPosition()),
									distanceRestrictions.get(i))) {
								return false;
							}
						} else if (!isTotalList) {
							doReturnTrue = true;
						} else {
							return false;
						}
					}
					if (doReturnTrue) {
						return true;
					}
				}
			}
		}
		return true;
	}

	private ScanResultSite getSite(Motif motif, ArrayList<ScanResultSite> list) {
		for (ScanResultSite site : list) {
			if (site.getMotif().equals(motif)) {
				return site;
			}
		}
		return null;
	}

	/**
	 * Scores the given protein using all the given motifs.
	 * 
	 * @param motifs
	 *            A set of motifs.
	 * @param p
	 *            A protein.
	 * @param maxScore
	 *            A maximum score. All results with a score >maxScore will be
	 *            discarded.
	 * @return Returns a list of hits.
	 */
	public ArrayList<ScanResultSite> scoreProtein(Set<Motif> motifs, Protein p,
			double maxScore) {
		ArrayList<ScanResultSite> hits = new ArrayList<ScanResultSite>();
		for (Motif m : motifs) {
			hits.addAll(scoreProtein(m, p, maxScore));
		}
		return hits;
	}

	/**
	 * Scores a single Protein.
	 * 
	 * @param motif
	 *            A motif.
	 * @param p
	 *            A protein.
	 * @param scoreThreshold
	 *            A maximum score.
	 * @return Returns a list of hits.
	 */
	public ArrayList<ScanResultSite> scoreProtein(Motif motif,
			LightWeightProtein p, double maxScore) {
		ArrayList<ScanResultSite> hits = new ArrayList<ScanResultSite>();
		ArrayList<Character> fixedCenters = motif.getFixedCentersAsCharacters();
		double score = 0;
		String seq = p.getSequence();
		if (isProteinScorable(seq)) {
			char[] sequence = seq.toCharArray();
			++proteinCount;
			for (int i = 0; i < sequence.length; ++i) {
				if (fixedCenters.contains(sequence[i])) {
					score = calculateSiteScore(i, sequence, motif);
					if (score >= 0.0 && score <= maxScore) {
						++siteCount;
						ScanResultSite site = new ScanResultSite(motif, p,
								null, null, i, roundScore(score));
						hits.add(site);
					}
				}
				score = -1;
			}
		}
		return hits;
	}

	/**
	 * Scores all the protein's fixed sites for creating a histogram.
	 * 
	 * @return A map that maps the calculated scores to the number of hits with
	 *         exactly this score.
	 */
	public Map<Double, Integer> scoreAllProteinsHist(Motif motif,
			Protein[] proteins) {
		Map<Double, Integer> scoreToCount = new HashMap<Double, Integer>();
		for (Protein p : proteins) {
			scoreToCount = scoreProteinHist(motif, p, scoreToCount);
		}
		return scoreToCount;
	}

	/**
	 * Scores a single Protein.
	 * 
	 * @param motif
	 *            A motif.
	 * @param scoreToCount
	 *            A map that maps scores to counts.
	 * @param fixedCenters
	 *            A list of a motifs fixedCenters, or NULL.
	 * @param p
	 *            A single protein with a sequence set.
	 * @return A map that maps the calculated scores to the number of hits with
	 *         exactly this score.
	 */
	private Map<Double, Integer> scoreProteinHist(Motif motif, Protein p,
			Map<Double, Integer> scoreToCount) {
		if (scoreToCount == null) {
			scoreToCount = new HashMap<Double, Integer>();
		}
		ArrayList<Character> fixedCenters = motif.getFixedCentersAsCharacters();
		double score = 0;
		String seq = p.getSequence();
		if (isProteinScorable(seq)) {
			char[] sequence = seq.toCharArray();
			++proteinCount;
			for (int i = 0; i < sequence.length; ++i) {
				if (fixedCenters.contains(sequence[i])) {
					score = calculateSiteScore(i, sequence, motif);
					if (score >= 0.0) {
						++siteCount;
						double roundedScore = roundScore(score);
						if (!scoreToCount.containsKey(roundedScore)) {
							scoreToCount.put(roundedScore, 1);
						} else {
							scoreToCount.put(roundedScore,
									scoreToCount.get(roundedScore) + 1);
						}
					}
				}
				score = -1;
			}
		}
		return scoreToCount;
	}

	/**
	 * @param sequence
	 *            A protein's sequence.
	 * @return TRUE, if the protein can be scored, FALSE elsewise.
	 */
	public boolean isProteinScorable(String sequence) {
		boolean scorable = false;
		if (sequence != null) { // && sequence.length() >=
								// ScansiteConstants.WINDOW_SIZE) { // TODO
								// score protein although it is shorter than
								// window??? for now, yes
			scorable = true;
		}
		return scorable;
	}

	/**
	 * @return The number of proteins scored since the last reset/instantiation.
	 */
	public int getProteinCount() {
		return proteinCount;
	}

	/**
	 * @return The number of sites scored since the last reset/instantiation.
	 */
	public int getSiteCount() {
		return siteCount;
	}

	/**
	 * round score.
	 * 
	 * @param d
	 *            the score
	 * @return the rounded score.
	 */
	public double roundScore(double d) {
		final double ROUND_HELP = 10000.0; // rounding to 4 decimals
		return (double) Math.round(d * ROUND_HELP) / ROUND_HELP;
	}

}
