package edu.mit.scansite.shared.dispatch.features;

import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DatabaseScanAction implements Action<DatabaseScanResult> {

	// private DatabaseSearchMultipleRestriction multipleRestr;
	// private ArrayList<String> motifNicks;
	// private ArrayList<Motif> userMotifs;
	// private boolean isMultiple = false;

	private MotifSelection motifSelection;
	private DataSource dataSource = null;
	private RestrictionProperties restrictionProperties = null;
	private int outputListSize = 1000;
	private boolean previouslyMappedSitesOnly;
	private String userSessionId;

	public DatabaseScanAction() {
	}

	public DatabaseScanAction(MotifSelection motifSelection, DataSource dataSource,
			RestrictionProperties restrictionProperties, int outputListSize, boolean previouslyMappedSitesOnly,
			String userSessionId) {
		this.motifSelection = motifSelection;
		this.dataSource = dataSource;
		this.restrictionProperties = restrictionProperties;
		this.outputListSize = outputListSize;
		this.previouslyMappedSitesOnly = previouslyMappedSitesOnly;
		this.userSessionId = userSessionId;
	}

	public MotifSelection getMotifSelection() {
		return motifSelection;
	}

	public void setMotifSelection(MotifSelection motifSelection) {
		this.motifSelection = motifSelection;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public RestrictionProperties getRestrictionProperties() {
		return restrictionProperties;
	}

	public void setRestrictionProperties(RestrictionProperties restrictionProperties) {
		this.restrictionProperties = restrictionProperties;
	}

	public int getOutputListSize() {
		return outputListSize;
	}

	public void setOutputListSize(int outputListSize) {
		this.outputListSize = outputListSize;
	}

	public boolean isPreviouslyMappedSitesOnly() {
		return previouslyMappedSitesOnly;
	}

	public void setPreviouslyMappedSitesOnly(boolean previouslyMappedSitesOnly) {
		this.previouslyMappedSitesOnly = previouslyMappedSitesOnly;
	}

	public String getUserSessionId() {
		return userSessionId;
	}

	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}
}
