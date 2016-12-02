package edu.mit.scansite.client.ui.event;

import com.google.gwt.event.shared.GwtEvent;

import edu.mit.scansite.shared.transferobjects.Histogram;

/**
 * @author Tobieh
 */
public class HistogramEditChangeEvent extends GwtEvent<HistogramEditChangeEventHandler> {
  public static Type<HistogramEditChangeEventHandler> TYPE = new Type<HistogramEditChangeEventHandler>();
  
  private Histogram histogram;
  private int histogramNr;
  
  public HistogramEditChangeEvent() {
  }
  
  @Override
  public Type<HistogramEditChangeEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(HistogramEditChangeEventHandler handler) {
    handler.onHistogramEditChangeEvent(this);
  }
  
  public Histogram getHistogram() {
    return histogram;
  }
  
  public void setHistogram(Histogram histogram) {
    this.histogram = histogram;
  }

  public void setHistogramNumber(int histogramNr) {
    this.histogramNr = histogramNr;
  }
  
  public int getHistogramNr() {
    return histogramNr;
  }
}
