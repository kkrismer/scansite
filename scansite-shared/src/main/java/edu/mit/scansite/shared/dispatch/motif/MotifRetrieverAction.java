package edu.mit.scansite.shared.dispatch.motif;

import edu.mit.scansite.shared.transferobjects.MotifClass;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifRetrieverAction implements Action<MotifRetrieverResult> {

	private String motifShortName;
	private String userSessionId = "";
	private MotifClass motifClass = MotifClass.MAMMALIAN;

	public MotifRetrieverAction() {
	}

	public MotifRetrieverAction(String motifShortName, MotifClass motifClass, String userSessionId) {
		this.motifShortName = motifShortName;
		this.userSessionId = userSessionId;
		this.motifClass = motifClass;
	}

	public String getMotifShortName() {
		return motifShortName;
	}

	public void setMotifShortName(String motifShortName) {
		this.motifShortName = motifShortName;
	}

	public String getUserSessionId() {
		return userSessionId;
	}

	public MotifClass getMotifClass() {
		return motifClass;
	}

	public void setMotifClass(MotifClass motifClass) {
		this.motifClass = motifClass;
	}
}
