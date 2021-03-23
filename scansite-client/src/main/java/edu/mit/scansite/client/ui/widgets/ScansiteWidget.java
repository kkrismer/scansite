package edu.mit.scansite.client.ui.widgets;

import java.util.LinkedList;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;

import edu.mit.scansite.shared.event.EventBus;
import edu.mit.scansite.shared.event.MessageClearEvent;
import edu.mit.scansite.shared.event.MessageEvent;
import edu.mit.scansite.shared.event.MessageEvent.MessageEventPriority;

/**
 * @author Konstantin Krismer
 */
public abstract class ScansiteWidget extends Composite {
	protected boolean isLoaded = false;
	protected LinkedList<Command> commands = new LinkedList<>();

	@Override
	public void onLoad() {
		isLoaded = true;
		while (!commands.isEmpty()) {
			commands.removeFirst().execute();
		}
	}

	@Override
	public void onUnload() {
		isLoaded = false;
	}

	protected void runCommandOnLoad(Command command) {
		if (isLoaded) {
			command.execute();
		} else {
			commands.add(command);
		}
	}

	public void hideMessage() {
		hideMessage(0);
	}

	public void hideMessage(int messageDisplayWidgetID) {
		EventBus.instance().fireEvent(new MessageClearEvent(messageDisplayWidgetID));
	}

	public void showErrorMessage(String message) {
		showErrorMessage(message, 0);
	}

	public void showErrorMessage(String message, int messageDisplayWidgetID) {
		EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.ERROR, message, this.getClass().toString(),
				null, messageDisplayWidgetID));
	}

	public void showWarningMessage(String message) {
		showWarningMessage(message, 0);
	}

	public void showWarningMessage(String message, int messageDisplayWidgetID) {
		EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.WARNING, message,
				this.getClass().toString(), null, messageDisplayWidgetID));
	}

	public void showInfoMessage(String message) {
		showInfoMessage(message, 0);
	}

	public void showInfoMessage(String message, int messageDisplayWidgetID) {
		EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.INFO, message, this.getClass().toString(),
				null, messageDisplayWidgetID));
	}
}
