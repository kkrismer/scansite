package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Tobieh
 */
public enum GapPenalty implements IsSerializable {
  NONE("None", 0),
  HIGH("High", 0.1),
  MEDIUM("Medium", 0.01),
  LOW("Low", 0.001);
  
  String representation;
  double value;
  
  private GapPenalty(String representation, double value) {
    this.representation = representation;
    this.value = value;
  }
  
  public String getRepresentation() {
    return representation;
  }
  
  public double getValue() {
    return value;
  }
  
  @Override
  public String toString() {
    return representation;
  }
}
