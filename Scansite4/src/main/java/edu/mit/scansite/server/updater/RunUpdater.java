package edu.mit.scansite.server.updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DatabaseException;

/**
 * Runs the database updater. This class can be used to instantiate a scansite
 * database. After the tables have been created with the appropriate SQL-Script,
 * simply run this program using the corresponding *.xml file.
 * 
 * @author tobieh
 */
public class RunUpdater {
	private static final Logger logger = LoggerFactory
			.getLogger(RunUpdater.class);

	public static void main(String[] args) {
		DbConnector dbConnector = null;
		try {
			dbConnector = new DbConnector(ServiceLocator.getInstance().getDbAccessFile());
			dbConnector.initLongTimeConnection();
			Updater updater = new Updater(dbConnector);
			updater.update();
		} catch (ScansiteUpdaterException | DatabaseException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				dbConnector.closeLongTimeConnection();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
