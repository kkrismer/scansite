package edu.mit.scansite.shared.transferobjects;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A data structure for representing scan results.
 * 
 * @author tobieh
 */
public class ScanResults implements IsSerializable {
	private LightWeightProtein protein;
	private MotifSelection motifSelection;
	private Boolean showDomains = false;
	private HistogramStringency histogramThreshold;
	private String imagePath;
	private List<ScanResultSite> hits;
	private List<DataSource> orthologyDataSources;
	private DataSource localizationDataSource;
	private Localization proteinLocalization;

	private DataSource histogramDataSourceSelection;
	private String histogramTaxonName;
	private List<DomainPosition> domainPositions;
	private String resultFilePath;

	public ScanResults() {
	}

	public ScanResults(LightWeightProtein protein,
			MotifSelection motifSelection, boolean showDomains,
			HistogramStringency histogramThreshold, String imagePath,
			List<ScanResultSite> hits, List<DataSource> orthologyDataSources,
			DataSource localizationDataSource, Localization proteinLocalization) {
		this.showDomains = showDomains;
		this.protein = protein;
		this.imagePath = imagePath;
		this.hits = hits;
		this.histogramThreshold = histogramThreshold;
		this.orthologyDataSources = orthologyDataSources;
		this.localizationDataSource = localizationDataSource;
		this.proteinLocalization = proteinLocalization;
	}

	public LightWeightProtein getProtein() {
		return protein;
	}

	public void setProtein(LightWeightProtein protein) {
		this.protein = protein;
	}

	public MotifSelection getMotifSelection() {
		return motifSelection;
	}

	public void setMotifSelection(MotifSelection motifSelection) {
		this.motifSelection = motifSelection;
	}

	public Boolean isShowDomains() {
		return showDomains;
	}

	public void setShowDomains(Boolean showDomains) {
		this.showDomains = showDomains;
	}

	public HistogramStringency getHistogramThreshold() {
		return histogramThreshold;
	}

	public void setHistogramThreshold(HistogramStringency histogramThreshold) {
		this.histogramThreshold = histogramThreshold;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public List<ScanResultSite> getHits() {
		return hits;
	}

	public void setHits(List<ScanResultSite> hits) {
		this.hits = hits;
	}

	public List<DataSource> getOrthologyDataSources() {
		return orthologyDataSources;
	}

	public void setOrthologyDataSources(List<DataSource> orthologyDataSources) {
		this.orthologyDataSources = orthologyDataSources;
	}

	public DataSource getLocalizationDataSource() {
		return localizationDataSource;
	}

	public void setLocalizationDataSource(DataSource localizationDataSource) {
		this.localizationDataSource = localizationDataSource;
	}

	public Localization getProteinLocalization() {
		return proteinLocalization;
	}

	public void setProteinLocalization(Localization proteinLocalization) {
		this.proteinLocalization = proteinLocalization;
	}

	public DataSource getHistogramDataSourceSelection() {
		return histogramDataSourceSelection;
	}

	public void setHistogramDataSourceSelection(
			DataSource histogramDataSourceSelection) {
		this.histogramDataSourceSelection = histogramDataSourceSelection;
	}

	public String getHistogramTaxonName() {
		return histogramTaxonName;
	}

	public void setHistogramTaxonName(String histogramTaxonName) {
		this.histogramTaxonName = histogramTaxonName;
	}

	public List<DomainPosition> getDomainPositions() {
		return domainPositions;
	}

	public void setDomainPositions(List<DomainPosition> domainPositions) {
		this.domainPositions = domainPositions;
	}

	public String getResultFilePath() {
		return resultFilePath;
	}

	public void setResultFilePath(String resultFilePath) {
		this.resultFilePath = resultFilePath;
	}
}
