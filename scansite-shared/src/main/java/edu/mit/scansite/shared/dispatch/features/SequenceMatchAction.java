package edu.mit.scansite.shared.dispatch.features;

import java.util.List;

import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;
import edu.mit.scansite.shared.transferobjects.SequencePattern;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SequenceMatchAction implements Action<SequenceMatchResult> {

	private List<SequencePattern> sequencePatterns = null;
	private DataSource dataSource = null;
	private RestrictionProperties restrictionProperties = null;
	private boolean limitResultsToPhosphorylatedProteins;
	private String userSessionId = "";

	public SequenceMatchAction() {
	}

	public SequenceMatchAction(List<SequencePattern> sequencePatterns,
			DataSource dataSource, RestrictionProperties restrictionProperties,
			boolean limitResultsToPhosphorylatedProteins, String userSessionId) {
		this.sequencePatterns = sequencePatterns;
		this.dataSource = dataSource;
		this.restrictionProperties = restrictionProperties;
		this.limitResultsToPhosphorylatedProteins = limitResultsToPhosphorylatedProteins;
		this.userSessionId = userSessionId;
	}

	public List<SequencePattern> getSequencePatterns() {
		return sequencePatterns;
	}

	public void setSequencePatterns(List<SequencePattern> sequencePatterns) {
		this.sequencePatterns = sequencePatterns;
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

	public boolean isLimitResultsToPhosphorylatedProteins() {
		return limitResultsToPhosphorylatedProteins;
	}

	public void setLimitResultsToPhosphorylatedProteins(
			boolean limitResultsToPhosphorylatedProteins) {
		this.limitResultsToPhosphorylatedProteins = limitResultsToPhosphorylatedProteins;
	}

	public String getUserSessionId() {
		return userSessionId;
	}

	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}
}
