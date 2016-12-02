package edu.mit.scansite.shared;

/**
 * @author Tobieh
 */
public class NoPfamException extends ScansiteException {

  private static final long serialVersionUID = -7148896653836606309L;

  public NoPfamException() {
  }
  
  public NoPfamException(String what) {
    super(what);
  }
  
  public NoPfamException(String what, Exception e) {
    super(what, e);
  }
  
  public NoPfamException(Exception ex) {
    super(ex);
  }
  
}
