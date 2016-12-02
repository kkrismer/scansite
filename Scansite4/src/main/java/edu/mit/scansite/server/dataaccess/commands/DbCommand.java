package edu.mit.scansite.server.dataaccess.commands;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
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
	protected DbConnector dbConnector;

	/**
	 * @param dbAccessConfig
	 *            The config-file that holds the database connection settings.
	 * @param dbConstantsConfig
	 *            The config-file that holds the database table and column
	 *            information.
	 * @param dbConnector
	 *            A dbConnector, if a connection is already available, or NULL,
	 *            if just a temporary connection is needed.
	 */
	public DbCommand(Properties dbAccessConfig, Properties dbConstantsConfig,
			DbConnector dbConnector) {
		this.dbAccessCfg = dbAccessConfig;
		this.dbConstantsCfg = dbConstantsConfig;
		this.dbConnector = dbConnector;
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
	 * @throws DataLayerException
	 *             Is thrown if the command cannot be executed.
	 * @throws DatabaseException
	 *             Is thrown if disconnecting the database is not possible.
	 */
	public T execute() throws DataAccessException, DatabaseException {
		if (dbConnector == null) {
			logger.warn("dbConnector was not initialized");
			dbConnector = new DbConnector(dbAccessCfg);
			dbConnector.initConnectionPooling();
		}
		try {
			T result = doExecute(dbConnector);
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
	 * @throws DataLayerException
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
	 * @param dbConnector
	 *            The databaseConnector (already connected to the database!).
	 * @return Executes the actual sql statement and returns the appropriate
	 *         result.
	 * @throws DataLayerException
	 *             Is thrown if an error occurs.
	 */
	protected abstract T doExecute(DbConnector dbConnector)
			throws DataAccessException;
}
