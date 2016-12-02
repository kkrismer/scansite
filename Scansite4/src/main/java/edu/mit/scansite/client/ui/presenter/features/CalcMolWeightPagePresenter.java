package edu.mit.scansite.client.ui.presenter.features;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.features.CalcMolWeightPageView;
import edu.mit.scansite.shared.dispatch.features.UtilitiesMwAndPiAction;
import edu.mit.scansite.shared.dispatch.features.UtilitiesMwAndPiResult;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.states.CalcMolWeightPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class CalcMolWeightPagePresenter extends Presenter implements
		CalcMolWeightPageView.Presenter {
	private CalcMolWeightPageView view;

	public CalcMolWeightPagePresenter(CalcMolWeightPageView view,
			CalcMolWeightPageState state) {
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
	public void onSubmitButtonClicked(LightWeightProtein protein, int maxSites) {
		fireHistoryEvent(view.getState(),
				NavigationEvent.PageId.FEATURE_CALC_MOLWEIGHT.getId());

		dispatch.execute(new UtilitiesMwAndPiAction(protein, maxSites),
				new AsyncCallback<UtilitiesMwAndPiResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										"Server-side error", this.getClass()
												.toString(), caught));
					}

					@Override
					public void onSuccess(UtilitiesMwAndPiResult result) {
						view.hideMessage();
						view.setResult(result);
					}
				});
	}
}
