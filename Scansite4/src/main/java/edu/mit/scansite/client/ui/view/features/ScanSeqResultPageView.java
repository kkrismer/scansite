package edu.mit.scansite.client.ui.view.features;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.dispatch.features.SequenceMatchResult;
import edu.mit.scansite.shared.transferobjects.states.ScanSeqResultPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class ScanSeqResultPageView extends PageView implements
		Stateful<ScanSeqResultPageState> {
	public abstract void setResult(SequenceMatchResult result);
}
