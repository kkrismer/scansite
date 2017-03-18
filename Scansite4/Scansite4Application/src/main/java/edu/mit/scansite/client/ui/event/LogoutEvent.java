package edu.mit.scansite.client.ui.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Tobieh
 */
public class LogoutEvent extends GwtEvent<LogoutEventHandler> {
  public static Type<LogoutEventHandler> TYPE = new Type<LogoutEventHandler>();

  public LogoutEvent() {
  }
  
  @Override
  public Type<LogoutEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(LogoutEventHandler handler) {
    handler.onLogoutEvent();
  }

}
