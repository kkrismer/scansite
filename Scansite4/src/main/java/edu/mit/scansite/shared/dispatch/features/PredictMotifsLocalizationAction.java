package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.MotifClass;

/**
 * @author Konstantin Krismer
 */
public class PredictMotifsLocalizationAction implements
		Action<PredictLocalizationResult> {

	private DataSource localizationDataSource;
	private MotifClass motifClass;

	public PredictMotifsLocalizationAction() {
	}

	public PredictMotifsLocalizationAction(DataSource localizationDataSource,
			MotifClass motifClass) {
		super();
		this.localizationDataSource = localizationDataSource;
		this.motifClass = motifClass;
	}

	public DataSource getLocalizationDataSource() {
		return localizationDataSource;
	}

	public void setLocalizationDataSource(DataSource localizationDataSource) {
		this.localizationDataSource = localizationDataSource;
	}

	public MotifClass getMotifClass() {
		return motifClass;
	}

	public void setMotifClass(MotifClass motifClass) {
		this.motifClass = motifClass;
	}
}
