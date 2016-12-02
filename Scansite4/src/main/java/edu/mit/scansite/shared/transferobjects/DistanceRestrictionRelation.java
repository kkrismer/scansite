package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public enum DistanceRestrictionRelation implements IsSerializable {
  AT_LEAST("at least (>=)"),
  AT_MOST("at most (<=)");
  
  String representation;
  
  private DistanceRestrictionRelation(String representation) {
    this.representation = representation;
  }
  
  public String getRepresentation() {
    return representation;
  }
  
  @Override
  public String toString() {
    return representation;
  }

  /**
   * compares like this: </br>
   * AT_LEAST: left >= right </br> 
   * AT_MOST: left <= right </br>
   * @param left 
   * @param right
   * @return TRUE, if condition is fulfilled, FALSE otherwise.
   */
  public boolean compareTo(int left, int right) {
    return (this.equals(AT_LEAST) ? left >= right : left <= right);
  }
}
