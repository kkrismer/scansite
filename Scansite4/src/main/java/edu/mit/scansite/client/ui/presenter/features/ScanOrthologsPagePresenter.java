package edu.mit.scansite.client.ui.presenter.features;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.event.OrthologScanMotifEvent;
import edu.mit.scansite.client.ui.event.OrthologScanSequencePatternEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.features.ScanOrthologsPageView;
import edu.mit.scansite.shared.dispatch.features.OrthologScanMotifAction;
import edu.mit.scansite.shared.dispatch.features.OrthologScanMotifResult;
import edu.mit.scansite.shared.dispatch.features.OrthologScanSequencePatternAction;
import edu.mit.scansite.shared.dispatch.features.OrthologScanSequencePatternResult;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifGroupRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifGroupRetrieverResult;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.SequencePattern;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.transferobjects.states.ScanOrthologsPageState;

/**
 * @author Konstantin Krismer
 */
public class ScanOrthologsPagePresenter extends Presenter implements
		ScanOrthologsPageView.Presenter {
	private ScanOrthologsPageView view;
	private User user;

	public ScanOrthologsPagePresenter(ScanOrthologsPageView view,
			ScanOrthologsPageState state, User user) {
		this.view = view;
		this.user = user;
		view.setState(state);
	}

	@Override
	public void go(HasWidgets container) {
		initMotifGroups();
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void onSequencePatternSubmitButtonClicked(
			SequencePattern sequencePatterns, DataSource orthologyDataSource,
			LightWeightProtein protein, HistogramStringency stringency,
			int alignmentRadius) {
		if (orthologyDataSource != null) {
			if (protein != null) {
				view.showWaitSymbol();
				fireHistoryEvent(view.getState(),
						NavigationEvent.PageId.FEATURE_SCAN_ORTHOLOGS.getId());

				OrthologScanSequencePatternAction action = new OrthologScanSequencePatternAction(
						sequencePatterns, orthologyDataSource, protein,
						stringency, alignmentRadius,
						user != null ? user.getSessionId() : "");
				dispatch.execute(action,
						new AsyncCallback<OrthologScanSequencePatternResult>() {
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
							public void onSuccess(
									OrthologScanSequencePatternResult result) {
								view.hideWaitImage();
								view.setSubmitButtonEnabled(true);
								if (result.isSuccess()) {
									view.hideMessage();
									EventBus.instance()
											.fireEvent(
													new OrthologScanSequencePatternEvent(
															result));
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
		} else {
			view.setSubmitButtonEnabled(false);
			EventBus.instance().fireEvent(
					new MessageEvent(MessageEventPriority.WARNING,
							"No orthology data source available", this.getClass()
									.toString(), null));
		}
	}

	@Override
	public void onMotifGroupSubmitButtonClicked(
			LightWeightMotifGroup motifGroup, int sitePosition,
			DataSource orthologyDataSource, LightWeightProtein protein,
			HistogramStringency stringency, int alignmentRadius) {
		view.showWaitSymbol();
		fireHistoryEvent(view.getState(),
				NavigationEvent.PageId.FEATURE_SCAN_ORTHOLOGS.getId());

		OrthologScanMotifAction action = new OrthologScanMotifAction(
				motifGroup, sitePosition, orthologyDataSource, protein,
				stringency, alignmentRadius, user != null ? user.getSessionId()
						: "");
		dispatch.execute(action, new AsyncCallback<OrthologScanMotifResult>() {
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
			public void onSuccess(OrthologScanMotifResult result) {
				view.setSubmitButtonEnabled(true);
				view.hideWaitImage();
				if (result.isSuccess()) {
					view.hideMessage();
					EventBus.instance().fireEvent(
							new OrthologScanMotifEvent(result));
				} else {
					EventBus.instance().fireEvent(
							new MessageEvent(MessageEventPriority.ERROR,
									result.getFailureMessage(), this.getClass()
											.toString(), null));
				}
			}
		});
	}

	public void initMotifGroups() {
		dispatch.execute(new LightWeightMotifGroupRetrieverAction(),
				new AsyncCallback<LightWeightMotifGroupRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						// view.disable();
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										"Error: motif groups not available", this.getClass()
												.toString(), caught));
					}

					@Override
					public void onSuccess(
							LightWeightMotifGroupRetrieverResult result) {
						view.initMotifGroups(result.getMotifGroups());
						view.hideMessage();
					}
				});
	}

	@Override
	public void bind() {
	}
}
