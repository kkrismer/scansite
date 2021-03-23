package edu.mit.scansite.shared;

import java.util.Comparator;

import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;

/**
 * A comparator that makes it possible to sort ScanResultSites by a set of
 * different fields.
 * 
 * @author tobieh
 */
public class ScanResultSiteComparator implements Comparator<ScanResultSite> {
	public enum ComparableFields {
		GROUP, MOTIF, POSITION, PROTEIN_ID, SCORE, PERCENTILE, SURFACE_ACCESSIBILITY, SITE_AA;
	}

	private ComparableFields compField = ComparableFields.GROUP;
	private ComparableFields secondaryCompField = ComparableFields.MOTIF;
	private boolean descending = false;
	private boolean useSecondarySorting = false;

	public ScanResultSiteComparator(ComparableFields compField) {
		this.compField = compField;
	}

	public ScanResultSiteComparator(ComparableFields compField,
			boolean useSecondarySorting) {
		this.compField = compField;
		this.useSecondarySorting = useSecondarySorting;
	}

	public ScanResultSiteComparator() {
	}

	public void setCompareField(ComparableFields compField) {
		if (!this.compField.equals(compField)) {
			this.secondaryCompField = this.compField;
			this.compField = compField;
		}
	}

	public void setSortStyle(boolean ascending) {
		this.descending = !ascending;
	}

	@Override
	public int compare(ScanResultSite srs1, ScanResultSite srs2) {
		if (srs1 != null && srs2 != null) {
			return compareSites(srs1, srs2);
		} else {
			return nullCheck(srs1, srs2);
		}
	}

	private int compareSites(ScanResultSite srs1, ScanResultSite srs2) {
		int result = compareBy(compField, srs1, srs2);
		if (result == 0) {
			result = compareBy(secondaryCompField, srs1, srs2);
		}
		return (useSecondarySorting) ? ((descending) ? result * -1 : result)
				: result;
	}

	private int compareBy(ComparableFields compField, ScanResultSite srs1,
			ScanResultSite srs2) {
		int result = 0;
		switch (compField) {
		case GROUP:
			LightWeightMotifGroup group1 = srs1.getMotif().getGroup();
			LightWeightMotifGroup group2 = srs2.getMotif().getGroup();
			result = (group1 != null && group2 != null) ? group1
					.getDisplayName().compareTo(group2.getDisplayName())
					: nullCheck(group1, group2);
			break;
		case MOTIF:
			Motif m1 = srs1.getMotif();
			Motif m2 = srs2.getMotif();
			result = (m1 != null && m2 != null) ? m1.getDisplayName()
					.compareTo(m2.getDisplayName()) : nullCheck(m1, m2);
			break;
		case POSITION:
			Integer p1 = Integer.valueOf(srs1.getPosition());
			Integer p2 = Integer.valueOf(srs2.getPosition());
			result = (p1 != null && p2 != null) ? p1.compareTo(p2) : nullCheck(
					p1, p2);
			break;
		case PROTEIN_ID:
			String id1 = srs1.getProtein().getIdentifier();
			String id2 = srs2.getProtein().getIdentifier();
			result = (id1 != null && id2 != null) ? id1.compareTo(id2)
					: nullCheck(id1, id2);
			break;
		case SCORE:
			Double s1 = Double.valueOf(srs1.getScore());
			Double s2 = Double.valueOf(srs2.getScore());
			result = (s1 != null && s2 != null) ? s1.compareTo(s2) : nullCheck(
					s1, s2);
			break;
		case PERCENTILE:
			Double perc1 = Double.valueOf(srs1.getPercentile());
			Double perc2 = Double.valueOf(srs2.getPercentile());
			result = (perc1 != null && perc2 != null) ? perc1.compareTo(perc2)
					: nullCheck(perc1, perc2);
			break;
		case SURFACE_ACCESSIBILITY:
			Double sa1 = Double.valueOf(srs1.getSurfaceAccessValue());
			Double sa2 = Double.valueOf(srs2.getSurfaceAccessValue());
			result = (sa1 != null && sa2 != null) ? sa1.compareTo(sa2)
					: nullCheck(sa1, sa2);
			break;
		case SITE_AA:
			String site1 = srs1.getSite();
			String site2 = srs2.getSite();
			result = (site1 != null && site2 != null) ? site1.compareTo(site2)
					: nullCheck(site1, site2);
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
