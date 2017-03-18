package edu.mit.scansite.client.ui.presenter.features;

import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.features.ScanSeqResultPageView;
import edu.mit.scansite.shared.dispatch.features.SequenceMatchResult;
import edu.mit.scansite.shared.transferobjects.states.ScanSeqResultPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ScanSeqResultPagePresenter extends Presenter {
	private ScanSeqResultPageView view;
	private SequenceMatchResult result;

	public ScanSeqResultPagePresenter(ScanSeqResultPageView view) {
		this.view = view;
		this.result = null;
	}

	public ScanSeqResultPagePresenter(ScanSeqResultPageView view,
			SequenceMatchResult result) {
		this.view = view;
		this.result = result;
	}

	public ScanSeqResultPagePresenter(ScanSeqResultPageView view,
			ScanSeqResultPageState state) {
		this.view = view;
		this.result = null;
		view.setState(state);
	}

	@Override
	public void bind() {
		if (result != null) {
			view.setResult(result);
		}
		fireHistoryEvent(view.getState(),
				NavigationEvent.PageId.FEATURE_SCAN_SEQ_RESULT.getId());
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
}
