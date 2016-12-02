package edu.mit.scansite.server.dataaccess.commands;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 */
public class RenameTableCommand extends DbUpdateCommand {
  
  private String fromTableName;
  private String toTableName;

  public RenameTableCommand(Properties dbAccessConfig, Properties dbConstantsConfig, DbConnector dbConnector, String fromTableName, String toTableName) {
    super(dbAccessConfig, dbConstantsConfig, dbConnector);
    this.fromTableName = fromTableName;
    this.toTableName = toTableName;
  }

  @Override
  protected String doGetSqlStatement() throws DataAccessException {
    return "ALTER TABLE " + fromTableName + " RENAME " + toTableName;
  }

  public void setFromTableName(String fromTableName) {
    this.fromTableName = fromTableName;
  }
  
  public void setToTableName(String toTableName) {
    this.toTableName = toTableName;
  }
}
