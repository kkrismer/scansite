package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Konstantin Krismer
 */
public class OrthologScanSiteRegion implements IsSerializable {
  private String downstreamRegion;
  private String upstreamRegion;
  private char phosphorylatedAminoAcid;
  private boolean isPredictedSite;
  private String identifier;
  private String species;

  public OrthologScanSiteRegion() {

  }

  public OrthologScanSiteRegion(String downstreamRegion, String upstreamRegion,
      char phosphorylatedAminoAcid, boolean isPredictedSite, String identifier,
      String species) {
    super();
    this.downstreamRegion = downstreamRegion;
    this.upstreamRegion = upstreamRegion;
    this.phosphorylatedAminoAcid = phosphorylatedAminoAcid;
    this.isPredictedSite = isPredictedSite;
    this.identifier = identifier;
    this.species = species;
  }

  public String getDownstreamRegion() {
    return downstreamRegion;
  }

  public void setDownstreamRegion(String downstreamRegion) {
    this.downstreamRegion = downstreamRegion;
  }

  public String getUpstreamRegion() {
    return upstreamRegion;
  }

  public void setUpstreamRegion(String upstreamRegion) {
    this.upstreamRegion = upstreamRegion;
  }

  public char getPhosphorylatedAminoAcid() {
    return phosphorylatedAminoAcid;
  }

  public void setPhosphorylatedAminoAcid(char phosphorylatedAminoAcid) {
    this.phosphorylatedAminoAcid = phosphorylatedAminoAcid;
  }

  public boolean isPredictedSite() {
    return isPredictedSite;
  }

  public void setPredictedSite(boolean isPredictedSite) {
    this.isPredictedSite = isPredictedSite;
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
