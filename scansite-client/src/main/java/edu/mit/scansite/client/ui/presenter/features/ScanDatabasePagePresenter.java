package edu.mit.scansite.client.ui.presenter.features;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.features.ScanDatabasePageView;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanAction;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanResult;
import edu.mit.scansite.shared.event.DatabaseScanEvent;
import edu.mit.scansite.shared.event.EventBus;
import edu.mit.scansite.shared.event.MessageEvent;
import edu.mit.scansite.shared.event.NavigationEvent;
import edu.mit.scansite.shared.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.transferobjects.states.ScanDatabasePageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ScanDatabasePagePresenter extends Presenter implements
		ScanDatabasePageView.Presenter {
	private ScanDatabasePageView view;
	private final User user;

	public ScanDatabasePagePresenter(ScanDatabasePageView view,
			ScanDatabasePageState state, User user) {
		this.view = view;
		this.user = user;
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
	public void onSubmitButtonClicked(MotifSelection motifSelection,
			DataSource dataSource, RestrictionProperties restrictionProperties,
			int outputListSize, boolean previouslyMappedSitesOnly) {
		view.showWaitSymbol();
		fireHistoryEvent(view.getState(),
				NavigationEvent.PageId.FEATURE_SCAN_DB.getId());

		DatabaseScanAction action = new DatabaseScanAction(motifSelection,
				dataSource, restrictionProperties, outputListSize, previouslyMappedSitesOnly,
				user == null ? "" : user.getSessionId());
		dispatch.execute(action, new AsyncCallback<DatabaseScanResult>() {
			@Override
			public void onFailure(Throwable caught) {
				view.setSubmitButtonEnabled(true);
				view.hideWaitSymbol();
				EventBus.instance().fireEvent(
						new MessageEvent(MessageEventPriority.ERROR,
								"Server-side error", this.getClass()
										.toString(), caught));
			}

			@Override
			public void onSuccess(DatabaseScanResult result) {
				view.setSubmitButtonEnabled(true);
				view.hideWaitSymbol();
				view.hideMessage();
				if (result.isSuccess()) {
					EventBus.instance()
							.fireEvent(new DatabaseScanEvent(result));
				} else {
					EventBus.instance().fireEvent(
							new MessageEvent(MessageEventPriority.ERROR,
									result.getFailureMessage(), this.getClass()
											.toString(), null));
				}
			}
		});
	}
}
