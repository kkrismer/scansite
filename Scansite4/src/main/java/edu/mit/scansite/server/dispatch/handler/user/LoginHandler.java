package edu.mit.scansite.server.dispatch.handler.user;

import java.util.ArrayList;

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
import edu.mit.scansite.shared.dispatch.user.LoginAction;
import edu.mit.scansite.shared.dispatch.user.LoginResult;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class LoginHandler implements ActionHandler<LoginAction, LoginResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public LoginHandler(final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<LoginAction> getActionType() {
		return LoginAction.class;
	}

	@Override
	public LoginResult execute(LoginAction action, ExecutionContext context)
			throws DispatchException {
		try {
			return tryLogin(action);
		} catch (DataAccessException e) {
			logger.error("Error getting login information: " + e.toString());
			throw new ActionException(e);
		}
	}

	public boolean isSessionValidLogin(String userSessionId) {
		try {
			if (userSessionId == null) {
				return false;
			} else {
				return getUserBySessionId(userSessionId) != null;
			}
		} catch (DataAccessException e) {
			return false;
		}
	}

	private LoginResult tryLogin(LoginAction action) throws DataAccessException {
		User user;
		String sessionId = action.getSessionId();
		if (sessionId == null) {
			user = ServiceLocator.getDaoFactory().getUserDao()
					.get(action.getUserEmail(), action.getUserPassword());
		} else {
			user = getUserBySessionId(action.getSessionId());
		}
		LoginResult result = new LoginResult();
		if (user != null) {
			result.setLoginSuccessful(true);
			result.setUser(user);
		}
		return result;
	}

	@Override
	public void rollback(LoginAction action, LoginResult result,
			ExecutionContext context) throws DispatchException {
	}

	public User getUserBySessionId(String sessionId) throws DataAccessException {
		ArrayList<User> all = getAll();
		for (User u : all) {
			String uSessionId = u.getSessionId();
			if (uSessionId.equals(sessionId)) {
				return u;
			}
		}
		return null;
	}

	public ArrayList<User> getAll() throws DataAccessException {
		return ServiceLocator.getDaoFactory().getUserDao().getAll();
	}
}
