package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Konstantin Krismer
 */
public interface MessageClearEventHandler extends EventHandler {
	void onMessageClearEvent(MessageClearEvent event);
}
