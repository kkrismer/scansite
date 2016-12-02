package edu.mit.scansite.server.dispatch.handler.history;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.shared.dispatch.history.RetrieveHistoryStateAction;
import edu.mit.scansite.shared.dispatch.history.RetrieveHistoryStateResult;
import edu.mit.scansite.shared.transferobjects.states.State;

/**
 * @author Konstantin Krismer
 */
public class RetrieveHistoryStateHandler implements
		ActionHandler<RetrieveHistoryStateAction, RetrieveHistoryStateResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<HttpSession> sessionProvider;

	@Inject
	public RetrieveHistoryStateHandler(
			final Provider<HttpSession> sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	@Override
	public Class<RetrieveHistoryStateAction> getActionType() {
		return RetrieveHistoryStateAction.class;
	}

	@Override
	public RetrieveHistoryStateResult execute(
			RetrieveHistoryStateAction action, ExecutionContext context)
			throws DispatchException {
		try {
			if (action.getSessionId() != null
					&& !action.getSessionId().isEmpty()) {
				State sessionData = (State) sessionProvider
						.get().getAttribute(action.getSessionId());
				if (sessionData != null) {
					return new RetrieveHistoryStateResult(sessionData);
				} else {
					return new RetrieveHistoryStateResult(
							"session id is invalid", false);
				}

			} else {
				return new RetrieveHistoryStateResult("session id is null",
						false);
			}
		} catch (Exception e) {
			logger.error("Error running database search: " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(RetrieveHistoryStateAction action,
			RetrieveHistoryStateResult result, ExecutionContext context)
			throws DispatchException {
	}
}
