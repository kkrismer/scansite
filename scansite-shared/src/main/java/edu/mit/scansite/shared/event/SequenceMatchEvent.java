package edu.mit.scansite.shared.event;

import com.google.gwt.event.shared.GwtEvent;

import edu.mit.scansite.shared.dispatch.features.SequenceMatchResult;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SequenceMatchEvent extends GwtEvent<SequenceMatchEventHandler> {
  public static Type<SequenceMatchEventHandler> TYPE = new Type<SequenceMatchEventHandler>();

  private SequenceMatchResult result;
  
  public SequenceMatchEvent(SequenceMatchResult result) {
    this.result = result;
  }
  
  @Override
  public Type<SequenceMatchEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(SequenceMatchEventHandler handler) {
    handler.onSequenceMatchEvent(result);
  }

}
