package edu.mit.scansite.client.ui.presenter.features;

import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.features.ScanOrthologsResultPageView;
import edu.mit.scansite.shared.dispatch.features.OrthologScanResult;
import edu.mit.scansite.shared.transferobjects.states.ScanOrthologsResultPageState;

/**
 * @author Konstantin Krismer
 */
public class ScanOrthologsResultPagePresenter extends Presenter {
	private ScanOrthologsResultPageView view;
	private OrthologScanResult result;

	public ScanOrthologsResultPagePresenter(ScanOrthologsResultPageView view) {
		this.view = view;
		this.result = null;
	}

	public ScanOrthologsResultPagePresenter(ScanOrthologsResultPageView view,
			OrthologScanResult result) {
		this.view = view;
		this.result = result;
	}

	public ScanOrthologsResultPagePresenter(ScanOrthologsResultPageView view,
			ScanOrthologsResultPageState state) {
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
				NavigationEvent.PageId.FEATURE_SCAN_ORTHOLOGS_RESULT.getId());
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
}
