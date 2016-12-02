package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.mit.scansite.shared.util.Formatter;

/**
 * @author Tobieh
 */
public class Taxon implements IsSerializable {

	private int id = -1;
	private String name;
	private String parentTaxonList = "";
	private boolean isSpecies = false;

	public Taxon() {
	}

	public Taxon(String name, String parentTaxonList, boolean isSpecies) {
		super();
		this.name = name;
		this.parentTaxonList = parentTaxonList;
		this.isSpecies = isSpecies;
	}

	public Taxon(String name) {
		super();
		this.name = name;
	}

	public Taxon(int id, String name, String parentTaxonList, boolean isSpecies) {
		super();
		this.id = id;
		this.name = name;
		this.parentTaxonList = parentTaxonList;
		this.isSpecies = isSpecies;
	}

	public Taxon(int id, String name, boolean isSpecies) {
		this.id = id;
		this.name = name;
		this.isSpecies = isSpecies;
	}

	public Taxon(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		Formatter f = new Formatter();
		return f.replaceMagicQuotes(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentTaxonList() {
		return parentTaxonList;
	}

	public void setParentTaxonList(String parentTaxonList) {
		this.parentTaxonList = parentTaxonList;
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isSpecies() {
		return isSpecies;
	}

	public void setSpecies(boolean isSpecies) {
		this.isSpecies = isSpecies;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Taxon other = (Taxon) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
