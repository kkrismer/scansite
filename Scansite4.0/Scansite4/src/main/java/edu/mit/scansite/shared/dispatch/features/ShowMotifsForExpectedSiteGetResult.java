package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Result;

import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ShowMotifsForExpectedSiteGetResult implements Result {
	private SafeHtml html;
	private int topPosition;
	private int leftPosition;

	public ShowMotifsForExpectedSiteGetResult() {
	}

	public ShowMotifsForExpectedSiteGetResult(SafeHtml html, int topPosition,
			int leftPosition) {
		this.html = html;
		this.topPosition = topPosition;
		this.leftPosition = leftPosition;
	}

	public SafeHtml getHtml() {
		return html;
	}

	public void setHtml(SafeHtml html) {
		this.html = html;
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
