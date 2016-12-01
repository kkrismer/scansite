package edu.mit.scansite.server.dataaccess.commands.identifiertype;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.IdentifierType;

/**
 * @author Konstantin Krismer
 */
public class IdentifierTypeAddCommand extends DbInsertCommand {
	private IdentifierType type;

	public IdentifierTypeAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			IdentifierType type) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.type = type;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettIdentifierTypes();
	}

	@Override
	protected String getIdColumnName() {
		return null; // id is not set by database
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();

		sql.append(CommandConstants.INSERTINTO).append(c.gettIdentifierTypes())
				.append('(').append(c.getcIdentifierTypesId())
				.append(CommandConstants.COMMA)
				.append(c.getcIdentifierTypesName()).append(')')
				.append(CommandConstants.VALUES).append('(')
				.append(type.getId()).append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(type.getName())).append(')');
		return sql.toString();
	}
}
