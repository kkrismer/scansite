package edu.mit.scansite.client.ui.presenter.features;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.features.CalcCompositionPageView;
import edu.mit.scansite.shared.dispatch.features.ProteinRetrieverAction;
import edu.mit.scansite.shared.dispatch.features.ProteinRetrieverResult;
import edu.mit.scansite.shared.event.EventBus;
import edu.mit.scansite.shared.event.MessageEvent;
import edu.mit.scansite.shared.event.NavigationEvent;
import edu.mit.scansite.shared.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.states.ChooseProteinWidgetState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class CalcCompositionPagePresenter extends Presenter implements
		CalcCompositionPageView.Presenter {
	private CalcCompositionPageView view;

	public CalcCompositionPagePresenter(CalcCompositionPageView view,
			ChooseProteinWidgetState state) {
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
	public void onSubmitButtonClicked(LightWeightProtein protein) {
		if (protein != null && protein.getIdentifier() != null
				&& protein.getDataSource() != null) {
			fireHistoryEvent(view.getState(),
					NavigationEvent.PageId.FEATURE_CALC_COMPOSITION.getId());

			dispatch.execute(new ProteinRetrieverAction(
					protein.getIdentifier(), protein.getDataSource()),
					new AsyncCallback<ProteinRetrieverResult>() {
						@Override
						public void onFailure(Throwable caught) {
							EventBus.instance().fireEvent(
									new MessageEvent(
											MessageEventPriority.ERROR,
											"Server-side error", this
													.getClass().toString(),
											caught));
						}

						@Override
						public void onSuccess(ProteinRetrieverResult result) {
							if (result.isSuccess()) {
								view.hideMessage();
								view.setResult(result.getProtein(),
										result.isSuccess(),
										result.getErrorMessage());
							} else {
								EventBus.instance().fireEvent(
										new MessageEvent(
												MessageEventPriority.ERROR,
												result.getErrorMessage(), this
														.getClass().toString(),
												null));
							}
						}
					});
		} else if (protein != null && protein.getSequence() != null) {
			fireHistoryEvent(view.getState(),
					NavigationEvent.PageId.FEATURE_CALC_COMPOSITION.getId());
			view.hideMessage();
			view.setResult(protein, true, "");
		} else {
			view.setResult(protein, false, "no protein selected");
		}
	}
}
