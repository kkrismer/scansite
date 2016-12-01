package edu.mit.scansite.shared.dispatch.history;

import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Konstantin Krismer
 */
public class StoreHistoryStateResult implements Result {
	private String sessionId;
	private boolean isSuccess = true;
	private String failureMessage;

	public StoreHistoryStateResult() {
	}

	public StoreHistoryStateResult(String sessionId) {
		this.sessionId = sessionId;
	}

	public StoreHistoryStateResult(String failureMessage, boolean isSuccess) {
		this.isSuccess = false; // if failureMessage exists, isSuccess is always false
		this.failureMessage = failureMessage;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public String getFailureMessage() {
		return failureMessage;
	}
}
