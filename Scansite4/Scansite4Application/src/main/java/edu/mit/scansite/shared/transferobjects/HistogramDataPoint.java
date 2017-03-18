package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A datapoint in a histogram. 
 * @author tobieh
 */
public class HistogramDataPoint implements Comparable<HistogramDataPoint>, IsSerializable {
  
  private double score = 0;
  private double absFreq = 0;
  
  public HistogramDataPoint () {
  }
  
  public HistogramDataPoint(double score, double absFreq) {
    super();
    this.score = score;
    this.absFreq = absFreq;
  }

  public double getScore() {
    return score;
  }

  public double getAbsFreq() {
    return absFreq;
  }
  
  public void setAbsFreq(double absFreq) {
    this.absFreq = absFreq;
  }
  
  public void setScore(double score) {
    this.score = score;
  }
  
  @Override
  public String toString() {
    return score + "->" + absFreq;
  }

  @Override
  public int compareTo(HistogramDataPoint o) {
    if (o == null) {
      return 1;
    }
    if (Double.compare(score, o.getScore()) == 0) {
      return 0;
    } else {
      return (score < o.getScore()) ? -1 : 1;
    }
  }
  
}
