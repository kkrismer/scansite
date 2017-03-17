package edu.mit.scansite.server.dataaccess.commands;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * A class for updating database entries.
 * 
 * @author tobieh
 */
public abstract class DbUpdateCommand extends DbCommand<Integer> {
	public DbUpdateCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	@Override
	protected Integer doExecute()
			throws DataAccessException {
		return DbConnector.getInstance().executeUpdateQuery(doGetSqlStatement());
	}
}
