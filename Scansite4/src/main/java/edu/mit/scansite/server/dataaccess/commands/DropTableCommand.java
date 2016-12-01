package edu.mit.scansite.server.dataaccess.commands;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 */
public class DropTableCommand extends DbUpdateCommand {
  
  private String tableName;

  public DropTableCommand(Properties dbAccessConfig, Properties dbConstantsConfig, DbConnector dbConnector, String tableName) {
    super(dbAccessConfig, dbConstantsConfig, dbConnector);
    this.tableName = tableName;
  }
  
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  @Override
  protected String doGetSqlStatement() throws DataAccessException {
    return "DROP TABLE " + tableName;
  }

}
