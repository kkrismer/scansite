package edu.mit.scansite.server.dispatch.handler.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.UserDao;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.user.UserRetrieverResult;
import edu.mit.scansite.shared.dispatch.user.UserUpdateAction;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserUpdateHandler implements ActionHandler<UserUpdateAction, UserRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Class<UserUpdateAction> getActionType() {
		return UserUpdateAction.class;
	}

	@Override
	public UserRetrieverResult execute(UserUpdateAction action, ExecutionContext context) throws DispatchException {
		UserRetrieverResult result = new UserRetrieverResult();
		try {
			UserDao userDao = ServiceLocator.getDaoFactory().getUserDao();
			result.setSuccess(userDao.update(action.getUser(), action.isPasswordChange()));
			result.setUsers(userDao.getAll());
		} catch (DataAccessException e) {
			logger.error("Error updating user: " + e.toString());
			throw new ActionException(e);
		}
		return result;
	}

	@Override
	public void rollback(UserUpdateAction action, UserRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}

}
