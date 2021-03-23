package edu.mit.scansite.server.dispatch.handler.history;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.shared.dispatch.history.StoreHistoryStateAction;
import edu.mit.scansite.shared.dispatch.history.StoreHistoryStateResult;

/**
 * @author Konstantin Krismer
 */
public class StoreHistoryStateHandler implements
		ActionHandler<StoreHistoryStateAction, StoreHistoryStateResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<HttpSession> sessionProvider;

	@Inject
	public StoreHistoryStateHandler(
//			final Provider<ServletContext> contextProvider,
			final Provider<HttpSession> sessionProvider) {
//		this.contextProvider = contextProvider;
		this.sessionProvider = sessionProvider;
	}

	@Override
	public Class<StoreHistoryStateAction> getActionType() {
		return StoreHistoryStateAction.class;
	}

	@Override
	public StoreHistoryStateResult execute(StoreHistoryStateAction action,
			ExecutionContext context) throws DispatchException {
		try {
			if (action.getSessionData() != null) {
				String sessionId = UUID.randomUUID().toString();
//				contextProvider.get().setAttribute(sessionId,
//						action.getSessionData());
				sessionProvider.get().setAttribute(sessionId,
						action.getSessionData());
				return new StoreHistoryStateResult(sessionId);
			} else {
				return new StoreHistoryStateResult("session data empty");
			}
		} catch (Exception e) {
			logger.error("Error running database search: " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(StoreHistoryStateAction action,
			StoreHistoryStateResult result, ExecutionContext context)
			throws DispatchException {
	}
}
