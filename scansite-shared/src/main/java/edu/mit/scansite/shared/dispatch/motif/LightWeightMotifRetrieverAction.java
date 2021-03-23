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
	private boolean onlyUserMotifs = false;

	public LightWeightMotifRetrieverAction() {
	}

	public LightWeightMotifRetrieverAction(MotifClass motifClass,
			String userSessionId) {
		this.userSessionId = userSessionId;
		this.motifClass = motifClass;
	}

	public LightWeightMotifRetrieverAction(MotifClass motifClass,
			String userSessionId, boolean onlyUserMotifs) {
		this.userSessionId = userSessionId;
		this.motifClass = motifClass;
		this.onlyUserMotifs = onlyUserMotifs;
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

	public boolean getOnlyUserMotifs() {
		return onlyUserMotifs;
	}

	public void setOnlyUserMotifs(boolean onlyUserMotifs) {
		this.onlyUserMotifs = onlyUserMotifs;
	}
}
