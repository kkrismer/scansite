package edu.mit.scansite.shared.transferobjects;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

/**
 * @author Konstantin Krismer
 */
public class Ortholog implements IsSerializable {
  private Protein protein;
  private List<ScanResultSite> phosphorylationSites;

  public static final ProvidesKey<Ortholog> KEY_PROVIDER = new ProvidesKey<Ortholog>() {
    public Object getKey(Ortholog item) {
      return item == null ? null : (item.getProtein() != null) ? item
          .getProtein().getIdentifier() : null;
    }
  };
  
  public Ortholog() {
    super();
  }


  
  public Ortholog(Protein protein, List<ScanResultSite> phosphorylationSites) {
    super();
    this.protein = protein;
    this.phosphorylationSites = phosphorylationSites;
  }

  public Protein getProtein() {
    return protein;
  }

  public void setProtein(Protein protein) {
    this.protein = protein;
  }

  public List<ScanResultSite> getPhosphorylationSites() {
    return phosphorylationSites;
  }

  public void setPhosphorylationSites(List<ScanResultSite> phosphorylationSites) {
    this.phosphorylationSites = phosphorylationSites;
  }
}
