package edu.mit.scansite.client.ui.view.features;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.dispatch.features.OrthologScanResult;
import edu.mit.scansite.shared.transferobjects.states.ScanOrthologsResultPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class ScanOrthologsResultPageView extends PageView implements
		Stateful<ScanOrthologsResultPageState> {
	public abstract void setResult(OrthologScanResult result);
}
