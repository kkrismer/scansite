package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public interface MessageEventHandler extends EventHandler {
  void onMessageEvent(MessageEvent event);
}
