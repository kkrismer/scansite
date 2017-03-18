package edu.mit.scansite.shared.dispatch.features;

import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Localization;

/**
 * @author Konstantin Krismer
 */
public class PredictProteinsLocalizationResult extends
		PredictLocalizationResult {
	private LightWeightProtein protein;
	private Localization localization;

	public PredictProteinsLocalizationResult() {
		super();
	}

	public PredictProteinsLocalizationResult(DataSource localizationDataSource,
			LightWeightProtein protein, int totalProteinLocalizations,
			Localization localization) {
		super(localizationDataSource, totalProteinLocalizations);
		this.protein = protein;
		this.localization = localization;
	}

	public PredictProteinsLocalizationResult(String errorMessage) {
		super(errorMessage);
	}

	public LightWeightProtein getProtein() {
		return protein;
	}

	public void setProtein(LightWeightProtein protein) {
		this.protein = protein;
	}

	public Localization getLocalization() {
		return localization;
	}

	public void setLocalization(Localization localization) {
		this.localization = localization;
	}
}
