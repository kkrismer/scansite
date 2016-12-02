package edu.mit.scansite.shared.dispatch.motif;

import edu.mit.scansite.shared.transferobjects.MotifClass;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifRetrieverAction implements Action<MotifRetrieverResult> {
  
  private String motifShortName;
  private boolean isUserLoggedIn = false;
  private MotifClass motifClass = MotifClass.MAMMALIAN;
  
  public MotifRetrieverAction() {
  }
  
  public MotifRetrieverAction(String motifShortName, MotifClass motifClass, boolean isUserLoggedIn) {
    this.motifShortName = motifShortName;
    this.isUserLoggedIn = isUserLoggedIn;
    this.motifClass = motifClass;
  }
  
  public String getMotifShortName() {
    return motifShortName;
  }
  
  public void setMotifShortName(String motifShortName) {
    this.motifShortName = motifShortName;
  }

  public boolean isUserLoggedIn() {
    return isUserLoggedIn;
  }

  public MotifClass getMotifClass() {
    return motifClass;
  }
  
  public void setMotifClass(MotifClass motifClass) {
    this.motifClass = motifClass;
  }
}
