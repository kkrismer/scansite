package edu.mit.scansite.shared.transferobjects.states;

import edu.mit.scansite.shared.transferobjects.MotifClass;

/**
 * @author Konstantin Krismer
 */
public class MotifClassWidgetState extends State {
	private MotifClass motifClass;

	public MotifClassWidgetState() {

	}

	public MotifClassWidgetState(MotifClass motifClass) {
		this.motifClass = motifClass;
	}

	public MotifClass getMotifClass() {
		return motifClass;
	}

	public void setMotifClass(MotifClass motifClass) {
		this.motifClass = motifClass;
	}
}
