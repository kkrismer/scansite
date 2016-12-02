package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Konstantin Krismer
 */
public class PatternPosition implements IsSerializable {
  private String aminoAcidCodes;
  private boolean expectedPhosphorylationSite = false;
  private boolean isPositionSpecific = true;

  public PatternPosition() {
    super();
  }

  public PatternPosition(String aminoAcidCodes,
      boolean expectedPhosphorylationSite) {
    super();
    this.aminoAcidCodes = aminoAcidCodes.toUpperCase();
    this.expectedPhosphorylationSite = expectedPhosphorylationSite;
  }

  public PatternPosition(String aminoAcidCodes,
      boolean expectedPhosphorylationSite, boolean isPositionSpecific) {
    super();
    this.aminoAcidCodes = aminoAcidCodes.toUpperCase();
    this.expectedPhosphorylationSite = expectedPhosphorylationSite;
    this.isPositionSpecific = isPositionSpecific;
  }

  public String getAminoAcidCodes() {
    return aminoAcidCodes;
  }

  public void setAminoAcidCodes(String aminoAcidCodes) {
    this.aminoAcidCodes = aminoAcidCodes.toUpperCase();
  }

  public boolean isExpectedPhosphorylationSite() {
    return expectedPhosphorylationSite;
  }

  public void setExpectedPhosphorylationSite(boolean expectedPhosphorylationSite) {
    this.expectedPhosphorylationSite = expectedPhosphorylationSite;
  }

  public boolean isPositionSpecific() {
    return isPositionSpecific;
  }

  public void setPositionSpecific(boolean isPositionSpecific) {
    this.isPositionSpecific = isPositionSpecific;
  }

  public String getPlain() {
    if(expectedPhosphorylationSite) {
      return aminoAcidCodes.toLowerCase();
    } else {
      return aminoAcidCodes.toUpperCase();
    }
  }

  public String getRegEx() {
    return toString();
  }

  public String getHtmlFormattedRegEx() {
    if (expectedPhosphorylationSite) {
      if(isPositionSpecific) {
        return "[<span class='regexWidgetPhosphorylationSiteColor'>"
            + aminoAcidCodes + "</span>]";
      } else {
        return "<span class='regexWidgetPhosphorylationSiteColor'>"
            + aminoAcidCodes + "</span>";
      }
    } else {
      return toString();
    }
  }

  @Override
  public String toString() {
    if(isPositionSpecific) {
      return "[" + aminoAcidCodes + "]";
    } else {
      return aminoAcidCodes; // already (complex) regular expression
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((aminoAcidCodes == null) ? 0 : aminoAcidCodes.hashCode());
    result = prime * result + (expectedPhosphorylationSite ? 1231 : 1237);
    result = prime * result + (isPositionSpecific ? 1231 : 1237);
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
    PatternPosition other = (PatternPosition) obj;
    if (aminoAcidCodes == null) {
      if (other.aminoAcidCodes != null)
        return false;
    } else if (!aminoAcidCodes.equals(other.aminoAcidCodes))
      return false;
    if (expectedPhosphorylationSite != other.expectedPhosphorylationSite)
      return false;
    if (isPositionSpecific != other.isPositionSpecific)
      return false;
    return true;
  }
}
