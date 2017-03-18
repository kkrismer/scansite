package edu.mit.scansite.shared.dispatch.motif;

import edu.mit.scansite.shared.transferobjects.MotifClass;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class LightWeightMotifRetrieverAction implements
		Action<LightWeightMotifRetrieverResult> {

	private String userSessionId = "";
	private MotifClass motifClass = MotifClass.MAMMALIAN;

	public LightWeightMotifRetrieverAction() {
	}

	public LightWeightMotifRetrieverAction(MotifClass motifClass,
			String userSessionId) {
		this.userSessionId = userSessionId;
		this.motifClass = motifClass;
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
