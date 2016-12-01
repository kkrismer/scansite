package edu.mit.scansite.client.ui.view.features;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;
import edu.mit.scansite.shared.transferobjects.states.ScanDatabasePageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class ScanDatabasePageView extends PageView implements
		Stateful<ScanDatabasePageState> {
	public interface Presenter {
		public void onSubmitButtonClicked(MotifSelection motifSelection,
				DataSource dataSource,
				RestrictionProperties restrictionProperties, int outputListSize);
	}

	public abstract void setPresenter(Presenter presenter);

	public abstract void setSubmitButtonEnabled(boolean enabled);

	public abstract void showWaitSymbol();

	public abstract void hideWaitImage();
}
