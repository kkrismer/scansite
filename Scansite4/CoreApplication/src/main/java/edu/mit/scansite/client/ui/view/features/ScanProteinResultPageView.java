package edu.mit.scansite.client.ui.view.features;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;
import edu.mit.scansite.shared.transferobjects.states.ScanProteinResultPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class ScanProteinResultPageView extends PageView implements
		Stateful<ScanProteinResultPageState> {
	public abstract void setResult(ProteinScanResult result);
}
