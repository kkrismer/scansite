package edu.mit.scansite.client.ui.view.features;

import java.util.List;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.SequencePattern;
import edu.mit.scansite.shared.transferobjects.states.ScanOrthologsPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class ScanOrthologsPageView extends PageView implements
		Stateful<ScanOrthologsPageState> {
	public interface Presenter {
		public void onSequencePatternSubmitButtonClicked(
				SequencePattern sequencePatterns,
				DataSource orthologyDataSource, LightWeightProtein protein,
				HistogramStringency stringency, int alignmentRadius);

		public void onMotifGroupSubmitButtonClicked(
				LightWeightMotifGroup motifGroup, int sitePosition,
				DataSource orthologyDataSource, LightWeightProtein protein,
				HistogramStringency stringency, int alignmentRadius);
	}

	public abstract void setPresenter(Presenter presenter);

	public abstract void setSubmitButtonEnabled(boolean enabled);

	public abstract void showWaitSymbol();

	public abstract void hideWaitSymbol();

	public abstract void initMotifGroups(List<LightWeightMotifGroup> motifGroups);
}