package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;

/**
 * @author Konstantin Krismer
 */
public class PredictProteinsLocalizationAction implements
		Action<PredictLocalizationResult> {

	private DataSource localizationDataSource;
	private LightWeightProtein protein;

	public PredictProteinsLocalizationAction() {
	}

	public PredictProteinsLocalizationAction(DataSource localizationDataSource,
			LightWeightProtein protein) {
		this.localizationDataSource = localizationDataSource;
		this.protein = protein;
	}

	public LightWeightProtein getProtein() {
		return protein;
	}

	public void setProtein(LightWeightProtein protein) {
		this.protein = protein;
	}

	public DataSource getLocalizationDataSource() {
		return localizationDataSource;
	}

	public void setLocalizationDataSource(DataSource localizationDataSource) {
		this.localizationDataSource = localizationDataSource;
	}
}
