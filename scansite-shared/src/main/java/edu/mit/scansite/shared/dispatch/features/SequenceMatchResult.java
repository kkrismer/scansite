package edu.mit.scansite.shared.dispatch.features;

import java.util.List;

import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.ProteinSequenceMatch;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;
import edu.mit.scansite.shared.transferobjects.SequencePattern;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SequenceMatchResult implements Result {

	private List<SequencePattern> sequencePatterns;
	private DataSource dataSource;
	private RestrictionProperties restrictionProperties;
	private int proteinsInDbCount = 0;
	private int restrictedProteinsInDbCount = 0;
	private int sequencePatternMatchCount = 0;
	private List<ProteinSequenceMatch> matches = null;
	private String resultFileName;
	private boolean isMoreMatchesThanMaxAllowed = false;
	private List<DataSource> compatibleOrthologyDataSources;

	public SequenceMatchResult() {
	}

	public SequenceMatchResult(List<ProteinSequenceMatch> matches) {
		this.matches = matches;
	}

	public void setMatches(List<ProteinSequenceMatch> matches) {
		this.matches = matches;
	}

	public List<ProteinSequenceMatch> getMatches() {
		return matches;
	}

	public List<SequencePattern> getSequencePatterns() {
		return sequencePatterns;
	}

	public void setSequencePatterns(List<SequencePattern> sequencePatterns) {
		this.sequencePatterns = sequencePatterns;
	}

	public int getProteinsInDbCount() {
		return proteinsInDbCount;
	}

	public void setProteinsInDbCount(int proteinsInDbCount) {
		this.proteinsInDbCount = proteinsInDbCount;
	}

	public int getRestrictedProteinsInDbCount() {
		return restrictedProteinsInDbCount;
	}

	public void setRestrictedProteinsInDbCount(int restrictedProteinsInDbCount) {
		this.restrictedProteinsInDbCount = restrictedProteinsInDbCount;
	}

	public int getSequencePatternMatchCount() {
		return sequencePatternMatchCount;
	}

	public void setSequencePatternMatchCount(int sequencePatternMatchCount) {
		this.sequencePatternMatchCount = sequencePatternMatchCount;
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

	public String getResultFilePath() {
		return resultFileName;
	}

	public void setResultFileName(String resultFileName) {
		this.resultFileName = resultFileName;
	}

	public void setMoreMatchesThanMaxAllowed(boolean moreThanMaxAllowed) {
		this.isMoreMatchesThanMaxAllowed = moreThanMaxAllowed;
	}

	public boolean isMoreMatchesThanMaxAllowed() {
		return isMoreMatchesThanMaxAllowed;
	}

	public List<DataSource> getCompatibleOrthologyDataSources() {
		return compatibleOrthologyDataSources;
	}

	public void setCompatibleOrthologyDataSources(
			List<DataSource> compatibleOrthologyDataSources) {
		this.compatibleOrthologyDataSources = compatibleOrthologyDataSources;
	}
}
