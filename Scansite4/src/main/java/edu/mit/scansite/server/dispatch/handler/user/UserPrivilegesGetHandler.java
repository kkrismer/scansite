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
import edu.mit.scansite.server.dispatch.BootstrapListener;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.user.UserPrivilegesAction;
import edu.mit.scansite.shared.dispatch.user.UserPrivilegesResult;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserPrivilegesGetHandler implements
		ActionHandler<UserPrivilegesAction, UserPrivilegesResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public UserPrivilegesGetHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<UserPrivilegesAction> getActionType() {
		return UserPrivilegesAction.class;
	}

	@Override
	public UserPrivilegesResult execute(UserPrivilegesAction action,
			ExecutionContext context) throws DispatchException {
		try {
			User user = ServiceLocator
					.getInstance()
					.getDaoFactory(
							BootstrapListener.getDbConnector(contextProvider
									.get())).getUserDao()
					.get(action.getUserEmail());
			if (user != null) {
				return new UserPrivilegesResult(user.isAdmin(),
						user.isSuperAdmin());
			} else {
				return new UserPrivilegesResult(false, false);
			}
		} catch (DataAccessException e) {
			logger.error("Error getting user privileges for user "
					+ action.getUserEmail() + ": " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(UserPrivilegesAction action,
			UserPrivilegesResult result, ExecutionContext context)
			throws DispatchException {
	}

}
