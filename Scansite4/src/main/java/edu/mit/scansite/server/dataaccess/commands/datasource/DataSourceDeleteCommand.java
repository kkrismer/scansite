package edu.mit.scansite.server.dataaccess.commands.datasource;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DataSourceDeleteCommand extends DbUpdateCommand {

	private int id = -1;

	public DataSourceDeleteCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, int id) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.id = id;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.DELETEFROM).append(c.gettDataSources())
				.append(CommandConstants.WHERE).append(c.getcDataSourcesId())
				.append(CommandConstants.EQ).append(id);
		return sql.toString();
	}
}
