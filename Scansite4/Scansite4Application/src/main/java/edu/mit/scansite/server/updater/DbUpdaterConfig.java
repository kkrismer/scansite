package edu.mit.scansite.server.updater;

import java.util.List;

import edu.mit.scansite.shared.transferobjects.DataSourceType;
import edu.mit.scansite.shared.transferobjects.EvidenceCode;
import edu.mit.scansite.shared.transferobjects.IdentifierType;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DbUpdaterConfig {

	private String tempDirPath;
	private String invalidFilePrefix;
	private List<DataSourceType> dataSourceTypes;
	private List<IdentifierType> identifierTypes;
	private List<EvidenceCode> evidenceCodes;
	private List<DataSourceMetaInfo> dataSourceMetaInfos;

	public DbUpdaterConfig() {
	}

	public DbUpdaterConfig(String tempDirPath, String invalidFilePrefix,
			List<DataSourceType> dataSourceTypes,
			List<IdentifierType> identifierTypes,
			List<EvidenceCode> evidenceCodes,
			List<DataSourceMetaInfo> dataSourceMetaInfos) {
		this.tempDirPath = tempDirPath;
		this.invalidFilePrefix = invalidFilePrefix;
		this.dataSourceTypes = dataSourceTypes;
		this.identifierTypes = identifierTypes;
		this.evidenceCodes = evidenceCodes;
		this.dataSourceMetaInfos = dataSourceMetaInfos;
	}

	public String getTempDirPath() {
		return tempDirPath;
	}

	public void setTempDirPath(String tempDirPath) {
		this.tempDirPath = tempDirPath;
	}

	public String getInvalidFilePrefix() {
		return invalidFilePrefix;
	}

	public void setInvalidFilePrefix(String invalidFilePrefix) {
		this.invalidFilePrefix = invalidFilePrefix;
	}

	public List<DataSourceType> getDataSourceTypes() {
		return dataSourceTypes;
	}

	public void setDataSourceTypes(List<DataSourceType> dataSourceTypes) {
		this.dataSourceTypes = dataSourceTypes;
	}

	public List<IdentifierType> getIdentifierTypes() {
		return identifierTypes;
	}

	public void setIdentifierTypes(List<IdentifierType> identifierTypes) {
		this.identifierTypes = identifierTypes;
	}

	public List<EvidenceCode> getEvidenceCodes() {
		return evidenceCodes;
	}

	public void setEvidenceCodes(List<EvidenceCode> evidenceCodes) {
		this.evidenceCodes = evidenceCodes;
	}

	public List<DataSourceMetaInfo> getDataSourceMetaInfos() {
		return dataSourceMetaInfos;
	}

	public void setDataSourceMetaInfos(
			List<DataSourceMetaInfo> dataSourceMetaInfos) {
		this.dataSourceMetaInfos = dataSourceMetaInfos;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DbUpdaterConfig\n");
		sb.append(" General:\n");
		sb.append("  TempDirPath = ").append(tempDirPath).append('\n');
		sb.append("  InvalidDirPath = ").append(invalidFilePrefix).append('\n');
		sb.append(" DataSourceTypes:\n");
		boolean first = true;
		for (DataSourceType dataSourceType : dataSourceTypes) {
			if (first) {
				first = false;
			} else {
				sb.append("  --\n");
			}
			sb.append("  Id = ").append(dataSourceType.getId()).append('\n');
			sb.append("  ShortName = ").append(dataSourceType.getShortName())
					.append('\n');
			sb.append("  DisplayName = ")
					.append(dataSourceType.getDisplayName()).append('\n');
		}
		sb.append(" IdentifierTypes:\n");
		first = true;
		for (IdentifierType identifierType : identifierTypes) {
			if (first) {
				first = false;
			} else {
				sb.append("  --\n");
			}
			sb.append("  Id = ").append(identifierType.getId()).append('\n');
			sb.append("  Name = ").append(identifierType.getName())
					.append('\n');
		}
		sb.append(" EvidenceCodes:\n");
		first = true;
		for (EvidenceCode evidenceCode : evidenceCodes) {
			if (first) {
				first = false;
			} else {
				sb.append("  --\n");
			}
			sb.append("  Code = ").append(evidenceCode.getCode()).append('\n');
			sb.append("  Name = ").append(evidenceCode.getName()).append('\n');
		}
		sb.append(" DataSources:\n");
		first = true;
		for (DataSourceMetaInfo db : dataSourceMetaInfos) {
			if (first) {
				first = false;
			} else {
				sb.append("  --\n");
			}
			sb.append("  DataSourceType = ")
					.append(db.getDataSource().getType().getDisplayName())
					.append('\n');
			sb.append("  IdentifierType = ")
					.append(db.getDataSource().getIdentifierType().getName())
					.append('\n');
			sb.append("  ShortName = ")
					.append(db.getDataSource().getShortName()).append('\n');
			sb.append("  DisplayName = ")
					.append(db.getDataSource().getDisplayName()).append('\n');
			sb.append("  Description = ")
					.append(db.getDataSource().getDescription()).append('\n');
			sb.append("  MetaInfoUrl = ").append(db.getUrl()).append('\n');
			sb.append("  MetaInfoVersionUrl = ").append(db.getVersionUrl())
					.append('\n');
			sb.append("  MetaInfoUpdaterClass = ").append(db.getUpdaterClass())
					.append('\n');
			sb.append("  MetaInfoEncoding = ").append(db.getEncoding())
					.append('\n');
			sb.append("  MetaInfoOrganismName = ").append(db.getOrganismName())
					.append('\n');
		}
		return sb.toString();
	}
}
