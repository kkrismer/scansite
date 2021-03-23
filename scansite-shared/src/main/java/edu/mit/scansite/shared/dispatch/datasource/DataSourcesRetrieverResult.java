package edu.mit.scansite.shared.dispatch.datasource;

import java.util.List;

import edu.mit.scansite.shared.transferobjects.DataSource;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DataSourcesRetrieverResult implements Result {
  private List<DataSource> dataSources;
  
  public DataSourcesRetrieverResult() {
  }
  
  public DataSourcesRetrieverResult(List<DataSource> dataSources) {
    this.dataSources = dataSources;
  }

  public List<DataSource> getDataSources() {
    return dataSources;
  }
  
  public void setDataSources(List<DataSource> dataSources) {
    this.dataSources = dataSources;
  }
}
