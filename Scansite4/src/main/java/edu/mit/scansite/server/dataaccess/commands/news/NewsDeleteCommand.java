package edu.mit.scansite.server.dataaccess.commands.news;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsDeleteCommand extends DbUpdateCommand {

	private int id;

	public NewsDeleteCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, int id) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.id = id;
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(c.DELETEFROM).append(c.gettNews()).append(c.WHERE)
				.append(c.getcNewsId()).append(c.EQ).append(id);
		return sql.toString();
	}
}
