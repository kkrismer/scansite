package edu.mit.scansite.client.ui.view.features;

import java.util.List;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;
import edu.mit.scansite.shared.transferobjects.SequencePattern;
import edu.mit.scansite.shared.transferobjects.states.ScanSeqPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class ScanSeqPageView extends PageView implements
		Stateful<ScanSeqPageState> {
	public interface Presenter {
		public void onSubmitButtonClicked(
				List<SequencePattern> sequencePatterns, DataSource dataSource,
				RestrictionProperties properties,
				boolean limitResultsToPhosphorylatedProteins);
	}

	public abstract void setPresenter(Presenter presenter);

	public abstract void setSubmitButtonEnabled(boolean enabled);

	public abstract void showWaitSymbol();

	public abstract void hideWaitImage();
}