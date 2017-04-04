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

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.Dao#disableChecks()
	 */
	@Override
	public void disableChecks() throws ScansiteUpdaterException {
		try {
			DbConnector.getInstance().setAutoCommit(false);
			Statement stmt = DbConnector.getInstance().getConnection().createStatement();
			stmt.execute("SET UNIQUE_CHECKS=0;");
			stmt.execute("SET FOREIGN_KEY_CHECKS=0;");
			DbConnector.getInstance().close(stmt);
		} catch (Exception e) {
			logger.error("Cannot disable checks: " + e.getMessage(), e);
			throw new ScansiteUpdaterException("Cannot disable checks", e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.Dao#enableChecks()
	 */
	@Override
	public void enableChecks() throws ScansiteUpdaterException {
		try {
			DbConnector.getInstance().setAutoCommit(true);
			Statement stmt = DbConnector.getInstance().getConnection().createStatement();
			stmt.execute("SET UNIQUE_CHECKS=1;");
			stmt.execute("SET FOREIGN_KEY_CHECKS=1;");
			DbConnector.getInstance().close(stmt);
		} catch (Exception e) {
			logger.error("Cannot enable checks: " + e.getMessage(), e);
			throw new ScansiteUpdaterException("Cannot enable checks", e);
		}
	}
}
