package edu.mit.scansite.server.dataaccess.commands;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * A class for querying database entries.
 * 
 * @author tobieh
 */
public abstract class DbQueryCommand<T> extends DbCommand<T> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public DbQueryCommand(Properties dbAccessConfig, Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	@Override
	protected T doExecute() throws DataAccessException {
		String query = "";
		DbConnector dbConnector = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			dbConnector = DbConnector.getInstance();
			connection = dbConnector.getConnection();
			statement = connection.createStatement();
			query = doGetSqlStatement();
			resultSet = statement.executeQuery(query);

			T result = null;
			if (!resultSet.isLast()) {
				result = doProcessResults(resultSet);
			}

			return result;
		} catch (Exception e) {
			DataAccessException dataAccessException = new DataAccessException(
					"executing SELECT statement failed: " + query + " (" + e.getMessage() + ")", e);
			logger.error(dataAccessException.getMessage(), dataAccessException);
			throw dataAccessException;
		} finally {
			if (dbConnector != null) {
				try {
					dbConnector.close(resultSet);
					dbConnector.close(statement);
					dbConnector.close(connection);
				} catch (Exception e) {
					DataAccessException dataAccessException = new DataAccessException(
							"closing result set, statement, and connection for SELECT statement failed: " + query + " (" + e.getMessage() + ")", e);
					logger.error(dataAccessException.getMessage(), dataAccessException);
					throw dataAccessException;
				}
			}
		}
	}

	/**
	 * @param result
	 *            The resultset that is returned by the databaseConnector.
	 * @return The processed results.
	 * @throws DataAccessException
	 *             Is thrown if an error occurs.
	 */
	protected abstract T doProcessResults(ResultSet result) throws DataAccessException;
}
