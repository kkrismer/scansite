package edu.mit.scansite.server.dispatch.handler.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.UserDao;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.user.UserAddAction;
import edu.mit.scansite.shared.dispatch.user.UserRetrieverResult;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserAddHandler implements ActionHandler<UserAddAction, UserRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Class<UserAddAction> getActionType() {
		return UserAddAction.class;
	}

	@Override
	public UserRetrieverResult execute(UserAddAction action, ExecutionContext context) throws DispatchException {
		try {
			UserDao userDao = ServiceLocator.getDaoFactory().getUserDao();
			userDao.add(action.getUser());
			return new UserRetrieverResult(userDao.getAll(), true);
		} catch (DataAccessException e) {
			logger.error("Error adding user: " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(UserAddAction action, UserRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
