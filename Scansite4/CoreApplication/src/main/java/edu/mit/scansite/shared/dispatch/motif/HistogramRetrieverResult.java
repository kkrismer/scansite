package edu.mit.scansite.shared.dispatch.motif;

import edu.mit.scansite.shared.transferobjects.Histogram;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramRetrieverResult implements Result {

	private int histogramNr;
	private Histogram histogram;
	private int topPosition = 0;
	private int leftPosition = 0;

	public HistogramRetrieverResult() {
	}

	public HistogramRetrieverResult(Histogram histogram, int histogramNr,
			int topPosition, int leftPosition) {
		this.histogram = histogram;
		this.histogramNr = histogramNr;
		this.topPosition = topPosition;
		this.leftPosition = leftPosition;
	}

	public Histogram getHistogram() {
		return histogram;
	}

	public int getHistogramNr() {
		return histogramNr;
	}

	public void setHistogram(Histogram histogram) {
		this.histogram = histogram;
	}

	public void setHistogramNr(int histogramNr) {
		this.histogramNr = histogramNr;
	}

	public int getTopPosition() {
		return topPosition;
	}

	public void setTopPosition(int topPosition) {
		this.topPosition = topPosition;
	}

	public int getLeftPosition() {
		return leftPosition;
	}

	public void setLeftPosition(int leftPosition) {
		this.leftPosition = leftPosition;
	}
}
