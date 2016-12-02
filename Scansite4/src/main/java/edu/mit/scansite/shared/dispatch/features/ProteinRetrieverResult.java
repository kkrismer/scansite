package edu.mit.scansite.shared.dispatch.features;

import edu.mit.scansite.shared.transferobjects.Protein;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinRetrieverResult implements Result {
  private boolean isSuccess = true;
  private String errorMessage = "";
  
  private Protein protein;
  
  public ProteinRetrieverResult() {
  }
  
  public ProteinRetrieverResult(String errorMessage) {
    isSuccess = false;
    this.errorMessage = errorMessage;
  }
  
  public ProteinRetrieverResult(Protein protein) {
    this.protein = protein;
  }
  
  public Protein getProtein() {
    return protein;
  }
  
  public void setProtein(Protein protein) {
    this.protein = protein;
  }
  
  public boolean isSuccess() {
    return isSuccess;
  }
  
  public String getErrorMessage() {
    return errorMessage;
  }
  
  public void setSuccess(boolean isSuccess) {
    this.isSuccess = isSuccess;
  }
  
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
