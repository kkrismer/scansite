package edu.mit.scansite.client.ui.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public interface NavigationEventHandler extends EventHandler {
  void onNavigationEvent(NavigationEvent event);
}
