package edu.mit.scansite.server.scoring;

import java.util.Map;

import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.util.ScansiteScoring;

/**
 * @author Tobieh
 */
public class HistogramScoringJob extends ScoringJob {

  protected ScansiteScoring scoring = new ScansiteScoring();
  protected Motif motif;
  protected Protein[] proteins;
  protected Map<Double, Integer> score2Counts;

  public HistogramScoringJob(Motif motif, Protein[] proteins) {
    super();
    this.motif = motif;
    this.proteins = proteins;
  }

  public Map<Double, Integer> getScore2Counts() {
    return score2Counts;
  }

  public int getProteinCount() {
    return scoring.getProteinCount();
  }
  
  public int getSiteCount() {
    return scoring.getSiteCount();
  }

  @Override
  protected void doRun() {
    score2Counts = scoring.scoreAllProteinsHist(motif, proteins);
  }

}
