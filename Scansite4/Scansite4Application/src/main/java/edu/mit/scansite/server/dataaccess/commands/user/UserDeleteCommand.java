package edu.mit.scansite.server.dataaccess.commands.user;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserDeleteCommand extends DbUpdateCommand {

	private String email;

	public UserDeleteCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, String email) {
		super(dbAccessConfig, dbConstantsConfig);
		this.email = email;
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(c.DELETEFROM).append(c.gettUsers()).append(c.WHERE)
				.append(c.getcUsersEmail()).append(c.LIKE).append('\"')
				.append(email).append('\"');
		return sql.toString();
	}
}
