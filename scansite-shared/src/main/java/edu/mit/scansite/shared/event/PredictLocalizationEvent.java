package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.GwtEvent;

import edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult;

/**
 * @author Konstantin Krismer
 */
public class PredictLocalizationEvent extends GwtEvent<PredictLocalizationEventHandler> {
	public static Type<PredictLocalizationEventHandler> TYPE = new Type<PredictLocalizationEventHandler>();

	private PredictLocalizationResult result;

	public PredictLocalizationEvent(PredictLocalizationResult result) {
		this.result = result;
	}

	@Override
	public Type<PredictLocalizationEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PredictLocalizationEventHandler handler) {
		handler.onPredictLocalizationEvent(result);
	}
}
