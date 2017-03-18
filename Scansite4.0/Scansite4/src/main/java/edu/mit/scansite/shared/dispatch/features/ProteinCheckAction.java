package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.dispatch.BooleanResult;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinCheckAction implements Action<BooleanResult> {

  private DataSource dataSource;
  private String accessionContains;

  public ProteinCheckAction() {
  }

  public ProteinCheckAction(DataSource dataSource, String accessionContains) {
    this.dataSource = dataSource;
    this.accessionContains = accessionContains;
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public String getAccessionContains() {
    return accessionContains;
  }

  public void setAccessionContains(String accessionNr) {
    this.accessionContains = accessionNr;
  }
}
