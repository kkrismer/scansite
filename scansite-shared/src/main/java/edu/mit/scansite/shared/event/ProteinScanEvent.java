package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.GwtEvent;

import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinScanEvent extends GwtEvent<ProteinScanEventHandler> {
	public static Type<ProteinScanEventHandler> TYPE = new Type<ProteinScanEventHandler>();

	private ProteinScanResult scanResult;

	public ProteinScanEvent(ProteinScanResult result) {
		this.scanResult = result;
	}

	@Override
	public Type<ProteinScanEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ProteinScanEventHandler handler) {
		handler.onProteinScanEvent(scanResult);
	}
}
