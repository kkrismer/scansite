package edu.mit.scansite.shared;

import java.io.Serializable;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ScansiteException extends Exception implements Serializable {
  private static final long serialVersionUID = -3728916610636250891L;
  
  public ScansiteException(String what) {
    super(what);
  }
  
  public ScansiteException() {
    super();
  }
  
  public ScansiteException(String what, Exception e) {
    super(what, e);
  }
  
  public ScansiteException(Exception ex) {
    super(ex);
  }
}
