package edu.mit.scansite.shared.dispatch.features;

import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Localization;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Konstantin Krismer
 */
public class PredictLocalizationMotifProteinPairResult extends
		PredictLocalizationResult {
	private Motif motif;
	private LightWeightProtein protein;
	private Localization motifLocalization;
	private Localization proteinLocalization;

	public PredictLocalizationMotifProteinPairResult() {
		super();
	}

	public PredictLocalizationMotifProteinPairResult(
			DataSource localizationDataSource, int totalProteinLocalizations,
			Motif motif, LightWeightProtein protein,
			Localization motifLocalization, Localization proteinLocalization) {
		super(localizationDataSource, totalProteinLocalizations);
		this.motif = motif;
		this.protein = protein;
		this.motifLocalization = motifLocalization;
		this.proteinLocalization = proteinLocalization;
	}

	public Motif getMotif() {
		return motif;
	}

	public void setMotif(Motif motif) {
		this.motif = motif;
	}

	public LightWeightProtein getProtein() {
		return protein;
	}

	public void setProtein(LightWeightProtein protein) {
		this.protein = protein;
	}

	public Localization getMotifLocalization() {
		return motifLocalization;
	}

	public void setMotifLocalization(Localization motifLocalization) {
		this.motifLocalization = motifLocalization;
	}

	public Localization getProteinLocalization() {
		return proteinLocalization;
	}

	public void setProteinLocalization(Localization proteinLocalization) {
		this.proteinLocalization = proteinLocalization;
	}
}
