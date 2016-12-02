package edu.mit.scansite.shared.dispatch.user;

import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserPrivilegesAction implements Action<UserPrivilegesResult> {
  private String userEmail;
  
  public UserPrivilegesAction() {
  }
  
  public UserPrivilegesAction(String email) {
    this.userEmail = email;
  }
  
  public String getUserEmail() {
    return userEmail;
  }
  
  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }
  
}
