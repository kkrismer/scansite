package edu.mit.scansite.shared.dispatch.features;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.DatabaseSearchScanResultSite;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DatabaseScanResult implements Result {
	private boolean success = true;
	private String failureMessage = null;

	private MotifSelection motifSelection;
	private DataSource dataSource = null;
	private RestrictionProperties restrictionProperties = null;
	private int outputListSize = 1000;
	private List<String> motifDisplayNames;
	private DataSource localizationDataSource;

	// private ArrayList<String> motifNicks;
	// private ArrayList<Motif> userMotifs;
	// private boolean isMultiple = false;
	// private DatabaseSearchMultipleRestriction multipleRestr;

	private String histogramBasePath = "";
	private List<DatabaseSearchScanResultSite> dbSearchSites;
	private int totalNrOfProteinsInDb = 0;
	private int totalNrOfSites = 0;
	private int nrOfCombinedSites = 0;
	private int nrOfProteinsFound = 0;
	private double median = 0;
	private double medianAbsDev = 0;
	private String resultFilePath;

	public DatabaseScanResult() {
	}

	public DatabaseScanResult(String failureMessage) {
		success = false;
		this.failureMessage = failureMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getFailureMessage() {
		return failureMessage;
	}

	public MotifSelection getMotifSelection() {
		return motifSelection;
	}

	public void setMotifSelection(MotifSelection motifSelection) {
		this.motifSelection = motifSelection;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public RestrictionProperties getRestrictionProperties() {
		return restrictionProperties;
	}

	public void setRestrictionProperties(
			RestrictionProperties restrictionProperties) {
		this.restrictionProperties = restrictionProperties;
	}

	public int getOutputListSize() {
		return outputListSize;
	}

	public void setOutputListSize(int outputListSize) {
		this.outputListSize = outputListSize;
	}

	public String getHistogramBasePath() {
		return histogramBasePath;
	}

	public void setHistogramBasePath(String histogramBasePath) {
		this.histogramBasePath = histogramBasePath;
	}

	public List<DatabaseSearchScanResultSite> getDbSearchSites() {
		return dbSearchSites;
	}

	public void setDbSearchSites(
			List<DatabaseSearchScanResultSite> dbSearchSites) {
		this.dbSearchSites = dbSearchSites;
	}

	public int getTotalNrOfProteinsInDb() {
		return totalNrOfProteinsInDb;
	}

	public void setTotalNrOfProteinsInDb(int totalNrOfProteinsInDb) {
		this.totalNrOfProteinsInDb = totalNrOfProteinsInDb;
	}

	public int getTotalNrOfSites() {
		return totalNrOfSites;
	}

	public void setTotalNrOfSites(int totalNrOfSites) {
		this.totalNrOfSites = totalNrOfSites;
	}

	public int getNrOfCombinedSites() {
		return nrOfCombinedSites;
	}

	public void setNrOfCombinedSites(int nrOfCombinedSites) {
		this.nrOfCombinedSites = nrOfCombinedSites;
	}

	public int getNrOfProteinsFound() {
		return nrOfProteinsFound;
	}

	public void setNrOfProteinsFound(int nrOfProteinsFound) {
		this.nrOfProteinsFound = nrOfProteinsFound;
	}

	public double getMedian() {
		return median;
	}

	public void setMedian(double median) {
		this.median = median;
	}

	public double getMedianAbsDev() {
		return medianAbsDev;
	}

	public void setMedianAbsDev(double medianAbsDev) {
		this.medianAbsDev = medianAbsDev;
	}

	public String getResultFilePath() {
		return resultFilePath;
	}

	public void setResultFilePath(String resultFilePath) {
		this.resultFilePath = resultFilePath;
	}

	public List<String> getMotifDisplayNames() {
		return motifDisplayNames;
	}

	public void setMotifDisplayNames(List<String> motifDisplayNames) {
		this.motifDisplayNames = motifDisplayNames;
	}

	public DataSource getLocalizationDataSource() {
		return localizationDataSource;
	}

	public void setLocalizationDataSource(DataSource localizationDataSource) {
		this.localizationDataSource = localizationDataSource;
	}
}
