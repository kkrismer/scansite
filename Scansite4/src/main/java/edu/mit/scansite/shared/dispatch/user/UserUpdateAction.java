package edu.mit.scansite.shared.dispatch.user;

import edu.mit.scansite.shared.transferobjects.User;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserUpdateAction implements Action<UserRetrieverResult> {
  private User user;
  private boolean doPasswordChange = false;
  
  public UserUpdateAction() {
  }
  
  public UserUpdateAction(User user, boolean passwordChange) {
    this.user = user;
    this.doPasswordChange = passwordChange;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  public User getUser() {
    return user;
  }
  
  public void setPasswordChange(boolean passwordChange) {
    this.doPasswordChange = passwordChange;
  }

  public boolean isPasswordChange() {
    return doPasswordChange;
  }
}
