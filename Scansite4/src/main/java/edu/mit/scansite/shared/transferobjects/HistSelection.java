package edu.mit.scansite.shared.transferobjects;

/**
 * A datastructure that holds a selection/pair of a datasource and a taxon.
 * 
 * @author tobieh
 */
public class HistSelection {
	private Taxon taxon;
	private DataSource datasource;

	public HistSelection() {

	}

	public HistSelection(Taxon t, DataSource ds) {
		if (t != null && ds != null) {
			this.taxon = t;
			this.datasource = ds;
		}
	}

	public Taxon getTaxon() {
		return taxon;
	}

	public DataSource getDatasource() {
		return datasource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((datasource == null) ? 0 : datasource.hashCode());
		result = prime * result + ((taxon == null) ? 0 : taxon.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (this == obj) {
			return true;
		} else if (obj instanceof HistSelection) {
			HistSelection other = (HistSelection) obj;
			if (!taxon.equals(other.getTaxon())
					|| !datasource.equals(other.getDatasource())) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
}
