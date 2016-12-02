package edu.mit.scansite.client.ui.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Konstantin Krismer
 */
public class MessageClearEvent extends GwtEvent<MessageClearEventHandler> {
	public static Type<MessageClearEventHandler> TYPE = new Type<MessageClearEventHandler>();

	public MessageClearEvent() {

	}

	@Override
	public Type<MessageClearEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MessageClearEventHandler handler) {
		handler.onMessageClearEvent(this);
	}
}
