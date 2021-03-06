package edu.mit.scansite.shared.dispatch.motif;

import java.util.List;

import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class LightWeightMotifGroupRetrieverResult implements Result {
  private List<LightWeightMotifGroup> motifGroups;

  public LightWeightMotifGroupRetrieverResult() {
  }

  public LightWeightMotifGroupRetrieverResult(
      List<LightWeightMotifGroup> motifGroups) {
    this.setMotifGroups(motifGroups);
  }

  public List<LightWeightMotifGroup> getMotifGroups() {
    return motifGroups;
  }

  public void setMotifGroups(List<LightWeightMotifGroup> motifGroups) {
    this.motifGroups = motifGroups;
  }
}
