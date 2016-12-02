package edu.mit.scansite.server.updater;

import edu.mit.scansite.shared.ScansiteException;

/**
 * @author Tobieh
 */
public class ScansiteUpdaterException extends ScansiteException {
  private static final long serialVersionUID = -4551228243081867677L;

  public ScansiteUpdaterException() {
    super();
  }
  
  public ScansiteUpdaterException(String message, Exception e) {
    super(message, e);
  }
  
  public ScansiteUpdaterException(Exception e) {
    super(e);
  }
  
  public ScansiteUpdaterException(String message) {
    super(message);
  }
}
