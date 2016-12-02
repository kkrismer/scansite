package edu.mit.scansite.server.dataaccess.commands.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserGetAllCommand extends DbQueryCommand<ArrayList<User>> {

	private String tUsers;
	private String cEmail;
	private String cFirstName;
	private String cLastName;
	private String cIsAdmin;
	private String cIsSuperAdmin;

	public UserGetAllCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		tUsers = c.gettUsers();
		cEmail = c.getcUsersEmail();
		cFirstName = c.getcUsersFirstName();
		cLastName = c.getcUsersLastName();
		cIsAdmin = c.getcUsersIsAdmin();
		cIsSuperAdmin = c.getcUsersIsSuperAdmin();
	}

	@Override
	protected ArrayList<User> doProcessResults(ResultSet result)
			throws DataAccessException {
		ArrayList<User> users = new ArrayList<User>();
		try {
			while (result.next()) {
				users.add(new User(result.getString(cEmail), result
						.getString(cFirstName), result.getString(cLastName),
						"", result.getBoolean(cIsAdmin), result
								.getBoolean(cIsSuperAdmin)));
			}
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return users;
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(c.SELECT).append(cEmail).append(c.COMMA).append(cFirstName)
				.append(c.COMMA).append(cLastName).append(c.COMMA)
				.append(cIsAdmin).append(c.COMMA).append(cIsSuperAdmin)
				.append(c.FROM).append(tUsers).append(c.ORDERBY)
				.append(cFirstName).append(c.COMMA).append(cLastName);
		return sql.toString();
	}
}
