package edu.mit.scansite.shared.dispatch.datasource;

import java.util.List;

import edu.mit.scansite.shared.transferobjects.DataSourceType;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Konstantin Krismer
 */
public class DataSourceTypesRetrieverResult implements Result {
	private List<DataSourceType> dataSourceTypes;

	public DataSourceTypesRetrieverResult() {
	}

	public DataSourceTypesRetrieverResult(List<DataSourceType> dataSourceTypes) {
		this.dataSourceTypes = dataSourceTypes;
	}

	public List<DataSourceType> getDataSourceTypes() {
		return dataSourceTypes;
	}

	public void setDataSourceTypes(List<DataSourceType> dataSourceTypes) {
		this.dataSourceTypes = dataSourceTypes;
	}
}
