package edu.mit.scansite.shared.transferobjects;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Tobieh
 */
public class MotifGroup extends LightWeightMotifGroup {
  private List<LightWeightMotif> motifs;

  public MotifGroup() {
    super();
    this.motifs = new LinkedList<LightWeightMotif>();
  }

  public MotifGroup(int id, String name, String shortName) {
    super(id, name, shortName);
    this.motifs = new LinkedList<LightWeightMotif>();
  }

  public MotifGroup(String name, String shortName) {
    super(name, shortName);
    this.motifs = new LinkedList<LightWeightMotif>();
  }

  public MotifGroup(int id, String name, String shortName,
      List<LightWeightMotif> motifs) {
    super(id, name, shortName);
    this.motifs = motifs;
  }

  public MotifGroup(String name, String shortName, List<LightWeightMotif> motifs) {
    super(name, shortName);
    this.motifs = motifs;
  }

  public List<LightWeightMotif> getMotifs() {
    return motifs;
  }

  public void setMotifs(List<LightWeightMotif> motifs) {
    this.motifs = motifs;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + getId();
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
    MotifGroup other = (MotifGroup) obj;
    if (getId() != other.getId())
      return false;
    return true;
  }
}
