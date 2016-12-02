package edu.mit.scansite.shared.transferobjects.states;

import edu.mit.scansite.shared.dispatch.features.DatabaseScanResult;

/**
 * @author Konstantin Krismer
 */
public class ScanDatabaseResultPageState extends State {
	private DatabaseScanResult databaseScanResult;

	public ScanDatabaseResultPageState() {

	}

	public ScanDatabaseResultPageState(DatabaseScanResult databaseScanResult) {
		this.databaseScanResult = databaseScanResult;
	}

	public DatabaseScanResult getDatabaseScanResult() {
		return databaseScanResult;
	}

	public void setDatabaseScanResult(DatabaseScanResult databaseScanResult) {
		this.databaseScanResult = databaseScanResult;
	}
}
