package edu.mit.scansite.shared;

import java.util.Comparator;

import edu.mit.scansite.shared.transferobjects.DatabaseSearchScanResultSite;
import edu.mit.scansite.shared.transferobjects.Protein;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DbSearchScanResultSiteComparator implements Comparator<DatabaseSearchScanResultSite> {

  public enum ComparableFields {
    SCORE,
    PROTEIN_ACC,
    SITE_POSITION,
    MW,
    PI;
  }
  
  private ComparableFields compField = ComparableFields.SCORE;
  private boolean descending = false;
  private int siteIdx = -1;
  
  /**
   * Allows you to initialize a custom sorter.
   * @param compField Choose field to sort.
   * @param siteIdx Only required if SITE_POSITION is used as compField. If an invalid number is given, 0 is used.
   * @param ascendingSorting Sort ASCENDING or DESCENDING.
   */
  public DbSearchScanResultSiteComparator(ComparableFields compField, int siteIdx, boolean ascendingSorting) {
    this.compField = compField;
    this.siteIdx = siteIdx;
    this.descending = !ascendingSorting;
  }
  
  public DbSearchScanResultSiteComparator(ComparableFields compField, boolean ascendingSorting) {
    this(compField, 0, ascendingSorting);
  }

  @Override
  public int compare(DatabaseSearchScanResultSite srs1, DatabaseSearchScanResultSite srs2) {
    if (srs1 != null && srs2 != null) {
      int result = compareBy(compField, srs1, srs2);
      return ((descending) ? result * -1 : result);
    } else {
      return nullCheck(srs1, srs2);
    }
  }
  
  public void setSortStyle(boolean ascending) { 
    this.descending = !ascending;
  }
  
  private int compareBy(ComparableFields compField, DatabaseSearchScanResultSite srs1, DatabaseSearchScanResultSite srs2) {
    int result = 0;
    switch (compField) {
      case PROTEIN_ACC:
        Protein p1 = srs1.getProtein();
        Protein p2 = srs2.getProtein();
        result = (p1 != null && p2 != null) ? p1.getIdentifier().compareTo(p2.getIdentifier()) : nullCheck(p1, p2); 
        break;
      case SITE_POSITION: 
        if (!srs1.isMultiple() && !srs2.isMultiple()) {
          Integer pos1 = srs1.getSite().getPosition();
          Integer pos2 = srs2.getSite().getPosition();
          result = (pos1 != null && pos2 != null) ? pos1.compareTo(pos2) : nullCheck(pos1, pos2); 
        } else {
          if (!(siteIdx >= 0 && srs1.isMultiple() && srs2.isMultiple() && srs1.getSites().size() > siteIdx && srs2.getSites().size() > siteIdx)) {
            siteIdx = 0;
          }
          Integer pos1 = srs1.getSites().get(siteIdx).getPosition();
          Integer pos2 = srs2.getSites().get(siteIdx).getPosition();
          result = (pos1 != null && pos2 != null) ? pos1.compareTo(pos2) : nullCheck(pos1, pos2); 
        }
        break;
      case MW:  
        p1 = srs1.getProtein();
        p2 = srs2.getProtein();
        if (p1 == null || p2 == null) {
          result = nullCheck(p1, p2);
        } else {
          Double mw1 = Double.valueOf(p1.getMolecularWeight());
          Double mw2 = Double.valueOf(p2.getMolecularWeight());
          result = (mw1 != null && mw2 != null) ? mw1.compareTo(mw2) : nullCheck(mw1, mw2);
        }
        break;
      case PI:  
        p1 = srs1.getProtein();
        p2 = srs2.getProtein();
        if (p1 == null || p2 == null) {
          result = nullCheck(p1, p2);
        } else {
          Double pi1 = Double.valueOf(p1.getpI());
          Double pi2 = Double.valueOf(p2.getpI());
          result = (pi1 != null && pi2 != null) ? pi1.compareTo(pi2) : nullCheck(pi1, pi2);
        }
        break;
      default: // SCORE:  
        Double s1 = Double.valueOf(srs1.getScore());
        Double s2 = Double.valueOf(srs2.getScore());
        result = (s1 != null && s2 != null) ? s1.compareTo(s2) : nullCheck(s1, s2); 
        break;
    }
    return result;
  }

  private int nullCheck(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
      if (o1 == null && o2 == null) {
        return 0;
      } else if (o1 == null) {
        return 1;
      } else {
        return -1;
      }
    } else {
      return 0;
    }
  }

}
