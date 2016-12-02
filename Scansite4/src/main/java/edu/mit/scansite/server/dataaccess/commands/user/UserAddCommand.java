package edu.mit.scansite.server.dataaccess.commands.user;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserAddCommand extends DbInsertCommand {
	private User entry;

	public UserAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, User entry) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.entry = entry;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return null;
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(c.INSERTINTO).append(c.gettUsers()).append(" ( ")
				.append(c.getcUsersEmail()).append(c.COMMA)
				.append(c.getcUsersFirstName()).append(c.COMMA)
				.append(c.getcUsersLastName()).append(c.COMMA)
				.append(c.getcUsersPassword()).append(c.COMMA)
				.append(c.getcUsersIsAdmin()).append(c.COMMA)
				.append(c.getcUsersIsSuperAdmin()).append(" ) ")
				.append(c.VALUES).append(" ( \"").append(entry.getEmail())
				.append("\" ").append(c.COMMA).append("\" ")
				.append(entry.getFirstName()).append("\" ").append(c.COMMA)
				.append("\" ").append(entry.getLastName()).append("\" ")
				.append(c.COMMA).append("PASSWORD(\"")
				.append(entry.getPassword()).append("\") ").append(c.COMMA)
				.append(entry.isAdmin()).append(c.COMMA)
				.append(entry.isSuperAdmin()).append(" ) ");
		return sql.toString();
	}

	@Override
	protected String getIdColumnName() {
		return null;
	}
}
