package edu.mit.scansite.shared.transferobjects;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.mit.scansite.shared.util.Formatter;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DataSource implements IsSerializable {
	protected int id = -1;
	private DataSourceType type;
	private IdentifierType identifierType;
	protected String shortName = "";
	protected String displayName = "";
	protected String description = "";
	protected String version = "";
	protected Date lastUpdate;
	private boolean isPrimaryDataSource = false;

	public DataSource() {

	}

	public DataSource(String shortName, String displayName) {
		super();
		this.shortName = shortName;
		this.displayName = displayName;
	}

	public DataSource(DataSourceType type, IdentifierType identifierType,
			String shortName, String displayName, String description,
			String version, Date lastUpdate, boolean isPrimaryDataSource) {
		super();
		this.type = type;
		this.identifierType = identifierType;
		this.shortName = shortName;
		this.displayName = displayName;
		this.description = description;
		this.version = version;
		this.lastUpdate = lastUpdate;
		this.isPrimaryDataSource = isPrimaryDataSource;
	}

	public DataSource(int id, DataSourceType type,
			IdentifierType identifierType, String shortName,
			String displayName, String description, String version,
			Date lastUpdate, boolean isPrimaryDataSource) {
		super();
		this.id = id;
		this.type = type;
		this.identifierType = identifierType;
		this.shortName = shortName;
		this.displayName = displayName;
		this.description = description;
		this.version = version;
		this.lastUpdate = lastUpdate;
		this.isPrimaryDataSource = isPrimaryDataSource;
	}

	public DataSource(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String name) {
		Formatter formatter = new Formatter();
		this.shortName = formatter.replaceMagicQuotes(name);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		Formatter formatter = new Formatter();
		this.description = formatter.replaceMagicQuotes(description);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		Formatter formatter = new Formatter();
		this.version = formatter.replaceMagicQuotes(version);
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public DataSourceType getType() {
		return type;
	}

	public void setType(DataSourceType type) {
		this.type = type;
	}

	public IdentifierType getIdentifierType() {
		return identifierType;
	}

	public void setIdentifierType(IdentifierType identifierType) {
		this.identifierType = identifierType;
	}

	public boolean isPrimaryDataSource() {
		return isPrimaryDataSource;
	}

	public void setPrimaryDataSource(boolean isPrimaryDataSource) {
		this.isPrimaryDataSource = isPrimaryDataSource;
	}

	@Override
	public String toString() {
		return shortName;
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
		DataSource other = (DataSource) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
