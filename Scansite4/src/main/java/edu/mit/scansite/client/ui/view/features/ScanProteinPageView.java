package edu.mit.scansite.client.ui.view.features;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.states.ScanProteinPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class ScanProteinPageView extends PageView implements
		Stateful<ScanProteinPageState> {
	public interface Presenter {
		public void onSubmitButtonClicked(LightWeightProtein protein,
				MotifSelection motifSelection, HistogramStringency stringency,
				boolean showDomains, boolean previouslyMappedSitesOnly, String histogramDataSource,
				String histogramTaxon, DataSource localizationDataSource);
	}

	public abstract void setPresenter(Presenter presenter);

	public abstract void setSubmitButtonEnabled(boolean enabled);

	public abstract void showWaitSymbol();

	public abstract void hideWaitImage();
}
