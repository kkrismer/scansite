package edu.mit.scansite.server.dispatch.handler.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.user.UserRetrieverAction;
import edu.mit.scansite.shared.dispatch.user.UserRetrieverResult;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserRetrieverHandler implements ActionHandler<UserRetrieverAction, UserRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Class<UserRetrieverAction> getActionType() {
		return UserRetrieverAction.class;
	}

	@Override
	public UserRetrieverResult execute(UserRetrieverAction action, ExecutionContext context) throws DispatchException {
		try {
			return new UserRetrieverResult(ServiceLocator.getDaoFactory().getUserDao().getAll(), true);
		} catch (DataAccessException e) {
			logger.error("Error getting user: " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(UserRetrieverAction action, UserRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}

}
