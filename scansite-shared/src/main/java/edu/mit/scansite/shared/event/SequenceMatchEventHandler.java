package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.EventHandler;

import edu.mit.scansite.shared.dispatch.features.SequenceMatchResult;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public interface SequenceMatchEventHandler extends EventHandler {
	void onSequenceMatchEvent(SequenceMatchResult result);
}
