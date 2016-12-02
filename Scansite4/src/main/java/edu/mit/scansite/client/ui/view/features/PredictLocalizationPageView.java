package edu.mit.scansite.client.ui.view.features;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.states.PredictLocalizationPageState;

/**
 * @author Konstantin Krismer
 */
public abstract class PredictLocalizationPageView extends PageView implements
		Stateful<PredictLocalizationPageState> {
	public interface Presenter {
		public void onSubmitButtonClicked(DataSource localizationDataSource,
				LightWeightProtein protein);

		public void onSubmitButtonClicked(DataSource localizationDataSource,
				MotifClass motifClass);
	}

	public abstract void setPresenter(Presenter presenter);

	public abstract void setSubmitButtonEnabled(boolean enabled);

	public abstract void showWaitSymbol();

	public abstract void hideWaitImage();
}
