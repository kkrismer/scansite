package edu.mit.scansite.shared.transferobjects.states;

import edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult;

/**
 * @author Konstantin Krismer
 */
public class PredictLocalizationResultPageState extends State {
	private PredictLocalizationResult predictLocalizationResult;

	public PredictLocalizationResultPageState() {

	}

	public PredictLocalizationResultPageState(
			PredictLocalizationResult predictLocalizationResult) {
		this.predictLocalizationResult = predictLocalizationResult;
	}

	public PredictLocalizationResult getPredictLocalizationResult() {
		return predictLocalizationResult;
	}

	public void setPredictLocalizationResult(
			PredictLocalizationResult predictLocalizationResult) {
		this.predictLocalizationResult = predictLocalizationResult;
	}
}
