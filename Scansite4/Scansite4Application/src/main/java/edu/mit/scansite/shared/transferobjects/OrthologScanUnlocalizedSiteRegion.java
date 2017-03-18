package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Konstantin Krismer
 */
public class OrthologScanUnlocalizedSiteRegion implements IsSerializable {
  private String sequence;
  private String identifier;
  private String species;

  public OrthologScanUnlocalizedSiteRegion() {

  }

  public OrthologScanUnlocalizedSiteRegion(String sequence, String identifier,
      String species) {
    super();
    this.sequence = sequence;
    this.identifier = identifier;
    this.species = species;
  }

  public String getSequence() {
    return sequence;
  }

  public void setSequence(String sequence) {
    this.sequence = sequence;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getSpecies() {
    return species;
  }

  public void setSpecies(String species) {
    this.species = species;
  }
}
