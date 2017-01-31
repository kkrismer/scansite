package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.MotifSelection;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinScanAction implements Action<ProteinScanResult> {
	private LightWeightProtein protein;
	private MotifSelection motifSelection;
	private HistogramStringency stringency;
	private boolean showDomains;
	private String histogramDataSource;
	private String histogramTaxon;
	private DataSource localizationDataSource;
	private String userSessionId;

	public ProteinScanAction() {

	}

	public ProteinScanAction(LightWeightProtein protein,
			MotifSelection motifSelection, HistogramStringency stringency,
			boolean showDomains, String histogramDataSource,
			String histogramTaxon, DataSource localizationDataSource, String userSessionId) {
		this.protein = protein;
		this.motifSelection = motifSelection;
		this.stringency = stringency;
		this.showDomains = showDomains;
		this.histogramDataSource = histogramDataSource;
		this.histogramTaxon = histogramTaxon;
		this.localizationDataSource = localizationDataSource;
		this.userSessionId = userSessionId;
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

	public boolean isShowDomains() {
		return showDomains;
	}

	public void setShowDomains(boolean showDomains) {
		this.showDomains = showDomains;
	}

	public MotifSelection getMotifSelection() {
		return motifSelection;
	}

	public void setMotifSelection(MotifSelection motifSelection) {
		this.motifSelection = motifSelection;
	}

	public String getHistogramDataSource() {
		return histogramDataSource;
	}

	public void setHistogramDataSource(String histogramDataSource) {
		this.histogramDataSource = histogramDataSource;
	}

	public String getHistogramTaxon() {
		return histogramTaxon;
	}

	public void setHistogramTaxon(String histogramTaxon) {
		this.histogramTaxon = histogramTaxon;
	}

	public DataSource getLocalizationDataSource() {
		return localizationDataSource;
	}

	public void setLocalizationDataSource(DataSource localizationDataSource) {
		this.localizationDataSource = localizationDataSource;
	}

	public String getUserSessionId() {
		return userSessionId;
	}

	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}
}
