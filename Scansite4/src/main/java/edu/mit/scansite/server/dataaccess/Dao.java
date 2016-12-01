package edu.mit.scansite.server.dataaccess;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Konstantin Krismer
 */
public interface Dao {

	public abstract void disableChecks() throws ScansiteUpdaterException;

	public abstract void enableChecks() throws ScansiteUpdaterException;

}