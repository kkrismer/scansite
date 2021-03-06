package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.EventHandler;

import edu.mit.scansite.shared.dispatch.features.OrthologScanMotifResult;

/**
 * @author Konstantin Krismer
 */
public interface OrthologScanMotifEventHandler extends EventHandler {
  void onOrthologScanMotifEvent(OrthologScanMotifResult result);
}
