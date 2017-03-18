package edu.mit.scansite.client.ui.presenter.features;

import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.features.PredictLocalizationResultPageView;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult;
import edu.mit.scansite.shared.dispatch.features.PredictMotifsLocalizationResult;
import edu.mit.scansite.shared.transferobjects.states.PredictLocalizationResultPageState;

/**
 * @author Konstantin Krismer
 */
public class PredictLocalizationResultPagePresenter extends Presenter {
	private PredictLocalizationResultPageView view;
	private PredictLocalizationResult result;

	public PredictLocalizationResultPagePresenter(PredictLocalizationResultPageView view) {
		this.view = view;
		this.result = null;
	}

	public PredictLocalizationResultPagePresenter(PredictLocalizationResultPageView view,
			PredictLocalizationResult result) {
		this.view = view;
		this.result = result;
	}

	public PredictLocalizationResultPagePresenter(PredictLocalizationResultPageView view,
			PredictMotifsLocalizationResult result) {
		this.view = view;
		this.result = result;
	}

	public PredictLocalizationResultPagePresenter(PredictLocalizationResultPageView view,
			PredictLocalizationResultPageState state) {
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
				NavigationEvent.PageId.FEATURE_PREDICT_LOCALIZATION_RESULT.getId());
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
}