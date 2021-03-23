package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.GwtEvent;

import edu.mit.scansite.shared.dispatch.features.OrthologScanSequencePatternResult;

/**
 * @author Konstantin Krismer
 */
public class OrthologScanSequencePatternEvent extends
    GwtEvent<OrthologScanSequencePatternEventHandler> {
  public static Type<OrthologScanSequencePatternEventHandler> TYPE = new Type<OrthologScanSequencePatternEventHandler>();

  private OrthologScanSequencePatternResult result;

  public OrthologScanSequencePatternEvent(
      OrthologScanSequencePatternResult result) {
    this.result = result;
  }

  @Override
  public Type<OrthologScanSequencePatternEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(OrthologScanSequencePatternEventHandler handler) {
    handler.onOrthologScanSequencePatternEvent(result);
  }
}
