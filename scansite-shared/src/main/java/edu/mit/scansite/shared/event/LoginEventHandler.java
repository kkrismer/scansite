package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Tobieh
 */
public interface LoginEventHandler extends EventHandler {
  void onLoginEvent(final String email, final String password);
}
