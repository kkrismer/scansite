package edu.mit.scansite.client.ui.event;

import com.google.gwt.event.shared.GwtEvent;

import edu.mit.scansite.shared.dispatch.features.DatabaseScanResult;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DatabaseScanEvent extends GwtEvent<DatabaseScanEventHandler> {
	public static Type<DatabaseScanEventHandler> TYPE = new Type<DatabaseScanEventHandler>();

	private DatabaseScanResult result;

	public DatabaseScanEvent(DatabaseScanResult result) {
		this.result = result;
	}

	@Override
	public Type<DatabaseScanEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DatabaseScanEventHandler handler) {
		handler.onDatabaseScanEvent(result);
	}
}
