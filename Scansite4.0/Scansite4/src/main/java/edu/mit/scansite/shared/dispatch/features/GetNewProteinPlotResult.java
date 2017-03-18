package edu.mit.scansite.shared.dispatch.features;

import java.util.ArrayList;

import edu.mit.scansite.shared.transferobjects.DomainPosition;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class GetNewProteinPlotResult implements Result {

  private boolean isSuccess = true;
  private String errorMessage = "";
  
  private String proteinPlotUrl;
  private ArrayList<DomainPosition> domainPositions;

  public GetNewProteinPlotResult() {
  }

  public GetNewProteinPlotResult(String proteinPlotUrl, ArrayList<DomainPosition> domainPositions) {
    this.proteinPlotUrl = proteinPlotUrl;
    this.domainPositions = domainPositions;
  }

  public GetNewProteinPlotResult(String errorMessage) {
    this.isSuccess = false;
    this.errorMessage = errorMessage;
  }

  public String getProteinPlotUrl() {
    return proteinPlotUrl;
  }

  public void setProteinPlotUrl(String proteinPlotUrl) {
    this.proteinPlotUrl = proteinPlotUrl;
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
