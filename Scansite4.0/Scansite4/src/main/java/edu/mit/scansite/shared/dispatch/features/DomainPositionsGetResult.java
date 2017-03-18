package edu.mit.scansite.shared.dispatch.features;

import java.util.ArrayList;

import edu.mit.scansite.shared.transferobjects.DomainPosition;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DomainPositionsGetResult implements Result {

  private boolean isSuccess = true;
  private String errorMessage = "";
  
  private ArrayList<DomainPosition> domainPositions;

  public DomainPositionsGetResult() {
  }

  public DomainPositionsGetResult(ArrayList<DomainPosition> domainPositions) {
    this.domainPositions = domainPositions;
  }

  public DomainPositionsGetResult(String errorMessage) {
    this.isSuccess = false;
    this.errorMessage = errorMessage;
  }

  public ArrayList<DomainPosition> getDomainPositions() {
    return domainPositions;
  }

  public void setDomainPositions(ArrayList<DomainPosition> domainPositions) {
    this.domainPositions = domainPositions;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
