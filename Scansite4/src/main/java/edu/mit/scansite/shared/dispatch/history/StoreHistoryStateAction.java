package edu.mit.scansite.shared.dispatch.history;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.states.State;

/**
 * @author Konstantin Krismer
 */
public class StoreHistoryStateAction implements Action<StoreHistoryStateResult> {
	private State sessionData;

	public StoreHistoryStateAction() {
	}

	public StoreHistoryStateAction(State sessionData) {
		this.sessionData = sessionData;
	}

	public State getSessionData() {
		return sessionData;
	}

	public void setSessionData(State sessionData) {
		this.sessionData = sessionData;
	}
}