package edu.mit.scansite.shared;

import java.util.Comparator;

import edu.mit.scansite.shared.transferobjects.Ortholog;
import edu.mit.scansite.shared.transferobjects.Protein;

/**
 * @author Konstantin Krismer
 */
public class OrthologComparator implements Comparator<Ortholog> {
	public enum ComparableFields {
		PROTEIN_ACC, ISOELECTRIC_POINT, MOLECULAR_WEIGHT;
	}

	private ComparableFields compField = ComparableFields.PROTEIN_ACC;

	public OrthologComparator() {

	}

	public OrthologComparator(ComparableFields compField) {
		this.compField = compField;
	}

	@Override
	public int compare(Ortholog o1, Ortholog o2) {
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
			} else { // (compField == ComparableFields.ISOELECTRIC_POINT) {
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
			}
		}
	}
}
