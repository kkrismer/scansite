package edu.mit.scansite.client.ui.event;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * @author Konstantin Krismer
 */
public class EventBus {
	private EventBus() {
	}

	private static final SimpleEventBus INSTANCE = GWT
			.create(SimpleEventBus.class);

	public static SimpleEventBus instance() {
		return INSTANCE;
	}
}
