package edu.mit.scansite.shared.dispatch.user;

import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class LoginAction implements Action<LoginResult> {
  private String userEmail;
  private String userPassword;
  private String sessionId;
  
  public LoginAction() {
  }
  
  public LoginAction(String sessionId) {
    this.sessionId = sessionId;
  }
  
  public LoginAction(String email, String password) {
    this.userEmail = email;
    this.userPassword = password;
  }
  
  public String getUserEmail() {
    return userEmail;
  }
  
  public String getSessionId() {
    return sessionId;
  }
  
  public String getUserPassword() {
    return userPassword;
  }
  
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
  
  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }
  
  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }
}
