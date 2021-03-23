package edu.mit.scansite.shared.dispatch.motif;

import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupAddAction implements
    Action<LightWeightMotifGroupRetrieverResult> {
  private LightWeightMotifGroup motifGroup;

  public MotifGroupAddAction() {
  }

  public MotifGroupAddAction(LightWeightMotifGroup motifGroup) {
    this.setMotifGroup(motifGroup);
  }

  public LightWeightMotifGroup getMotifGroup() {
    return motifGroup;
  }

  public void setMotifGroup(LightWeightMotifGroup motifGroup) {
    this.motifGroup = motifGroup;
  }
}
