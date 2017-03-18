package edu.mit.scansite.server.dataaccess.ssh;

/**
 * @author Tobieh
 */
public class SshConnectorException extends Exception {
  private static final long serialVersionUID = -271598669602371726L;

  public SshConnectorException() {
  }

  public SshConnectorException(String what, Exception e) {
    super(what, e);
  }

  public SshConnectorException(Exception e) {
    super(e);
  }
  
}
