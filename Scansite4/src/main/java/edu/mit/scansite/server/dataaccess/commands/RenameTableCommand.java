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
      return "IF EXISTS(SELECT table_name" +
              " FROM INFORMATION_SCHEMA.TABLES "
              +"WHERE table_schema = 'scansite4' "
              +"AND table_name LIKE '" + fromTableName
              + "') THEN ALTER TABLE " + fromTableName + " RENAME " + toTableName
              + "; END IF";
  }

  public void setFromTableName(String fromTableName) {
    this.fromTableName = fromTableName;
  }
  
  public void setToTableName(String toTableName) {
    this.toTableName = toTableName;
  }
}
