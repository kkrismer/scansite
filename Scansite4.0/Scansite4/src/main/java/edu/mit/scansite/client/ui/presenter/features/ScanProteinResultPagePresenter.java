package edu.mit.scansite.client.ui.presenter.features;

import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.features.ScanProteinResultPageView;
import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;
import edu.mit.scansite.shared.transferobjects.states.ScanProteinResultPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ScanProteinResultPagePresenter extends Presenter {
	private ScanProteinResultPageView view;
	private ProteinScanResult result;

	public ScanProteinResultPagePresenter(ScanProteinResultPageView view) {
		this.view = view;
		this.result = null;
	}

	public ScanProteinResultPagePresenter(ScanProteinResultPageView view,
			ProteinScanResult result) {
		this.view = view;
		this.result = result;
	}

	public ScanProteinResultPagePresenter(ScanProteinResultPageView view,
			ScanProteinResultPageState state) {
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
				NavigationEvent.PageId.FEATURE_SCAN_PROTEIN_RESULT.getId());
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
}
