package edu.mit.scansite.shared.dispatch.motif;

import edu.mit.scansite.shared.transferobjects.MotifClass;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifDeleteAction implements
		Action<LightWeightMotifRetrieverResult> {
	
	private String userSessionId = "";
	private int motifId;
	private MotifClass motifClass = MotifClass.MAMMALIAN;

	public MotifDeleteAction() {

	}

	public MotifDeleteAction(int motifId, MotifClass motifClass,
			String userSessionId) {
		this.userSessionId = userSessionId;
		this.motifId = motifId;
		this.motifClass = motifClass;
	}

	public String getUserSessionId() {
		return userSessionId;
	}

	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}

	public void setMotifId(int motifId) {
		this.motifId = motifId;
	}

	public int getMotifId() {
		return motifId;
	}

	public MotifClass getMotifClass() {
		return motifClass;
	}

	public void setMotifClass(MotifClass motifClass) {
		this.motifClass = motifClass;
	}
}
