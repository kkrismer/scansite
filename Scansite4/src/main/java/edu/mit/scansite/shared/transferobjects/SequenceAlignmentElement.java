package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Konstantin Krismer
 */
public class SequenceAlignmentElement implements
    Comparable<SequenceAlignmentElement>, IsSerializable {
  private String identifier;
  private String sequence;
  private String organismName;
  private boolean hasPhosphoSite;

  public SequenceAlignmentElement() {

  }

  public SequenceAlignmentElement(String identifier, String sequence,
      String organismName, boolean hasPhosphoSite) {
    super();
    this.identifier = identifier;
    this.sequence = sequence;
    this.organismName = organismName;
    this.setHasPhosphoSite(hasPhosphoSite);
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getSequence() {
    return sequence;
  }

  public void setSequence(String sequence) {
    this.sequence = sequence;
  }

  public String getOrganismName() {
    return organismName;
  }

  public void setOrganismName(String organismName) {
    this.organismName = organismName;
  }

  public boolean hasPhosphoSite() {
    return hasPhosphoSite;
  }

  public void setHasPhosphoSite(boolean hasPhosphoSite) {
    this.hasPhosphoSite = hasPhosphoSite;
  }

  @Override
  public int compareTo(SequenceAlignmentElement o) {
    if(this.hasPhosphoSite && o.hasPhosphoSite() || !this.hasPhosphoSite && !o.hasPhosphoSite()) {
      return this.getIdentifier().compareTo(o.getIdentifier());
    } else if(this.hasPhosphoSite) {
      return -1;
    } else {
      return 1;
    }
    
  }
}
