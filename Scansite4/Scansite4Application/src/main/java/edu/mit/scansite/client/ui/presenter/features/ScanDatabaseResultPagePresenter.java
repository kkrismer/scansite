package edu.mit.scansite.client.ui.presenter.features;

import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.features.ScanDatabaseResultPageView;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanResult;
import edu.mit.scansite.shared.transferobjects.states.ScanDatabaseResultPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ScanDatabaseResultPagePresenter extends Presenter {
	private ScanDatabaseResultPageView view;
	private DatabaseScanResult result;

	public ScanDatabaseResultPagePresenter(ScanDatabaseResultPageView view) {
		this.view = view;
		this.result = null;
	}

	public ScanDatabaseResultPagePresenter(ScanDatabaseResultPageView view,
			DatabaseScanResult result) {
		this.view = view;
		this.result = result;
	}

	public ScanDatabaseResultPagePresenter(ScanDatabaseResultPageView view,
			ScanDatabaseResultPageState state) {
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
				NavigationEvent.PageId.FEATURE_SCAN_DB_RESULT.getId());
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
}
