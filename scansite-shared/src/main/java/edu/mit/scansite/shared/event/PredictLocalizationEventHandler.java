package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.EventHandler;

import edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult;

/**
 * @author Konstantin Krismer
 */
public interface PredictLocalizationEventHandler extends EventHandler {
	void onPredictLocalizationEvent(PredictLocalizationResult result);
}
