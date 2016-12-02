package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.mit.scansite.shared.ScansiteConstants;

/**
 * The three main/default histogram thresholds.
 * @author tobieh
 */
public enum HistogramStringency implements IsSerializable {
  STRINGENCY_HIGH ("High", ScansiteConstants.STRINGENCY_HIGH),
  STRINGENCY_MEDIUM ("Medium", ScansiteConstants.STRINGENCY_MEDIUM),
  STRINGENCY_LOW ("Low", ScansiteConstants.STRINGENCY_LOW),
  STRINGENCY_MIN ("Minimum", ScansiteConstants.STRINGENCY_MIN);
  
  private String name;
  private double percentileValue;
  
  private HistogramStringency(String name, double value) {
    this.name = name;
    this.percentileValue = value;
  }
  
  public double getPercentileValue() {
    return percentileValue;
  }
  
  public String getName() {
    return name;
  }
};
