package edu.mit.scansite.shared.dispatch.motif;

import edu.mit.scansite.shared.transferobjects.Motif;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifUpdateAction implements Action<LightWeightMotifRetrieverResult> {

	private Motif motif;
	private String userSessionId = "";

	public MotifUpdateAction() {
	}

	public MotifUpdateAction(Motif motif, String userSessionId) {
		this.motif = motif;
	}

	public Motif getMotif() {
		return motif;
	}

	public void setMotif(Motif motif) {
		this.motif = motif;
	}

	public String getUserSessionId() {
		return userSessionId;
	}

	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}
}
