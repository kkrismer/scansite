package edu.mit.scansite.shared.dispatch.features;

import edu.mit.scansite.shared.transferobjects.DatabaseSearchScanResultSite;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DbSearchHistogramRetrieverAction implements
		Action<DbSearchHistogramRetrieverResult> {

	private DatabaseSearchScanResultSite site;
	private String histogramBasePath;
	private int topPosition = 0;
	private int leftPosition = 0;

	public DbSearchHistogramRetrieverAction() {
	}

	public DbSearchHistogramRetrieverAction(DatabaseSearchScanResultSite site,
			String histogramBasePath) {
		this.site = site;
		this.histogramBasePath = histogramBasePath;
	}

	public DbSearchHistogramRetrieverAction(DatabaseSearchScanResultSite site,
			String histogramBasePath, int topPosition, int leftPosition) {
		this.site = site;
		this.histogramBasePath = histogramBasePath;
		this.topPosition = topPosition;
		this.leftPosition = leftPosition;
	}

	public String getHistogramBasePath() {
		return histogramBasePath;
	}

	public DatabaseSearchScanResultSite getSite() {
		return site;
	}

	public void setSite(DatabaseSearchScanResultSite site) {
		this.site = site;
	}

	public void setHistogramBasePath(String histogramBasePath) {
		this.histogramBasePath = histogramBasePath;
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
