package edu.mit.scansite.shared.dispatch;

import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 */
public class BooleanResult implements Result {
  private boolean successful = false;
  
  public BooleanResult() {
  }
  
  public BooleanResult(boolean successful) {
    this.successful = successful;
  }
  
  public boolean isSuccessful() {
    return successful;
  }

}
