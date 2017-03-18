package edu.mit.scansite.client.ui.view.features;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanResult;
import edu.mit.scansite.shared.transferobjects.states.ScanDatabaseResultPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class ScanDatabaseResultPageView extends PageView implements
		Stateful<ScanDatabaseResultPageState> {
	public abstract void setResult(DatabaseScanResult result);
}
