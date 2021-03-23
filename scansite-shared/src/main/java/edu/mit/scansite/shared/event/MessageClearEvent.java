package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Konstantin Krismer
 */
public class MessageClearEvent extends GwtEvent<MessageClearEventHandler> {
	public static Type<MessageClearEventHandler> TYPE = new Type<MessageClearEventHandler>();
	private int messageDisplayWidgetID = 0;

	public MessageClearEvent() {
		this(0);
	}

	public MessageClearEvent(int messageDisplayWidgetID) {
		this.messageDisplayWidgetID = messageDisplayWidgetID;
	}

	@Override
	public Type<MessageClearEventHandler> getAssociatedType() {
		return TYPE;
	}

	public int getMessageDisplayWidgetID() {
		return messageDisplayWidgetID;
	}

	@Override
	protected void dispatch(MessageClearEventHandler handler) {
		handler.onMessageClearEvent(this);
	}
}
