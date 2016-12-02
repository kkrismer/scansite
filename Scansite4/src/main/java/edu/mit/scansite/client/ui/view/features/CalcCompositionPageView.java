package edu.mit.scansite.client.ui.view.features;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.states.ChooseProteinWidgetState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class CalcCompositionPageView extends PageView implements
		Stateful<ChooseProteinWidgetState> {
	public interface Presenter {
		public void onSubmitButtonClicked(LightWeightProtein protein);
	}

	public abstract void setPresenter(Presenter presenter);

	public abstract void setResult(LightWeightProtein protein,
			boolean isSuccess, String errorMessage);
}
