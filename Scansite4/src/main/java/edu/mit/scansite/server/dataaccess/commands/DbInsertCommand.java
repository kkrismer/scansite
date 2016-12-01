package edu.mit.scansite.server.dataaccess.commands;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * A command for inserting tuples into the database.
 * 
 * @author tobieh
 */
public abstract class DbInsertCommand extends DbCommand<Integer> {
	public DbInsertCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	@Override
	protected Integer doExecute(DbConnector dbConnector)
			throws DataAccessException {
		return dbConnector.executeInsertQuery(doGetSqlStatement());
	}

	/**
	 * @return The name of the current table.
	 * @throws DataLayerException
	 *             Is thrown if an error occurs.
	 */
	protected abstract String getTableName() throws DataAccessException;

	/**
	 * @return The name of the id column in the current table.
	 * @throws DataLayerException
	 *             Is thrown if an error occurs.
	 */
	protected abstract String getIdColumnName();
}
