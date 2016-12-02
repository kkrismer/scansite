package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Konstantin Krismer
 */
public class LightWeightProtein implements IsSerializable {
	protected String identifier;
	protected DataSource dataSource = null;
	protected String sequence = null;

	public LightWeightProtein() {

	}

	public LightWeightProtein(Protein protein) {
		this.identifier = protein.getIdentifier();
		this.dataSource = protein.getDataSource();
		this.sequence = protein.getSequence();
	}

	public LightWeightProtein(String identifier, String sequence) {
		this.identifier = identifier;
		this.sequence = sequence;
	}

	public LightWeightProtein(String identifier, DataSource dataSource) {
		this.identifier = identifier;
		this.dataSource = dataSource;
	}

	public LightWeightProtein(String identifier, DataSource dataSource,
			String sequence) {
		this.identifier = identifier;
		this.dataSource = dataSource;
		this.sequence = sequence;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return identifier
				+ ((dataSource == null) ? " (user-defined)" : (" ("
						+ dataSource.getDisplayName() + ")"));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataSource == null) ? 0 : dataSource.hashCode());
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
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
		LightWeightProtein other = (LightWeightProtein) obj;
		if (dataSource == null) {
			if (other.dataSource != null)
				return false;
		} else if (!dataSource.equals(other.dataSource))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		return true;
	}
}
