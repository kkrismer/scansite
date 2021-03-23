package edu.mit.scansite.shared.dispatch.history;

import edu.mit.scansite.shared.transferobjects.states.State;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Konstantin Krismer
 */
public class RetrieveHistoryStateResult implements Result {
	private State sessionData;
	private boolean isSuccess = true;
	private String failureMessage;

	public RetrieveHistoryStateResult() {
	}

	public RetrieveHistoryStateResult(State sessionData) {
		this.sessionData = sessionData;
	}

	public RetrieveHistoryStateResult(String failureMessage, boolean isSuccess) {
		this.isSuccess = false; // if failureMessage exists, isSuccess is always
								// false
		this.failureMessage = failureMessage;
	}

	public State getSessionData() {
		return sessionData;
	}

	public void setSessionData(State sessionData) {
		this.sessionData = sessionData;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public String getFailureMessage() {
		return failureMessage;
	}
}
