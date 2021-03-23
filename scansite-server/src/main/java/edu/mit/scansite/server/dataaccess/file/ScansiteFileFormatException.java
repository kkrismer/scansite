package edu.mit.scansite.server.dataaccess.file;

import java.io.Serializable;

import edu.mit.scansite.shared.ScansiteException;

/**
 * @author Tobieh
 */
public class ScansiteFileFormatException extends ScansiteException implements Serializable {
  private static final long serialVersionUID = -3699235147887849315L;
    
  public ScansiteFileFormatException() {
  }
 
  public ScansiteFileFormatException(String what, Exception e) {
    super(what, e);
  }
 
  public ScansiteFileFormatException(String what) {
    super(what);
  }
 
  public ScansiteFileFormatException(Exception e) {
    super(e);
  }
  
}
