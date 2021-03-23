package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Tobieh
 */
public interface HistogramEditChangeEventHandler extends EventHandler {
  void onHistogramEditChangeEvent(HistogramEditChangeEvent event);
}
