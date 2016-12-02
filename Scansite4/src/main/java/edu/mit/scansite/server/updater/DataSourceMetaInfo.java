package edu.mit.scansite.server.updater;

import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * Encapsulates the information that is saved about a single database defined in
 * the updater configuration file.
 * 
 * @author Konstantin Krismer
 */
public class DataSourceMetaInfo {
	private DataSource dataSource;
	private String url;
	private String versionUrl;
	private String updaterClass;
	private String encoding;
	private String organismName;

	public DataSourceMetaInfo() {
	}

	public DataSourceMetaInfo(DataSource dataSource, String url,
			String versionUrl, String updaterClass, String encoding,
			String organismName) {
		this.dataSource = dataSource;
		this.url = url;
		this.versionUrl = versionUrl;
		this.updaterClass = updaterClass;
		this.encoding = encoding;
		this.organismName = organismName;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersionUrl() {
		return versionUrl;
	}

	public void setVersionUrl(String versionUrl) {
		this.versionUrl = versionUrl;
	}

	public String getUpdaterClass() {
		return updaterClass;
	}

	public void setUpdaterClass(String updaterClass) {
		this.updaterClass = updaterClass;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getOrganismName() {
		return organismName;
	}

	public void setOrganismName(String organismName) {
		this.organismName = organismName;
	}
}
