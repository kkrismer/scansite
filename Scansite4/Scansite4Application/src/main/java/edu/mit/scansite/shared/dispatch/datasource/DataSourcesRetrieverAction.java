package edu.mit.scansite.shared.dispatch.datasource;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.DataSourceType;

/**
 * @author Tobieh
 */
public class DataSourcesRetrieverAction implements
    Action<DataSourcesRetrieverResult> {
  private boolean primaryDataSourcesOnly = false;
  private DataSourceType type = null;

  public DataSourcesRetrieverAction() {
  }

  public DataSourcesRetrieverAction(DataSourceType type) {
    this.setType(type);
  }

  public DataSourcesRetrieverAction(boolean primaryDataSourcesOnly) {
    this.primaryDataSourcesOnly = primaryDataSourcesOnly;
  }

  public DataSourcesRetrieverAction(DataSourceType type,
      boolean primaryDataSourcesOnly) {
    this.setType(type);
    this.primaryDataSourcesOnly = primaryDataSourcesOnly;
  }

  public boolean isPrimaryDataSourcesOnly() {
    return primaryDataSourcesOnly;
  }

  public void setPrimaryDataSourcesOnly(boolean primaryDataSourcesOnly) {
    this.primaryDataSourcesOnly = primaryDataSourcesOnly;
  }

  public DataSourceType getType() {
    return type;
  }

  public void setType(DataSourceType type) {
    this.type = type;
  }
}
