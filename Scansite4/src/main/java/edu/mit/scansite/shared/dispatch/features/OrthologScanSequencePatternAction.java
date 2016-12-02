package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.SequencePattern;

/**
 * @author Konstantin Krismer
 */
public class OrthologScanSequencePatternAction implements
		Action<OrthologScanSequencePatternResult> {

	private SequencePattern sequencePattern;
	private DataSource orthologyDataSource;
	private LightWeightProtein protein;
	private HistogramStringency stringency;
	private int alignmentRadius;
	private boolean isMainFormEmission;
	private String userSessionId;

	public OrthologScanSequencePatternAction() {

	}

	public OrthologScanSequencePatternAction(SequencePattern sequencePattern,
			DataSource orthologyDataSource, LightWeightProtein protein,
			HistogramStringency stringency, int alignmentRadius, String userSessionId) {
		this.sequencePattern = sequencePattern;
		this.orthologyDataSource = orthologyDataSource;
		this.protein = protein;
		this.stringency = stringency;
		this.alignmentRadius = alignmentRadius;
		isMainFormEmission = true;
		this.userSessionId = userSessionId;
	}

	public OrthologScanSequencePatternAction(SequencePattern sequencePattern,
			LightWeightProtein protein, HistogramStringency stringency,
			int alignmentRadius, String userSessionId) {
		this.sequencePattern = sequencePattern;
		this.orthologyDataSource = null;
		this.protein = protein;
		this.stringency = stringency;
		this.alignmentRadius = alignmentRadius;
		isMainFormEmission = false;
		this.userSessionId = userSessionId;
	}

	public SequencePattern getSequencePattern() {
		return sequencePattern;
	}

	public void setSequencePattern(SequencePattern sequencePattern) {
		this.sequencePattern = sequencePattern;
	}

	public DataSource getOrthologyDataSource() {
		return orthologyDataSource;
	}

	public void setOrthologyDataSource(DataSource orthologyDataSource) {
		this.orthologyDataSource = orthologyDataSource;
	}

	public LightWeightProtein getProtein() {
		return protein;
	}

	public void setProtein(LightWeightProtein protein) {
		this.protein = protein;
	}

	public HistogramStringency getStringency() {
		return stringency;
	}

	public void setStringency(HistogramStringency stringency) {
		this.stringency = stringency;
	}

	public int getAlignmentRadius() {
		return alignmentRadius;
	}

	public void setAlignmentRadius(int alignmentRadius) {
		this.alignmentRadius = alignmentRadius;
	}

	public boolean isMainFormEmission() {
		return isMainFormEmission;
	}

	public void setMainFormEmission(boolean isMainFormEmission) {
		this.isMainFormEmission = isMainFormEmission;
	}

	public String getUserSessionId() {
		return userSessionId;
	}

	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}
}
