package edu.mit.scansite.server.updater;

/**
 * A class that uses reflection for instantiating DbUpdaters.
 * 
 * @author tobieh
 */
public class DbUpdaterFactory {
	public static DbUpdater getDbUpdater(String dbUpdaterClassName)
			throws DbUpdaterException {
		try {
			Class<? extends DbUpdater> updaterClass = Class.forName(
					dbUpdaterClassName).asSubclass(DbUpdater.class);
			DbUpdater updater = updaterClass.newInstance();
			return updater;
		} catch (Exception e) {
			throw new DbUpdaterException(e);
		}
	}
}
