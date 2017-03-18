package edu.mit.scansite.server.dataaccess.file;

/**
 * @author Tobieh
 */
public class ResultFileWriterException extends Exception {
  private static final long serialVersionUID = 4507715701912467019L;

  public ResultFileWriterException() {
  }
  
  public ResultFileWriterException(Exception e) {
    super(e);
  }
  
  public ResultFileWriterException(String s, Exception e) {
    super(s, e);
  }
}
