package edu.mit.scansite.shared.dispatch.features;

import java.util.List;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import edu.mit.scansite.shared.transferobjects.SequencePattern;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ShowMotifsForExpectedSiteGetAction implements
		Action<ShowMotifsForExpectedSiteGetResult> {
	private List<ScanResultSite> hits;
	private List<SequencePattern> patterns;
	private int topPosition;
	private int leftPosition;

	public ShowMotifsForExpectedSiteGetAction() {
	}

	public ShowMotifsForExpectedSiteGetAction(List<ScanResultSite> hits,
			List<SequencePattern> patterns) {
		super();
		this.hits = hits;
		this.patterns = patterns;
	}

	public ShowMotifsForExpectedSiteGetAction(List<ScanResultSite> hits,
			List<SequencePattern> patterns, int topPosition, int leftPosition) {
		super();
		this.hits = hits;
		this.patterns = patterns;
		this.topPosition = topPosition;
		this.leftPosition = leftPosition;
	}

	public List<ScanResultSite> getHits() {
		return hits;
	}

	public void setHits(List<ScanResultSite> hits) {
		this.hits = hits;
	}

	public List<SequencePattern> getPatterns() {
		return patterns;
	}

	public void setPatterns(List<SequencePattern> patterns) {
		this.patterns = patterns;
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
