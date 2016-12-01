package edu.mit.scansite.server.dispatch.handler.user;

import javax.servlet.ServletContext;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.UserDao;
import edu.mit.scansite.server.dispatch.BootstrapListener;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.user.UserRetrieverResult;
import edu.mit.scansite.shared.dispatch.user.UserUpdateAction;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserUpdateHandler implements
		ActionHandler<UserUpdateAction, UserRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public UserUpdateHandler(final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<UserUpdateAction> getActionType() {
		return UserUpdateAction.class;
	}

	@Override
	public UserRetrieverResult execute(UserUpdateAction action,
			ExecutionContext context) throws DispatchException {
		UserRetrieverResult result = new UserRetrieverResult();
		try {
			UserDao userDao = ServiceLocator
					.getInstance()
					.getDaoFactory(
							BootstrapListener.getDbConnector(contextProvider
									.get())).getUserDao();
			result.setSuccess(userDao.update(action.getUser(),
					action.isPasswordChange()));
			result.setUsers(userDao.getAll());
		} catch (DataAccessException e) {
			logger.error("Error updating user: " + e.toString());
			throw new ActionException(e);
		}
		return result;
	}

	@Override
	public void rollback(UserUpdateAction action, UserRetrieverResult result,
			ExecutionContext context) throws DispatchException {
	}

}
