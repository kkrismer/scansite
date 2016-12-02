package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DbSearchHistogramRetrieverResult implements Result {
	private String histogramFilePath;
	private int topPosition = 0;
	private int leftPosition = 0;

	public DbSearchHistogramRetrieverResult() {
	}

	public DbSearchHistogramRetrieverResult(String histogramFilePath) {
		this.histogramFilePath = histogramFilePath;
	}

	public DbSearchHistogramRetrieverResult(String histogramFilePath,
			int topPosition, int leftPosition) {
		this.histogramFilePath = histogramFilePath;
		this.topPosition = topPosition;
		this.leftPosition = leftPosition;
	}

	public String getHistogramFilePath() {
		return histogramFilePath;
	}

	public void setHistogramFilePath(String histogramFilePath) {
		this.histogramFilePath = histogramFilePath;
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
