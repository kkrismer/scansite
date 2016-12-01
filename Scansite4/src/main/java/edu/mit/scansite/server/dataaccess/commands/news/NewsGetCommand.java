package edu.mit.scansite.server.dataaccess.commands.news;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.NewsEntry;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsGetCommand extends DbQueryCommand<NewsEntry> {

	private int id;

	public NewsGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, int id) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.id = id;
	}

	@Override
	protected NewsEntry doProcessResults(ResultSet result)
			throws DataAccessException {
		try {
			if (result.next()) {
				User user = new User(result.getString(c.getcUsersEmail()),
						result.getString(c.getcUsersFirstName()),
						result.getString(c.getcUsersLastName()), "",
						result.getBoolean(c.getcUsersIsAdmin()),
						result.getBoolean(c.getcUsersIsSuperAdmin()));
				return new NewsEntry(result.getInt(c.getcNewsId()),
						result.getString(c.getcNewsTitle()), result.getString(c
								.getcNewsText()), result.getDate(c
								.getcNewsDate()), user);
			}
			return null;
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(c.SELECT).append(c.getcNewsId()).append(c.COMMA)
				.append(c.getcNewsTitle()).append(c.COMMA)
				.append(c.getcNewsText()).append(c.COMMA + c.getcUsersEmail())
				.append(c.COMMA).append(c.getcNewsDate())
				.append(c.COMMA + c.getcUsersFirstName()).append(c.COMMA)
				.append(c.getcUsersLastName()).append(c.COMMA)
				.append(c.getcUsersIsAdmin()).append(c.COMMA)
				.append(c.getcUsersIsSuperAdmin()).append(c.FROM)
				.append(c.gettNews()).append(c.INNERJOIN).append(c.gettUsers())
				.append(CommandConstants.USING).append(CommandConstants.LPAR)
				.append(c.getcUsersEmail()).append(CommandConstants.RPAR)
				.append(c.WHERE).append(c.getcNewsId()).append(c.EQ).append(id);
		return sql.toString();
	}
}
