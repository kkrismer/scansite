package edu.mit.scansite.shared.dispatch.history;

import edu.mit.scansite.shared.transferobjects.states.State;
import net.customware.gwt.dispatch.shared.Action;

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