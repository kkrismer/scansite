package edu.mit.scansite.shared.dispatch.motif;

import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifNumbersRetrieverAction implements Action<MotifNumbersRetrieverResult> {
  
  private String userSessionId = "";
  
  public MotifNumbersRetrieverAction() {
  }

  public MotifNumbersRetrieverAction(String userSessionId) {
    this.userSessionId = userSessionId;
  }
  
  public void setUserSessionId(String userSessionId) {
    this.userSessionId = userSessionId;
  }

  public String getUserSessionId() {
    return userSessionId;
  }
}
