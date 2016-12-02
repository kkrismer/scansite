package edu.mit.scansite.shared.dispatch.user;

import edu.mit.scansite.shared.transferobjects.User;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserAddAction implements Action<UserRetrieverResult> {
  private User user;
  
  public UserAddAction() {
  }
  
  public UserAddAction(User user) {
    this.user = user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  public User getUser() {
    return user;
  }
}
