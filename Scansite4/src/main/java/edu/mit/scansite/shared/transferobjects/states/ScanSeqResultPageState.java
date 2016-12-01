package edu.mit.scansite.shared.transferobjects.states;

import edu.mit.scansite.shared.dispatch.features.SequenceMatchResult;

/**
 * @author Konstantin Krismer
 */
public class ScanSeqResultPageState extends State {
	private SequenceMatchResult sequenceMatchResult;

	public ScanSeqResultPageState() {

	}

	public ScanSeqResultPageState(SequenceMatchResult sequenceMatchResult) {
		this.sequenceMatchResult = sequenceMatchResult;
	}

	public SequenceMatchResult getSequenceMatchResult() {
		return sequenceMatchResult;
	}

	public void setSequenceMatchResult(SequenceMatchResult sequenceMatchResult) {
		this.sequenceMatchResult = sequenceMatchResult;
	}
}
