package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Konstantin Krismer
 */
public class PredictLocalizationMotifProteinPairAction implements
		Action<PredictLocalizationResult> {
	private DataSource localizationDataSource;
	private Motif motif;
	private LightWeightProtein protein;

	public PredictLocalizationMotifProteinPairAction() {

	}

	public PredictLocalizationMotifProteinPairAction(
			DataSource localizationDataSource, Motif motif,
			LightWeightProtein protein) {
		super();
		this.localizationDataSource = localizationDataSource;
		this.motif = motif;
		this.protein = protein;
	}

	public DataSource getLocalizationDataSource() {
		return localizationDataSource;
	}

	public void setLocalizationDataSource(DataSource localizationDataSource) {
		this.localizationDataSource = localizationDataSource;
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
}
