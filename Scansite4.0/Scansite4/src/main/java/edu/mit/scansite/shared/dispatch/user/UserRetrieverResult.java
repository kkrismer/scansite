package edu.mit.scansite.shared.dispatch.user;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserRetrieverResult implements Result {
	private boolean isSuccess = false;
	private List<User> users;

	public UserRetrieverResult() {
	}

	public UserRetrieverResult(List<User> users, boolean isSuccess) {
		this.users = users;
		this.isSuccess = isSuccess;
	}

	public UserRetrieverResult(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}
