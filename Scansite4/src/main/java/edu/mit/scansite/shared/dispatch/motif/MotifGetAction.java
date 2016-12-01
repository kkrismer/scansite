package edu.mit.scansite.shared.dispatch.motif;

import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGetAction implements Action<MotifGetResult> {

  private String motifShortName;

  public MotifGetAction() {
  }

  public MotifGetAction(String motifShortName) {
    this.motifShortName = motifShortName;
  }

  public String getMotifShortName() {
    return motifShortName;
  }

  public void setMotifShortName(String motifShortName) {
    this.motifShortName = motifShortName;
  }
}
