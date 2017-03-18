package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.mit.scansite.shared.util.Formatter;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class LightWeightMotifGroup implements IsSerializable,
    Comparable<LightWeightMotifGroup> {

  private int id = -1;
  private String displayName = "";
  private String shortName = "";

  public LightWeightMotifGroup() {
    super();
  }

  public LightWeightMotifGroup(int id, String displayName, String shortName) {
    super();
    this.id = id;
    this.displayName = displayName;
    this.shortName = shortName;
  }

  public LightWeightMotifGroup(String displayName, String shortName) {
    super();
    this.displayName = displayName;
    this.shortName = shortName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    Formatter formatter = new Formatter();
    this.displayName = formatter.replaceMagicQuotes(displayName);
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    Formatter formatter = new Formatter();
    this.shortName = formatter.replaceMagicQuotes(shortName);
  }

  @Override
  public int compareTo(LightWeightMotifGroup o) {
    return (o != null) ? displayName.compareTo(o.getDisplayName()) : 1;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    LightWeightMotifGroup other = (LightWeightMotifGroup) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
