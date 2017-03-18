package edu.mit.scansite.shared.dispatch.motif;

import edu.mit.scansite.shared.transferobjects.MotifClass;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupRetrieverAction implements
		Action<MotifGroupRetrieverResult> {
	private MotifClass motifClass = null;
	private String userSessionId = "";

	public MotifGroupRetrieverAction() {

	}

	public MotifGroupRetrieverAction(MotifClass motifClass, String userSessionId) {
		this.motifClass = motifClass;
		this.userSessionId = userSessionId;
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