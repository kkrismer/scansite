package edu.mit.scansite.shared.dispatch.motif;

import edu.mit.scansite.shared.transferobjects.Histogram;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramUpdateAction implements Action<HistogramRetrieverResult> {
  private Histogram histogram;
  private int histogramNr;
  
  public HistogramUpdateAction() {
  }

  public void setHistogram(Histogram histogram) {
    this.histogram = histogram;
  }

  public void setHistogramNr(int histNr) {
    this.histogramNr = histNr;
  }

  public Histogram getHistogram() {
    return histogram;
  }

  public int getHistogramNr() {
    return histogramNr;
  }

}
