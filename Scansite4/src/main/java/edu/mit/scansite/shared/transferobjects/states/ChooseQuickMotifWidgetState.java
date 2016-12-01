package edu.mit.scansite.shared.transferobjects.states;

import edu.mit.scansite.shared.transferobjects.SequencePattern;

/**
 * @author Konstantin Krismer
 */
public class ChooseQuickMotifWidgetState extends State {
	private String motifName;
	private SequencePattern firstSequencePattern;
	private SequencePattern secondSequencePattern;

	public ChooseQuickMotifWidgetState() {

	}

	public ChooseQuickMotifWidgetState(String motifName,
			SequencePattern firstSequencePattern,
			SequencePattern secondSequencePattern) {
		super();
		this.motifName = motifName;
		this.firstSequencePattern = firstSequencePattern;
		this.secondSequencePattern = secondSequencePattern;
	}

	public String getMotifName() {
		return motifName;
	}

	public void setMotifName(String motifName) {
		this.motifName = motifName;
	}

	public SequencePattern getFirstSequencePattern() {
		return firstSequencePattern;
	}

	public void setFirstSequencePattern(SequencePattern firstSequencePattern) {
		this.firstSequencePattern = firstSequencePattern;
	}

	public SequencePattern getSecondSequencePattern() {
		return secondSequencePattern;
	}

	public void setSecondSequencePattern(SequencePattern secondSequencePattern) {
		this.secondSequencePattern = secondSequencePattern;
	}
}
