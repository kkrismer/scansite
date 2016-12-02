package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Tobieh
 */
public enum OrganismClass implements IsSerializable {
	MAMMALIA("Mammals", "MAM"), VERTEBRATA("Vertebrates", "VRT"), INVERTEBRATA(
			"Invertebrates", "INV"), BACTERIA("Bacteria", "BAC"), FUNGI(
			"Fungi", "FUN"), VIRUSES("Viruses", "VIR"), PLANTS("Plants", "PLT"), OTHER(
			"Other", "OTR"), ALL("All", "ALL");

	String displayName;
	String shortName;

	private OrganismClass(String displayName, String shortName) {
		this.displayName = displayName;
		this.shortName = shortName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getShortName() {
		return shortName;
	}

	public static OrganismClass getByStringRepresentation(String representation) {
		for (OrganismClass oc : OrganismClass.values()) {
			if (oc.getDisplayName().equalsIgnoreCase(representation)
					|| oc.getShortName().equalsIgnoreCase(representation)) {
				return oc;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return displayName;
	}
}
