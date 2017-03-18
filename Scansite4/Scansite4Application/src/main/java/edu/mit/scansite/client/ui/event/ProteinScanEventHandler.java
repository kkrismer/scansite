package edu.mit.scansite.client.ui.event;

import com.google.gwt.event.shared.EventHandler;

import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public interface ProteinScanEventHandler extends EventHandler {
	void onProteinScanEvent(ProteinScanResult scanResult);
}
