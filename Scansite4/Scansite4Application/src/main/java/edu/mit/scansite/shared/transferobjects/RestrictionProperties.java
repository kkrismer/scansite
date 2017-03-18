package edu.mit.scansite.shared.transferobjects;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Konstantin Krismer
 */
public class RestrictionProperties implements IsSerializable {

	private OrganismClass organismClass = OrganismClass.MAMMALIA;
	private String speciesRegEx = null;
	private int phosphorylatedSites = 0;
	private Double molecularWeightFrom = null;
	private Double molecularWeightTo = null;
	private Double isoelectricPointFrom = null;
	private Double isoelectricPointTo = null;
	private String keywordRegEx = null;
	private List<String> sequenceRegEx = null;

	public RestrictionProperties() {

	}

	public RestrictionProperties(OrganismClass organismClass,
			String speciesRegEx, int phosphorylatedSites,
			Double molecularWeightFrom, Double molecularWeightTo,
			Double isoelectricPointFrom, Double isoelectricPointTo,
			String keywordRegEx, List<String> sequenceRegEx) {
		this.organismClass = organismClass;
		if (speciesRegEx != null && !speciesRegEx.isEmpty()) {
			this.speciesRegEx = speciesRegEx;
		}
		this.phosphorylatedSites = phosphorylatedSites;
		this.molecularWeightFrom = molecularWeightFrom;
		this.molecularWeightTo = molecularWeightTo;
		this.isoelectricPointFrom = isoelectricPointFrom;
		this.isoelectricPointTo = isoelectricPointTo;
		if (keywordRegEx != null && !keywordRegEx.isEmpty()) {
			this.keywordRegEx = keywordRegEx;
		}
		if (sequenceRegEx != null && !sequenceRegEx.isEmpty()) {
			this.sequenceRegEx = sequenceRegEx;
		}
	}

	public boolean isDefault() {
		if (!organismClass.equals(OrganismClass.MAMMALIA)) {
			return false;
		}
		if (speciesRegEx != null) {
			return false;
		}
		if (phosphorylatedSites != 0) {
			return false;
		}
		if (molecularWeightFrom != null) {
			return false;
		}
		if (molecularWeightTo != null) {
			return false;
		}
		if (isoelectricPointFrom != null) {
			return false;
		}
		if (isoelectricPointTo != null) {
			return false;
		}
		if (keywordRegEx != null) {
			return false;
		}
		if (sequenceRegEx != null) {
			return false;
		}
		return true;
	}

	public OrganismClass getOrganismClass() {
		return organismClass;
	}

	public void setOrganismClass(OrganismClass organismClass) {
		this.organismClass = organismClass;
	}

	public String getSpeciesRegEx() {
		return speciesRegEx;
	}

	public void setSpeciesRegEx(String speciesRegEx) {
		this.speciesRegEx = speciesRegEx;
	}

	public int getPhosphorylatedSites() {
		return phosphorylatedSites;
	}

	public void setPhosphorylatedSites(int phosphorylatedSites) {
		this.phosphorylatedSites = phosphorylatedSites;
	}

	public Double getMolecularWeightFrom() {
		return molecularWeightFrom;
	}

	public void setMolecularWeightFrom(Double molecularWeightFrom) {
		this.molecularWeightFrom = molecularWeightFrom;
	}

	public Double getMolecularWeightTo() {
		return molecularWeightTo;
	}

	public void setMolecularWeightTo(Double molecularWeightTo) {
		this.molecularWeightTo = molecularWeightTo;
	}

	public Double getIsoelectricPointFrom() {
		return isoelectricPointFrom;
	}

	public void setIsoelectricPointFrom(Double isoelectricPointFrom) {
		this.isoelectricPointFrom = isoelectricPointFrom;
	}

	public Double getIsoelectricPointTo() {
		return isoelectricPointTo;
	}

	public void setIsoelectricPointTo(Double isoelectricPointTo) {
		this.isoelectricPointTo = isoelectricPointTo;
	}

	public String getKeywordRegEx() {
		return keywordRegEx;
	}

	public void setKeywordRegEx(String keywordRegEx) {
		this.keywordRegEx = keywordRegEx;
	}

	public List<String> getSequenceRegEx() {
		return sequenceRegEx;
	}

	public void setSequenceRegEx(List<String> sequenceRegEx) {
		this.sequenceRegEx = sequenceRegEx;
	}
}
