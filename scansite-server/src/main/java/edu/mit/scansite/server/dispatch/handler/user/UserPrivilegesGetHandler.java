package edu.mit.scansite.server.dispatch.handler.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.user.UserPrivilegesAction;
import edu.mit.scansite.shared.dispatch.user.UserPrivilegesResult;
import edu.mit.scansite.shared.transferobjects.User;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserPrivilegesGetHandler implements ActionHandler<UserPrivilegesAction, UserPrivilegesResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Class<UserPrivilegesAction> getActionType() {
		return UserPrivilegesAction.class;
	}

	@Override
	public UserPrivilegesResult execute(UserPrivilegesAction action, ExecutionContext context)
			throws DispatchException {
		try {
			User user = ServiceLocator.getDaoFactory().getUserDao().get(action.getUserEmail());
			if (user != null) {
				return new UserPrivilegesResult(user.isCollaborator(), user.isAdmin());
			} else {
				return new UserPrivilegesResult(false, false);
			}
		} catch (DataAccessException e) {
			logger.error("Error getting user privileges for user " + action.getUserEmail() + ": " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(UserPrivilegesAction action, UserPrivilegesResult result, ExecutionContext context)
			throws DispatchException {
	}

}
