package edu.mit.scansite.shared.dispatch.features;

import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Konstantin Krismer
 */
public class PredictMotifsLocalizationAction implements Action<PredictLocalizationResult> {

	private String userSessionId = "";
	private DataSource localizationDataSource;
	private MotifClass motifClass;

	public PredictMotifsLocalizationAction() {
	}

	public PredictMotifsLocalizationAction(DataSource localizationDataSource, MotifClass motifClass,
			String userSessionId) {
		super();
		this.localizationDataSource = localizationDataSource;
		this.motifClass = motifClass;
		this.userSessionId = userSessionId;
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

	public String getUserSessionId() {
		return userSessionId;
	}

	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}
}
