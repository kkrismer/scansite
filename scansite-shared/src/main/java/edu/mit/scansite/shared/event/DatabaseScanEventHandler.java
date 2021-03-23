package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.EventHandler;

import edu.mit.scansite.shared.dispatch.features.DatabaseScanResult;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public interface DatabaseScanEventHandler extends EventHandler {
  void onDatabaseScanEvent(DatabaseScanResult result);
}
