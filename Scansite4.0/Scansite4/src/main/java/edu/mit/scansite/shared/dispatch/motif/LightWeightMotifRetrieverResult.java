package edu.mit.scansite.shared.dispatch.motif;

import java.util.List;

import edu.mit.scansite.shared.transferobjects.LightWeightMotif;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class LightWeightMotifRetrieverResult implements Result {

  private List<LightWeightMotif> motifs;

  public LightWeightMotifRetrieverResult() {

  }

  public LightWeightMotifRetrieverResult(List<LightWeightMotif> motifs) {
    this.setMotifs(motifs);
  }

  public List<LightWeightMotif> getMotifs() {
    return motifs;
  }

  public void setMotifs(List<LightWeightMotif> motifs) {
    this.motifs = motifs;
  }
}
