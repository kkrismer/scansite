package edu.mit.scansite.server.dataaccess.commands.user;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserUpdateCommand extends DbUpdateCommand {
	private User user;
	private boolean changePassword = false;

	public UserUpdateCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, User user,
			boolean changePassword) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.user = user;
		this.changePassword = changePassword;
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(c.UPDATE).append(c.gettUsers()).append(c.SET)
				.append(c.getcUsersFirstName()).append(c.EQ)
				.append(c.enquote(user.getFirstName())).append(c.COMMA)
				.append(c.getcUsersLastName()).append(c.EQ)
				.append(c.enquote(user.getLastName())).append(c.COMMA);
		if (changePassword) {
			sql.append(c.getcUsersPassword()).append(c.EQ)
					.append(" PASSWORD(\"").append(user.getPassword())
					.append("\") ").append(c.COMMA);
		}
		sql.append(c.getcUsersIsAdmin()).append(c.EQ).append(user.isAdmin())
				.append(c.COMMA);
		sql.append(c.getcUsersIsSuperAdmin()).append(c.EQ)
				.append(user.isSuperAdmin());
		sql.append(c.WHERE).append(c.getcUsersEmail()).append(c.LIKE)
				.append(c.enquote(user.getEmail()));
		return sql.toString();
	}
}
