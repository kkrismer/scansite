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
		Connection connection = DbConnector.getInstance().getConnection();
		try {
			Statement statement = connection.createStatement();
			query = doGetSqlStatement();
			ResultSet resultSet = statement.executeQuery(query);

			T result = null;
			if (!resultSet.isLast()) {
				result = doProcessResults(resultSet);
			}
			DbConnector.getInstance().close(resultSet);
			DbConnector.getInstance().close(statement);

			return result;
		} catch (Exception e) {
			DataAccessException e2 = new DataAccessException(
					"executing SELECT-Statement failed: " + query + " (" + e.getMessage() + ")", e);
			logger.error(e2.getMessage(), e2);
			throw e2;
		} finally {
			//instead of closing the connection: reuse it
			//DbConnector.getInstance().close(connection);
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
