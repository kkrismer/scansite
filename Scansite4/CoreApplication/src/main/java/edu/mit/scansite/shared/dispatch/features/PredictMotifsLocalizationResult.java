package edu.mit.scansite.shared.dispatch.features;

import java.util.Map;

import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightLocalization;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifClass;

/**
 * @author Konstantin Krismer
 */
public class PredictMotifsLocalizationResult extends PredictLocalizationResult {
	private MotifClass motifClass;
	private Map<Motif, LightWeightLocalization> localizations;

	public PredictMotifsLocalizationResult() {
		super();
	}

	public PredictMotifsLocalizationResult(DataSource localizationDataSource,
			MotifClass motifClass, int totalProteinLocalizations,
			Map<Motif, LightWeightLocalization> localizations,
			boolean isSuccess, String errorMessage) {
		super(localizationDataSource, totalProteinLocalizations);
		this.motifClass = motifClass;
		this.localizations = localizations;
	}

	public PredictMotifsLocalizationResult(String errorMessage) {
		super(errorMessage);
	}

	public MotifClass getMotifClass() {
		return motifClass;
	}

	public void setMotifClass(MotifClass motifClass) {
		this.motifClass = motifClass;
	}

	public Map<Motif, LightWeightLocalization> getLocalizations() {
		return localizations;
	}

	public void setLocalizations(
			Map<Motif, LightWeightLocalization> localizations) {
		this.localizations = localizations;
	}
}
