package edu.mit.scansite.server.updater;

import edu.mit.scansite.shared.ScansiteException;

/**
 * @author Tobieh
 */
public class DbUpdaterException extends ScansiteException {
  private static final long serialVersionUID = -7722899708317086941L;
  
  public DbUpdaterException() {
    super();
  }
  public DbUpdaterException(String what, Exception e) {
    super(what, e);
  }
  public DbUpdaterException(Exception e) {
    super(e);
  }
}
