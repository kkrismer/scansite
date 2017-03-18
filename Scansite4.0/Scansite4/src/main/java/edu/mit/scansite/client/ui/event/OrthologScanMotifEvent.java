package edu.mit.scansite.client.ui.event;

import com.google.gwt.event.shared.GwtEvent;

import edu.mit.scansite.shared.dispatch.features.OrthologScanMotifResult;

/**
 * @author Konstantin Krismer
 */
public class OrthologScanMotifEvent extends
    GwtEvent<OrthologScanMotifEventHandler> {
  public static Type<OrthologScanMotifEventHandler> TYPE = new Type<OrthologScanMotifEventHandler>();

  private OrthologScanMotifResult result;

  public OrthologScanMotifEvent(OrthologScanMotifResult result) {
    this.result = result;
  }

  @Override
  public Type<OrthologScanMotifEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(OrthologScanMotifEventHandler handler) {
    handler.onOrthologScanMotifEvent(result);
  }
}
