package edu.mit.scansite.client.ui.view.features;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult;
import edu.mit.scansite.shared.transferobjects.states.PredictLocalizationResultPageState;

/**
 * @author Konstantin Krismer
 */
public abstract class PredictLocalizationResultPageView extends PageView
		implements Stateful<PredictLocalizationResultPageState> {
	public abstract void setResult(PredictLocalizationResult result);
}
