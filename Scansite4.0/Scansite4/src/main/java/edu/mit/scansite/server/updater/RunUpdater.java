package edu.mit.scansite.server.updater;

import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		try {
			Updater updater = new Updater();
			updater.update();
		} catch (ScansiteUpdaterException e) {
			logger.error(e.getMessage());
		}
	}
}
