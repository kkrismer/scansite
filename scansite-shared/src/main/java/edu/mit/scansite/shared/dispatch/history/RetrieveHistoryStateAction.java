package edu.mit.scansite.shared.dispatch.history;

import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Konstantin Krismer
 */
public class RetrieveHistoryStateAction implements
		Action<RetrieveHistoryStateResult> {
	private String sessionId;

	public RetrieveHistoryStateAction() {
	}

	public RetrieveHistoryStateAction(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}