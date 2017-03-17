package edu.mit.scansite.server.dataaccess.commands;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 */
public class DropTableCommand extends DbUpdateCommand {
  
  private String tableName;

  public DropTableCommand(Properties dbAccessConfig, Properties dbConstantsConfig, String tableName) {
    super(dbAccessConfig, dbConstantsConfig);
    this.tableName = tableName;
  }
  
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  @Override
  protected String doGetSqlStatement() throws DataAccessException {
    return "IF EXISTS(SELECT table_name" +
            " FROM INFORMATION_SCHEMA.TABLES "
            +"WHERE table_schema = 'scansite4' "
            +"AND table_name LIKE '" + tableName
            + "') THEN DROP TABLE " + tableName
            + "; END IF";
  }

}
