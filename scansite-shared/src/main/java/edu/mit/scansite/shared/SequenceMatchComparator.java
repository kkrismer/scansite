package edu.mit.scansite.shared;

import java.util.Comparator;

import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.ProteinSequenceMatch;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SequenceMatchComparator implements
		Comparator<ProteinSequenceMatch> {
	public enum ComparableFields {
		PROTEIN_ACC, SITE_MATCHES, ISOELECTRIC_POINT, MOLECULAR_WEIGHT;
	}

	private ComparableFields compField = ComparableFields.PROTEIN_ACC;
	private int regexIdx = 0;

	public SequenceMatchComparator() {
	}

	public SequenceMatchComparator(ComparableFields compField, int regexIdx) {
		if (compField != null) {
			this.compField = compField;
		}
		if (compField == ComparableFields.SITE_MATCHES) {
			if (regexIdx >= 0) {
				this.regexIdx = regexIdx;
			} else {
				regexIdx = 0;
			}
		}
	}

	public SequenceMatchComparator(ComparableFields compField) {
		this(compField, 0);
	}

	@Override
	public int compare(ProteinSequenceMatch o1, ProteinSequenceMatch o2) {
		if (o1 == null && o2 == null) {
			return 0;
		} else if (o1 == null || o2 == null) {
			return o1 == null ? -1 : 1;
		} else {
			if (compField == ComparableFields.PROTEIN_ACC) {
				Protein p1 = o1.getProtein();
				Protein p2 = o2.getProtein();
				if (p1 == null && p2 == null) {
					return 0;
				} else if (p1 == null || p2 == null) {
					return p1 == null ? -1 : 1;
				} else {
					return p1.getIdentifier().compareTo(p2.getIdentifier());
				}
			} else if (compField == ComparableFields.MOLECULAR_WEIGHT) {
				Protein p1 = o1.getProtein();
				Protein p2 = o2.getProtein();
				if (p1 == null && p2 == null) {
					return 0;
				} else if (p1 == null || p2 == null) {
					return p1 == null ? -1 : 1;
				} else {
					return (p1.getMolecularWeight() < p2.getMolecularWeight()) ? -1
							: (Double.compare(p1.getMolecularWeight(), p2
									.getMolecularWeight()) == 0) ? 0 : 1;
				}
			} else if (compField == ComparableFields.ISOELECTRIC_POINT) {
				Protein p1 = o1.getProtein();
				Protein p2 = o2.getProtein();
				if (p1 == null && p2 == null) {
					return 0;
				} else if (p1 == null || p2 == null) {
					return p1 == null ? -1 : 1;
				} else {
					return (p1.getpI() < p2.getpI()) ? -1 : (Double.compare(p1.getpI(), p2
							.getpI()) == 0) ? 0 : 1;
				}
			} else { // compfield == SITE_MATCHES
				int[] matches1 = o1.getNrOfSequenceMatches();
				int[] matches2 = o2.getNrOfSequenceMatches();
				if (matches1 == null && matches2 == null) {
					return 0;
				} else if (matches1 == null || matches2 == null) {
					return matches1 == null ? -1 : 1;
				} else {
					if (matches1.length != matches2.length) {
						compField = ComparableFields.PROTEIN_ACC;
						return compare(o1, o2);
					}
					if (regexIdx >= matches1.length) {
						regexIdx = 0;
					}
					return (matches1[regexIdx] < matches2[regexIdx]) ? -1
							: matches1[regexIdx] == matches2[regexIdx] ? 0 : 1;
				}
			}
		}
	}

}
