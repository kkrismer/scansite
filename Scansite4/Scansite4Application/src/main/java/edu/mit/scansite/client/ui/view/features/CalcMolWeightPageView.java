package edu.mit.scansite.client.ui.view.features;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.dispatch.features.UtilitiesMwAndPiResult;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.states.CalcMolWeightPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class CalcMolWeightPageView extends PageView implements
		Stateful<CalcMolWeightPageState> {
	public interface Presenter {
		public void onSubmitButtonClicked(LightWeightProtein protein,
				int maxSites);
	}

	public abstract void setPresenter(Presenter presenter);

	public abstract void setResult(UtilitiesMwAndPiResult result);
}
