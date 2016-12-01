package edu.mit.scansite.shared.dispatch.user;

import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserDeleteAction implements Action<UserRetrieverResult> {
  private String userEmail;
  
  public UserDeleteAction() {
  }
  
  public UserDeleteAction(String email) {
    this.userEmail = email;
  }
  
  public String getUserEmail() {
    return userEmail;
  }
  
  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }
  
}
