package edu.mit.scansite.shared.dispatch.features;

import java.util.ArrayList;

import edu.mit.scansite.shared.transferobjects.DomainPosition;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DomainPlotGetResult implements Result {
  
  private boolean isSuccess = true;
  private String errorMessage = "";
  
  private String domainPlotUrl;
  private ArrayList<DomainPosition> domainPositions;

  public DomainPlotGetResult() {
  }
  
  public DomainPlotGetResult(String errorMessage) {
    isSuccess = false;
    this.errorMessage = errorMessage;
  }
  
  public DomainPlotGetResult(String domainPlotUrl, ArrayList<DomainPosition> domainPositions) {
    this.domainPlotUrl = domainPlotUrl;
    this.domainPositions = domainPositions;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public String getDomainPlotUrl() {
    return domainPlotUrl;
  }

  public void setDomainPlotUrl(String domainPlotUrl) {
    this.domainPlotUrl = domainPlotUrl;
  }

  public ArrayList<DomainPosition> getDomainPositions() {
    return domainPositions;
  }

  public void setDomainPositions(ArrayList<DomainPosition> domainPositions) {
    this.domainPositions = domainPositions;
  }
  
}
