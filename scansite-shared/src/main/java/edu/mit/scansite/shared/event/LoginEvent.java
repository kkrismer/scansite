package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Tobieh
 */
public class LoginEvent extends GwtEvent<LoginEventHandler> {
  public static Type<LoginEventHandler> TYPE = new Type<LoginEventHandler>();

  private String email;
  private String password;
  
  public LoginEvent(String email, String password) {
    this.email = email;
    this.password = password;
  }
  
  @Override
  public Type<LoginEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(LoginEventHandler handler) {
    handler.onLoginEvent(email, password);
  }

}
