package edu.mit.scansite.server.domains;

/**
 * @author Tobieh
 */
public class DomainLocatorException extends Exception {
  private static final long serialVersionUID = -2156897658596283580L;

  public DomainLocatorException() {
  }

  public DomainLocatorException(String s, Exception e) {
    super(s, e);
  }

  public DomainLocatorException(Exception e) {
    super(e);
  }

  public DomainLocatorException(String s) {
    super(s);
  }
  
  
}
