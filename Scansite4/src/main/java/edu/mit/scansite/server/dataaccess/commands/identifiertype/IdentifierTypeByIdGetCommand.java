package edu.mit.scansite.server.dataaccess.commands.identifiertype;

import java.sql.ResultSet;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.IdentifierType;

/**
 * @author Konstantin Krismer
 */
public class IdentifierTypeByIdGetCommand extends
		DbQueryCommand<IdentifierType> {
	private int id;

	public IdentifierTypeByIdGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, int id) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.id = id;
	}

	@Override
	protected IdentifierType doProcessResults(ResultSet result)
			throws DataAccessException {
		try {
			IdentifierType type = null;
			if (result.next()) {
				type = new IdentifierType(result.getInt(c
						.getcIdentifierTypesId()), result.getString(c
						.getcIdentifierTypesName()));
			}
			return type;
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcIdentifierTypesId())
				.append(CommandConstants.COMMA)
				.append(c.getcIdentifierTypesName())
				.append(CommandConstants.FROM).append(c.gettIdentifierTypes())
				.append(CommandConstants.WHERE)
				.append(c.getcIdentifierTypesId()).append(CommandConstants.EQ)
				.append(id);
		return sql.toString();
	}
}
