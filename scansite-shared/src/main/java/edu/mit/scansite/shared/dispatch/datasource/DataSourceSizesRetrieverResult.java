package edu.mit.scansite.shared.dispatch.datasource;

import java.util.Map;

import edu.mit.scansite.shared.transferobjects.DataSource;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Konstantin Krismer
 */
public class DataSourceSizesRetrieverResult implements Result {
	private Map<DataSource, Integer> dataSourceSizes;

	public DataSourceSizesRetrieverResult() {
	}

	public DataSourceSizesRetrieverResult(
			Map<DataSource, Integer> dataSourceSizes) {
		this.dataSourceSizes = dataSourceSizes;
	}

	public Map<DataSource, Integer> getDataSourceSizes() {
		return dataSourceSizes;
	}

	public void setDataSourceSizes(Map<DataSource, Integer> dataSourceSizes) {
		this.dataSourceSizes = dataSourceSizes;
	}
}
