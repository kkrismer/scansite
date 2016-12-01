package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;

/**
 * @author Konstantin Krismer
 */
public class OrthologScanMotifAction implements Action<OrthologScanMotifResult> {
	private LightWeightMotifGroup motifGroup;
	private int sitePosition;
	private DataSource orthologyDataSource;
	private LightWeightProtein protein;
	private HistogramStringency stringency;
	private int alignmentRadius;
	private boolean isMainFormEmission;
	private String userSessionId;

	public OrthologScanMotifAction() {
		super();
	}

	public OrthologScanMotifAction(LightWeightMotifGroup motifGroup,
			int sitePosition, DataSource orthologyDataSource,
			LightWeightProtein protein, HistogramStringency stringency,
			int alignmentRadius, String userSessionId) {
		super();
		this.motifGroup = motifGroup;
		this.sitePosition = sitePosition;
		this.orthologyDataSource = orthologyDataSource;
		this.protein = protein;
		this.stringency = stringency;
		this.alignmentRadius = alignmentRadius;
		isMainFormEmission = true;
		this.userSessionId = userSessionId;
	}

	public OrthologScanMotifAction(LightWeightMotifGroup motifGroup,
			int sitePosition, LightWeightProtein protein,
			HistogramStringency stringency, int alignmentRadius,
			String userSessionId) {
		super();
		this.motifGroup = motifGroup;
		this.sitePosition = sitePosition;
		this.orthologyDataSource = null;
		this.protein = protein;
		this.stringency = stringency;
		this.alignmentRadius = alignmentRadius;
		isMainFormEmission = false;
		this.userSessionId = userSessionId;
	}

	public LightWeightMotifGroup getMotifGroup() {
		return motifGroup;
	}

	public void setMotifGroup(LightWeightMotifGroup motifGroup) {
		this.motifGroup = motifGroup;
	}

	public int getSitePosition() {
		return sitePosition;
	}

	public void setSitePosition(int sitePosition) {
		this.sitePosition = sitePosition;
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
