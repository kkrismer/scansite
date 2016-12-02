package edu.mit.scansite.shared.dispatch.features;

import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SiteInSequenceHtmlGetAction implements
		Action<SiteInSequenceHtmlGetResult> {
	private ScanResultSite site;
	private int topPosition;
	private int leftPosition;

	public SiteInSequenceHtmlGetAction() {
	}

	public SiteInSequenceHtmlGetAction(ScanResultSite site, int topPosition,
			int leftPosition) {
		this.site = site;
		this.topPosition = topPosition;
		this.leftPosition = leftPosition;
	}

	public ScanResultSite getSite() {
		return site;
	}

	public void setSite(ScanResultSite site) {
		this.site = site;
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
