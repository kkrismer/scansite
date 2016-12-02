package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Konstantin Krismer
 */
public class OrthologyCheckOracleResult implements Result {
  private int numberOfOrthologs;

  public OrthologyCheckOracleResult() {

  }

  public OrthologyCheckOracleResult(int numberOfOrthologs) {
    this.numberOfOrthologs = numberOfOrthologs;
  }

  public int getNumberOfOrthologs() {
    return numberOfOrthologs;
  }

  public void setNumberOfOrthologs(int numberOfOrthologs) {
    this.numberOfOrthologs = numberOfOrthologs;
  }
}
