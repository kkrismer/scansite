package edu.mit.scansite.shared.dispatch.user;

import edu.mit.scansite.shared.transferobjects.User;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class LoginResult implements Result {
	private boolean loginSuccessful = false;
	private User user;

	public LoginResult() {
	}

	public LoginResult(boolean loginSuccessful, User user) {
		this.loginSuccessful = loginSuccessful;
		this.user = user;
	}

	public boolean isLoginSuccessful() {
		return loginSuccessful;
	}

	public void setLoginSuccessful(boolean loginSuccessful) {
		this.loginSuccessful = loginSuccessful;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
