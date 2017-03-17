package edu.mit.scansite.server.dataaccess.commands.identifiertype;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.IdentifierType;

/**
 * @author Konstantin Krismer
 */
public class IdentifierTypesGetAllCommand extends
    DbQueryCommand<List<IdentifierType>> {

  public IdentifierTypesGetAllCommand(Properties dbAccessConfig,
      Properties dbConstantsConfig) {
    super(dbAccessConfig, dbConstantsConfig);
  }

  @Override
  protected List<IdentifierType> doProcessResults(ResultSet result)
      throws DataAccessException {
    List<IdentifierType> types = new LinkedList<IdentifierType>();
    try {
      while (result.next()) {
        IdentifierType type = new IdentifierType();
        type.setId(result.getInt(c.getcIdentifierTypesId()));
        type.setName(result.getString(c.getcIdentifierTypesName()));
        types.add(type);
      }
    } catch (Exception e) {
      throw new DataAccessException(e.getMessage(), e);
    }
    return types;
  }

  @Override
  protected String doGetSqlStatement() throws DataAccessException {
    StringBuilder sql = new StringBuilder();
    sql.append(CommandConstants.SELECT).append(c.getcIdentifierTypesId())
        .append(CommandConstants.COMMA).append(c.getcIdentifierTypesName())
        .append(CommandConstants.FROM).append(c.gettIdentifierTypes());
    return sql.toString();
  }
}
