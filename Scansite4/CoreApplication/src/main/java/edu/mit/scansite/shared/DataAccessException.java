package edu.mit.scansite.shared;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DataAccessException extends DatabaseException {
  
  private static final long serialVersionUID = 8264729178912112083L;
  
  public DataAccessException(String what) {
    super(what);
  }
  
  public DataAccessException() {
    super();
  }
  
  public DataAccessException(String what, Exception e) {
    super(what, e);
  }
}
