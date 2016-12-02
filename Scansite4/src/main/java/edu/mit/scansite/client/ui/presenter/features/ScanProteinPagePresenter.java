package edu.mit.scansite.client.ui.presenter.features;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.event.ProteinScanEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.features.ScanProteinPageView;
import edu.mit.scansite.shared.dispatch.features.ProteinScanAction;
import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.states.ScanProteinPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ScanProteinPagePresenter extends Presenter implements
		ScanProteinPageView.Presenter {
	private ScanProteinPageView view;

	public ScanProteinPagePresenter(ScanProteinPageView view,
			ScanProteinPageState state) {
		this.view = view;
		view.setState(state);
	}

	@Override
	public void bind() {
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void onSubmitButtonClicked(LightWeightProtein protein,
			MotifSelection motifSelection, HistogramStringency stringency,
			boolean showDomains, String histogramDataSource,
			String histogramTaxon, DataSource localizationDataSource) {
		if (protein != null) {
			view.showWaitSymbol();
			fireHistoryEvent(view.getState(),
					NavigationEvent.PageId.FEATURE_SCAN_PROTEIN.getId());

			ProteinScanAction action = new ProteinScanAction(protein,
					motifSelection, stringency, showDomains,
					histogramDataSource, histogramTaxon, localizationDataSource);

			dispatch.execute(action, new AsyncCallback<ProteinScanResult>() {
				@Override
				public void onFailure(Throwable caught) {
					view.setSubmitButtonEnabled(true);
					view.hideWaitImage();
					EventBus.instance().fireEvent(
							new MessageEvent(MessageEventPriority.ERROR,
									"Server-side error", this.getClass()
											.toString(), caught));
				}

				@Override
				public void onSuccess(ProteinScanResult result) {
					view.setSubmitButtonEnabled(true);
					view.hideWaitImage();
					view.hideMessage();
					if (result.isSuccess()) {
						EventBus.instance().fireEvent(
								new ProteinScanEvent(result));
					} else {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										result.getFailureMessage(), this.getClass()
												.toString(), null));
					}
				}
			});
		} else {
			view.setSubmitButtonEnabled(false);
			EventBus.instance().fireEvent(
					new MessageEvent(MessageEventPriority.WARNING,
							"Unknown protein selected", this.getClass()
									.toString(), null));
		}
	}
}
