package edu.mit.scansite.server.dataaccess.databaseconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.shared.DataAccessException;

/**
 * A simple class for using a database (SELECT, INSERT und UPDATE).
 *
 * @author Thomas Bernwinkler
 * @author Konstantin Krismer
 * @author Tobias Ehrenberger
 */
public class DbConnector {
	private final Logger logger = LoggerFactory.getLogger(DbConnector.class);

	private static DbConnector datasource;
	private BasicDataSource ds;

	private DbConnector() throws DataAccessException {
		Properties properties;
		try {
			properties = ServiceLocator.getDbAccessProperties();

			ds = new BasicDataSource();
			ds.setDriverClassName(properties.get("DB_DRIVER").toString());
			ds.setUsername(properties.get("DB_USER").toString());
			ds.setPassword(properties.get("DB_PASSWORD").toString());
			ds.setUrl(properties.get("DB_CONNECTION_STRING").toString());
		} catch (DataAccessException e) {
			logger.error("Could not find database connection files. Assuming Web Service is running.");
			throw e;
		}
	}

	public static DbConnector getInstance() throws DataAccessException {
		if (datasource == null) {
			datasource = new DbConnector();
			return datasource;
		} else {
			return datasource;
		}
	}

	public Connection getConnection() throws SQLException {
		return this.ds.getConnection();
	}

	/**
	 * Closes given result set hard
	 * @throws SQLException 
	 */
	public void close(ResultSet resultSet) throws SQLException {
		if (resultSet != null) {
			resultSet.close();
		}
	}

	/**
	 * Closes given PreparedStatement set hard
	 * 
	 * @throws SQLException
	 */
	public void close(PreparedStatement preparedStatement) throws SQLException {
		if (preparedStatement != null) {
			preparedStatement.close();
		}
	}

	public void close(Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	public void close(Statement statement) throws SQLException {
		if (statement != null) {
			statement.close();
		}
	}

	/**
	 * @return The last automatically generated key value
	 */
	public int executeInsertQuery(final String query) throws DataAccessException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			int id = 0;
			connection = getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			resultSet = statement.getGeneratedKeys();
			while (resultSet.next()) {
				if (id < resultSet.getInt(1)) {
					id = resultSet.getInt(1);
				}
			}
			return id;
		} catch (Exception e) {
			DataAccessException dataAccessException = new DataAccessException("executing INSERT statement failed: " + query, e);
			logger.error(dataAccessException.getMessage(), dataAccessException);
			throw dataAccessException;
		} finally {
			try {
				close(resultSet);
				close(statement);
				close(connection);
			} catch (SQLException e) {
				DataAccessException dataAccessException = new DataAccessException("closing INSERT statement failed: " + query, e);
				logger.error(dataAccessException.getMessage(), dataAccessException);
				throw dataAccessException;
			}
		}
	}

	/**
	 * @return The number of rows updated or, in case of DDL statements, zero.
	 */
	public int executeUpdateQuery(final String query) throws DataAccessException {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = getConnection();
			statement = connection.createStatement();
			return statement.executeUpdate(query);
		} catch (Exception e) {
			DataAccessException dataAccessException = new DataAccessException("executing UPDATE statement failed: " + query, e);
			logger.error(dataAccessException.getMessage(), dataAccessException);
			throw dataAccessException;
		} finally {
			try {
				close(statement);
				close(connection);
			} catch (SQLException e) {
				DataAccessException dataAccessException = new DataAccessException("closing UPDATE statement failed: " + query, e);
				logger.error(dataAccessException.getMessage(), dataAccessException);
				throw dataAccessException;
			}
		}
	}
}
