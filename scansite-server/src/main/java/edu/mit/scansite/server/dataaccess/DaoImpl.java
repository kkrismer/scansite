package edu.mit.scansite.server.dataaccess;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * An abstract superclass for DAOs.
 * 
 * @author tobieh
 */
public abstract class DaoImpl implements Dao {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected Properties dbAccessConfig;
	protected Properties dbConstantsConfig;

	protected boolean useTempTablesForUpdate = false;

	public DaoImpl(Properties dbAccessConfig, Properties dbConstantsConfig) {
		this.dbAccessConfig = dbAccessConfig;
		this.dbConstantsConfig = dbConstantsConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.Dao#disableChecks()
	 */
	@Override
	public void disableChecks() throws ScansiteUpdaterException {
		DbConnector dbConnector = null;
		Connection connection = null;
		Statement statement = null;
		try {
			dbConnector = DbConnector.getInstance();
			// dbConnector.setAutoCommit(false);
	        // TODO disabled after DbConnector rework
			connection = DbConnector.getInstance().getConnection();
			statement = DbConnector.getInstance().getConnection().createStatement();
			statement.execute("SET UNIQUE_CHECKS=0;");
			statement.execute("SET FOREIGN_KEY_CHECKS=0;");
		} catch (Exception e) {
			logger.error("Cannot disable checks: " + e.getMessage(), e);
			throw new ScansiteUpdaterException("Cannot disable checks", e);
		} finally {
			if (dbConnector != null) {
				try {
					dbConnector.close(statement);
					dbConnector.close(connection);
				} catch (Exception e) {
					ScansiteUpdaterException exception = new ScansiteUpdaterException(
							"closing statement, and connection after checks disabled failed (" + e.getMessage() + ")", e);
					logger.error(exception.getMessage(), exception);
					throw exception;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.Dao#enableChecks()
	 */
	@Override
	public void enableChecks() throws ScansiteUpdaterException {
		DbConnector dbConnector = null;
		Connection connection = null;
		Statement statement = null;
		try {
			dbConnector = DbConnector.getInstance();
			// dbConnector.setAutoCommit(true);
	        // TODO disabled after DbConnector rework
			connection = DbConnector.getInstance().getConnection();
			statement = DbConnector.getInstance().getConnection().createStatement();
			statement.execute("SET UNIQUE_CHECKS=1;");
			statement.execute("SET FOREIGN_KEY_CHECKS=1;");
		} catch (Exception e) {
			logger.error("Cannot enable checks: " + e.getMessage(), e);
			throw new ScansiteUpdaterException("Cannot enable checks", e);
		} finally {
			if (dbConnector != null) {
				try {
					dbConnector.close(statement);
					dbConnector.close(connection);
				} catch (Exception e) {
					ScansiteUpdaterException exception = new ScansiteUpdaterException(
							"closing statement, and connection after checks disabled failed (" + e.getMessage() + ")", e);
					logger.error(exception.getMessage(), exception);
					throw exception;
				}
			}
		}
	}
}
