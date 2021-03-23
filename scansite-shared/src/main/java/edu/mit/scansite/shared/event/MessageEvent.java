package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MessageEvent extends GwtEvent<MessageEventHandler> {
	public static Type<MessageEventHandler> TYPE = new Type<MessageEventHandler>();

	public enum MessageEventPriority {
		WARNING, ERROR, INFO;
	}

	private MessageEventPriority priority;
	private String message;
	private String source;
	private int messageDisplayWidgetID = 0;
	private Throwable throwable;

	public MessageEvent(MessageEventPriority priority, String message,
			String source, Throwable throwable) {
		this.priority = priority;
		this.message = message;
		this.source = source;
		this.throwable = throwable;
	}

	public MessageEvent(MessageEventPriority priority, String message,
			String source, Throwable throwable, int messageDisplayWidgetID) {
		this.priority = priority;
		this.message = message;
		this.source = source;
		this.throwable = throwable;
		this.messageDisplayWidgetID = messageDisplayWidgetID;
	}

	@Override
	public Type<MessageEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MessageEventHandler handler) {
		handler.onMessageEvent(this);
	}

	public MessageEventPriority getPriority() {
		return priority;
	}

	public String getMessage() {
		return message;
	}

	public String getSource() {
		return source;
	}

	public int getMessageDisplayWidgetID() {
		return messageDisplayWidgetID;
	}

	public Throwable getThrowable() {
		return throwable;
	}
}
