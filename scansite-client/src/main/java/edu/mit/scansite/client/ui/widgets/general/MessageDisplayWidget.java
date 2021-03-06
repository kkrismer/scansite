package edu.mit.scansite.client.ui.widgets.general;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.event.EventBus;
import edu.mit.scansite.shared.event.MessageClearEvent;
import edu.mit.scansite.shared.event.MessageClearEventHandler;
import edu.mit.scansite.shared.event.MessageEvent;
import edu.mit.scansite.shared.event.MessageEventHandler;
import edu.mit.scansite.shared.event.MessageEvent.MessageEventPriority;

/**
 * @author Konstantin Krismer
 */
public class MessageDisplayWidget extends ScansiteWidget {
	interface MessageDisplayWidgetUiBinder extends UiBinder<Widget, MessageDisplayWidget> {
	}

	private static MessageDisplayWidgetUiBinder uiBinder = GWT.create(MessageDisplayWidgetUiBinder.class);

	private MessageEventPriority selectivePriority = null;

	@UiField
	SpanElement messageSpan;

	public @UiConstructor MessageDisplayWidget(final int messageDisplayWidgetID) {
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.instance().addHandler(MessageEvent.TYPE, new MessageEventHandler() {
			@Override
			public void onMessageEvent(MessageEvent event) {
				if (event.getMessageDisplayWidgetID() == messageDisplayWidgetID) {
					if (selectivePriority != null) {
						if (event.getPriority().equals(selectivePriority)) {
							messageSpan.setClassName(retrieveClassName(selectivePriority));
							messageSpan.setInnerText(event.getMessage());
						}
					} else {
						messageSpan.setInnerText(event.getMessage());
						messageSpan.setClassName(retrieveClassName(event.getPriority()));
					}
				}
			}
		});

		EventBus.instance().addHandler(MessageClearEvent.TYPE, new MessageClearEventHandler() {
			@Override
			public void onMessageClearEvent(MessageClearEvent event) {
				if (event.getMessageDisplayWidgetID() == messageDisplayWidgetID) {
					messageSpan.setClassName("");
					messageSpan.setInnerText("");
				}
			}
		});
	}

	public MessageEventPriority getSelectivePriority() {
		return selectivePriority;
	}

	public void setSelectivePriority(MessageEventPriority selectivePriority) {
		this.selectivePriority = selectivePriority;
	}

	private String retrieveClassName(MessageEventPriority priority) {
		switch (priority) {
		case ERROR:
			return "errorMessage";
		case WARNING:
			return "warningMessage";
		case INFO:
			return "infoMessage";
		default:
			return "warningMessage";
		}
	}
}
