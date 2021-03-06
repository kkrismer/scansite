package edu.mit.scansite.client.ui.presenter.features;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.features.PredictLocalizationPageView;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult;
import edu.mit.scansite.shared.dispatch.features.PredictMotifsLocalizationAction;
import edu.mit.scansite.shared.dispatch.features.PredictProteinsLocalizationAction;
import edu.mit.scansite.shared.event.EventBus;
import edu.mit.scansite.shared.event.MessageEvent;
import edu.mit.scansite.shared.event.NavigationEvent;
import edu.mit.scansite.shared.event.PredictLocalizationEvent;
import edu.mit.scansite.shared.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.transferobjects.states.PredictLocalizationPageState;

/**
 * @author Konstantin Krismer
 */
public class PredictLocalizationPagePresenter extends Presenter implements
		PredictLocalizationPageView.Presenter {
	private PredictLocalizationPageView view;
	private final User user;

	public PredictLocalizationPagePresenter(PredictLocalizationPageView view,
			PredictLocalizationPageState state, User user) {
		this.view = view;
		view.setState(state);
		this.user = user;
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
	public void onSubmitButtonClicked(DataSource localizationDataSource,
			LightWeightProtein protein) {
		view.showWaitSymbol();
		fireHistoryEvent(view.getState(),
				NavigationEvent.PageId.FEATURE_PREDICT_LOCALIZATION.getId());

		PredictProteinsLocalizationAction action = new PredictProteinsLocalizationAction(
				localizationDataSource, protein);
		dispatch.execute(action,
				new AsyncCallback<PredictLocalizationResult>() {
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
					public void onSuccess(PredictLocalizationResult result) {
						view.setSubmitButtonEnabled(true);
						view.hideWaitSymbol();
						view.hideMessage();
						if (result.isSuccess()) {
							EventBus.instance().fireEvent(
									new PredictLocalizationEvent(result));
						} else {
							EventBus.instance().fireEvent(
									new MessageEvent(
											MessageEventPriority.ERROR, result
													.getErrorMessage(), this
													.getClass().toString(),
											null));
						}
					}
				});
	}

	@Override
	public void onSubmitButtonClicked(DataSource localizationDataSource,
			MotifClass motifClass) {
		view.showWaitSymbol();
		fireHistoryEvent(view.getState(),
				NavigationEvent.PageId.FEATURE_PREDICT_LOCALIZATION.getId());

		PredictMotifsLocalizationAction action = new PredictMotifsLocalizationAction(
				localizationDataSource, motifClass, user == null ? "" : user.getSessionId());
		dispatch.execute(action,
				new AsyncCallback<PredictLocalizationResult>() {
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
					public void onSuccess(PredictLocalizationResult result) {
						view.setSubmitButtonEnabled(true);
						view.hideWaitSymbol();
						view.hideMessage();
						if (result.isSuccess()) {
							EventBus.instance().fireEvent(
									new PredictLocalizationEvent(result));
						} else {
							EventBus.instance().fireEvent(
									new MessageEvent(
											MessageEventPriority.ERROR, result
													.getErrorMessage(), this
													.getClass().toString(),
											null));
						}
					}
				});
	}
}
