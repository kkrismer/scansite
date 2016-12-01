package edu.mit.scansite.server.dataaccess.databaseconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;

/**
 * A simple class for using a database (SELECT, INSERT und UPDATE).
 * 
 * @author Tobias Ehrenberger
 * @author Konstantin Krismer
 */
public class DbConnector {
	private final Logger logger = LoggerFactory.getLogger(DbConnector.class);
	/**
	 * The configuration file variables for accessing the database.
	 */
	private static final String DB_DRIVER = "DB_DRIVER";
	private static final String DB_CONNECTION_STRING = "DB_CONNECTION_STRING";
	private static final String DB_USER = "DB_USER";
	private static final String DB_PASSWORD = "DB_PASSWORD";

	private DataSource dataSource = null;
	private Connection singleConnection = null;
	private boolean singleConnectionMode = false;

	/**
	 * The database connection url.
	 */
	private String dbDriver;

	/**
	 * The database driver.
	 */
	private String driverUrl;

	/**
	 * The username for accessing the database.
	 */
	private String user;

	/**
	 * The password for accessing the database.
	 */
	private String password;

	/**
	 * The constructor.
	 * 
	 * @param config
	 *            The config propterties file needs to contain this information:
	 *            - dbDriver (e.g. "com.mysql.jdbc.Driver), - connectionString
	 *            (e.g. "jdbc:mysql://localhost:port/datbaseName"), - username
	 *            (e.g. user), - password (e.g. mypw or nothing, then the
	 *            password is interpreted as "").
	 * @throws DatabaseException
	 *             Is thrown if either the config file does not contain the
	 *             required information or the given Driver can not be loaded.
	 */
	public DbConnector(Properties config) throws DatabaseException {
		dbDriver = config.getProperty(DB_DRIVER, null);
		driverUrl = config.getProperty(DB_CONNECTION_STRING, null);
		user = config.getProperty(DB_USER, null);
		password = config.getProperty(DB_PASSWORD, "");
		if (dbDriver == null) {
			DatabaseAccessException e = new DatabaseAccessException(
					"No DatabaseDriver given " + "(Check \"" + DB_DRIVER
							+ "\" in config-file).");
			logger.error(e.getMessage(), e);
			throw e;
		}
		if (driverUrl == null) {
			DatabaseAccessException e = new DatabaseAccessException(
					"No connectionString given " + "(Check \""
							+ DB_CONNECTION_STRING + "\" in config-file).");
			logger.error(e.getMessage(), e);
			throw e;
		}
		if (user == null) {
			DatabaseAccessException e = new DatabaseAccessException(
					"No username given " + "(Check \"" + DB_USER
							+ "\" in config-file).");
			logger.error(e.getMessage(), e);
			throw e;
		}

		try {
			Class.forName(this.dbDriver);
		} catch (ClassNotFoundException e) {
			DatabaseAccessException e2 = new DatabaseAccessException(
					"Given DatabaseDriver invalid " + "(Check \"" + DB_DRIVER
							+ "\" in config-file).", e);
			logger.error(e.getMessage(), e2);
			throw e2;
		}
	}

	public void initConnectionPooling() {
		PoolProperties properties = new PoolProperties();
		properties.setUrl(driverUrl);
		properties.setDriverClassName(dbDriver);
		properties.setUsername(user);
		properties.setPassword(password);
		properties.setJmxEnabled(true);
		properties.setTestWhileIdle(false);
		properties.setTestOnBorrow(true);
		properties.setValidationQuery("SELECT 1");
		properties.setTestOnReturn(false);
		properties.setValidationInterval(30000);
		properties.setTimeBetweenEvictionRunsMillis(30000);
		properties.setMaxActive(100);
		properties.setInitialSize(10);
		properties.setMaxWait(10000);
		properties.setRemoveAbandonedTimeout(60);
		properties.setMinEvictableIdleTimeMillis(30000);
		properties.setMinIdle(10);
		properties.setLogAbandoned(true);
		properties.setRemoveAbandoned(true);
		properties
				.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
						+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		dataSource = new DataSource();
		dataSource.setPoolProperties(properties);
		singleConnectionMode = false;
		logger.info("Tomcat JDBC connection pool is established");
	}

	public void initLongTimeConnection() throws DataAccessException {
		try {
			singleConnection = DriverManager.getConnection(driverUrl, user,
					password);
			singleConnectionMode = true;
			logger.info("Long time connection is established");
		} catch (Exception e) {
			DataAccessException e2 = new DataAccessException(
					"Connecting to database failed: " + e.getMessage(), e);
			logger.error(e2.getMessage(), e);
			throw e2;
		}
	}

	public boolean isSingleConnectionMode() {
		return singleConnectionMode;
	}

	/**
	 * Closes given result set hard
	 */
	public void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}

	/**
	 * Closes given PreparedStatement set hard
	 */
	public void close(PreparedStatement preparedStatement) {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}

	/**
	 * Retrieves a connection from the Tomcat connection pool, returns stored
	 * single connection in single connection mode
	 * 
	 * @throws DataAccessException
	 */
	public Connection getConnection() throws DataAccessException {
		try {
			if (singleConnectionMode) {
				return singleConnection;
			} else {
				return dataSource.getConnection();
			}
		} catch (SQLException e) {
			DataAccessException e2 = new DataAccessException(e.getMessage(), e);
			logger.error(e2.getMessage(), e);
			throw e2;
		}
	}

	public void close(Connection connection) {
		if (connection != null && !singleConnectionMode) {
			try {
				connection.close();
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}

	public void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}

//	No need to close the data source
//	public void closeDataSource() {
//		dataSource.close(true);
//		logger.info("closed data source and all open connections");
//	}

	public void closeLongTimeConnection() throws DatabaseAccessException {
		if (singleConnectionMode) {
			close(singleConnection);
			logger.info("closed long time connection");
		} else {
			DatabaseAccessException e = new DatabaseAccessException(
					"Long time connection can only be closed in single connection mode");
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * @return The last automatically generated key value
	 */
	public int executeInsertQuery(final String query)
			throws DataAccessException {
		if (singleConnectionMode) {
			return executeInsertQuery(singleConnection, query);
		} else {
			return executeInsertQuery(null, query);
		}
	}

	/**
	 * @return The last automatically generated key value
	 */
	public int executeInsertQuery(Connection connection, final String query)
			throws DataAccessException {
		boolean providedConnection = connection != null;
		try {
			int id = 0;
			if (!providedConnection) {
				connection = dataSource.getConnection();
			}
			Statement statement = connection.createStatement();
			statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = statement.getGeneratedKeys();
			while (resultSet.next()) {
				if (id < resultSet.getInt(1)) {
					id = resultSet.getInt(1);
				}
			}
			close(resultSet);
			close(statement);
			return id;
		} catch (Exception e) {
			DataAccessException e2 = new DataAccessException(
					"executing INSERT-Statement failed: " + query, e);
			logger.error(e2.getMessage(), e2);
			throw e2;
		} finally {
			if (!providedConnection) {
				close(connection);
			}
		}
	}

	/**
	 * @return The number of rows updated or, in case of DDL statements, zero.
	 */
	public int executeUpdateQuery(final String query)
			throws DataAccessException {
		if (singleConnectionMode) {
			return executeUpdateQuery(singleConnection, query);
		} else {
			return executeUpdateQuery(null, query);
		}
	}

	/**
	 * @return The number of rows updated or, in case of DDL statements, zero.
	 */
	public int executeUpdateQuery(Connection connection, final String query)
			throws DataAccessException {
		boolean providedConnection = connection != null;
		try {
			int id = 0;
			if (!providedConnection) {
				connection = dataSource.getConnection();
			}
			Statement statement = connection.createStatement();
			id = statement.executeUpdate(query);
			close(statement);
			return id;
		} catch (Exception e) {
			DataAccessException e2 = new DataAccessException(
					"executing UPDATE-Statement failed: " + query, e);
			logger.error(e2.getMessage(), e2);
			throw e2;
		} finally {
			if (!providedConnection) {
				close(connection);
			}
		}
	}

	public void rollback() throws DatabaseAccessException {
		if (!singleConnectionMode) {
			DatabaseAccessException e = new DatabaseAccessException(
					"Rollback can only be performed in single connection mode");
			logger.error(e.getMessage(), e);
			throw e;
		}
		try {
			singleConnection.rollback();
		} catch (Exception e) {
			throw new DatabaseAccessException(e.getMessage(), e);
		}
	}

	public void setAutoCommit(boolean autoCommit)
			throws DatabaseAccessException {
		if (!singleConnectionMode) {
			DatabaseAccessException e = new DatabaseAccessException(
					"AutoCommit property can only be set in single connection mode");
			logger.error(e.getMessage(), e);
			throw e;
		}
		try {
			singleConnection.setAutoCommit(autoCommit);
		} catch (Exception e) {
			throw new DatabaseAccessException(e.getMessage(), e);
		}
	}
}
