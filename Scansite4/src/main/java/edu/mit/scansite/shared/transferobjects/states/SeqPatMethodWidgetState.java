package edu.mit.scansite.shared.transferobjects.states;

import java.util.List;

import edu.mit.scansite.shared.transferobjects.SequencePattern;

/**
 * @author Konstantin Krismer
 */
public class SeqPatMethodWidgetState extends State {
	private List<SequencePattern> sequencePatterns;
	private boolean showPhosphorylatedProteinsOnly;

	public SeqPatMethodWidgetState() {

	}

	public SeqPatMethodWidgetState(List<SequencePattern> sequencePatterns,
			boolean showPhosphorylatedProteinsOnly) {
		this.sequencePatterns = sequencePatterns;
		this.showPhosphorylatedProteinsOnly = showPhosphorylatedProteinsOnly;
	}

	public List<SequencePattern> getSequencePatterns() {
		return sequencePatterns;
	}

	public void setSequencePatterns(List<SequencePattern> sequencePatterns) {
		this.sequencePatterns = sequencePatterns;
	}

	public boolean isShowPhosphorylatedProteinsOnly() {
		return showPhosphorylatedProteinsOnly;
	}

	public void setShowPhosphorylatedProteinsOnly(
			boolean showPhosphorylatedProteinsOnly) {
		this.showPhosphorylatedProteinsOnly = showPhosphorylatedProteinsOnly;
	}
}
