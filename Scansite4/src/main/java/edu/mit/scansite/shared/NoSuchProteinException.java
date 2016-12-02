package edu.mit.scansite.shared;

/**
 * @author Tobieh
 */
public class NoSuchProteinException extends ScansiteException {
  private static final long serialVersionUID = 4954861724334960909L;

  public NoSuchProteinException() {
  }
  
  public NoSuchProteinException(String what) {
    super(what);
  }
  
  public NoSuchProteinException(String what, Exception e) {
    super(what, e);
  }
  
  public NoSuchProteinException(Exception ex) {
    super(ex);
  }
  
  
}
