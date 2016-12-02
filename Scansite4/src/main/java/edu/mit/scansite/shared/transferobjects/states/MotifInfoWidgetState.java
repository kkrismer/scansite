package edu.mit.scansite.shared.transferobjects.states;

/**
 * @author Konstantin Krismer
 */
public class MotifInfoWidgetState extends State {
	private MotifClassWidgetState motifClassWidgetState;
	private int motifListBoxSelectedIndex;

	public MotifInfoWidgetState() {

	}

	public MotifInfoWidgetState(MotifClassWidgetState motifClassWidgetState,
			int motifListBoxSelectedIndex) {
		super();
		this.motifClassWidgetState = motifClassWidgetState;
		this.motifListBoxSelectedIndex = motifListBoxSelectedIndex;
	}

	public MotifClassWidgetState getMotifClassWidgetState() {
		return motifClassWidgetState;
	}

	public void setMotifClassWidgetState(
			MotifClassWidgetState motifClassWidgetState) {
		this.motifClassWidgetState = motifClassWidgetState;
	}

	public int getMotifListBoxSelectedIndex() {
		return motifListBoxSelectedIndex;
	}

	public void setMotifListBoxSelectedIndex(int motifListBoxSelectedIndex) {
		this.motifListBoxSelectedIndex = motifListBoxSelectedIndex;
	}
}
