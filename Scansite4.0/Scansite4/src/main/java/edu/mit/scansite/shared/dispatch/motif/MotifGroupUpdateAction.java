package edu.mit.scansite.shared.dispatch.motif;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupUpdateAction implements
    Action<LightWeightMotifGroupRetrieverResult> {
  private LightWeightMotifGroup motifGroup;

  public MotifGroupUpdateAction() {
  }

  public MotifGroupUpdateAction(LightWeightMotifGroup motifGroup) {
    this.setMotifGroup(motifGroup);
  }

  public LightWeightMotifGroup getMotifGroup() {
    return motifGroup;
  }

  public void setMotifGroup(LightWeightMotifGroup motifGroup) {
    this.motifGroup = motifGroup;
  }
}
