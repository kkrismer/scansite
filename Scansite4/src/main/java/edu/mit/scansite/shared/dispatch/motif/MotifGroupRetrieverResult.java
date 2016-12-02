package edu.mit.scansite.shared.dispatch.motif;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;
import edu.mit.scansite.shared.transferobjects.MotifGroup;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupRetrieverResult implements Result {
  private List<MotifGroup> motifGroups;

  public MotifGroupRetrieverResult() {

  }

  public MotifGroupRetrieverResult(List<MotifGroup> motifGroups) {
    this.setMotifGroups(motifGroups);
  }

  public List<MotifGroup> getMotifGroups() {
    return motifGroups;
  }

  public void setMotifGroups(List<MotifGroup> motifGroups) {
    this.motifGroups = motifGroups;
  }
}
