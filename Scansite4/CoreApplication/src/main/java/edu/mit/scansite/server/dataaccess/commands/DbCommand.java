package edu.mit.scansite.server.dataaccess.commands;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;

/**
 * An abstract class that defines the basic functionality of a database command.
 * 
 * @author tobieh
 */
public abstract class DbCommand<T> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * An instance of CommandConstants.
	 */
	protected CommandConstants c;
	protected Properties dbAccessCfg;
	protected Properties dbConstantsCfg;

	/**
	 * @param dbAccessConfig
	 *            The config-file that holds the database connection settings.
	 * @param dbConstantsConfig
	 *            The config-file that holds the database table and column
	 *            information.
	 */
	public DbCommand(Properties dbAccessConfig, Properties dbConstantsConfig) {
		this.dbAccessCfg = dbAccessConfig;
		this.dbConstantsCfg = dbConstantsConfig;
		this.c = CommandConstants.instance(dbConstantsConfig);
	}

	/**
	 * Sets the CommandConstants class to use temporary tables or not. Temporary
	 * tables are used when the database is updated.
	 * 
	 * @param useTemporaryTables
	 *            TRUE, in order to use temporary tables, FALSE otherwise.
	 */
	public void setUseOfTempTables(boolean useTemporaryTables) {
		this.c = CommandConstants.instance(useTemporaryTables);
	}

	/**
	 * Executes the command.
	 * 
	 * @return Returns the result of type T.
	 * @throws DatabaseException
	 * 			   Is thrown if the command cannot be executed.
	 *             Is thrown if disconnecting the database is not possible.
	 */
	public T execute() throws DatabaseException {
		try {
			T result = doExecute();
			return result;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (dbAccessCfg.isEmpty()) {
				throw new DataAccessException(
						"database access config file not found", ex);
			} else {
				throw new DataAccessException(ex.getMessage(), ex);
			}
		}
	}

	/**
	 * @return The config.
	 */
	protected Properties getConfig() {
		return dbAccessCfg;
	}

	/**
	 * Retrieves the sql-statement.
	 * 
	 * @return The sql-statement that is going to be executed.
	 * @throws DataAccessException
	 *             Is thrown if an error occurs.
	 */
	protected abstract String doGetSqlStatement() throws DataAccessException;

	/**
	 * Retrieves the sql-statement.
	 * 
	 * @throws DataLayerException
	 *             Is thrown if an error occurs.
	 */
	// protected abstract void doSupplyValues(PreparedStatement statement);

	/**
	 * Hook method for the actual execution of the sql statement.
	 *
	 * @return Executes the actual sql statement and returns the appropriate
	 *         result.
	 */
	protected abstract T doExecute()
			throws DataAccessException;
}
