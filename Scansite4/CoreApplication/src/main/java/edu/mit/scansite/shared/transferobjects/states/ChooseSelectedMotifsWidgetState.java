package edu.mit.scansite.shared.transferobjects.states;

import java.util.List;

/**
 * @author Konstantin Krismer
 */
public class ChooseSelectedMotifsWidgetState extends State {
	private MotifClassWidgetState motifClassWidgetState;
	private List<Integer> selectedMotifIndices;
	private List<Integer> selectedMotifGroupIndices;

	public ChooseSelectedMotifsWidgetState() {

	}

	public ChooseSelectedMotifsWidgetState(
			MotifClassWidgetState motifClassWidgetState,
			List<Integer> selectedMotifIndices,
			List<Integer> selectedMotifGroupIndices) {
		super();
		this.motifClassWidgetState = motifClassWidgetState;
		this.selectedMotifIndices = selectedMotifIndices;
		this.selectedMotifGroupIndices = selectedMotifGroupIndices;
	}

	public MotifClassWidgetState getMotifClassWidgetState() {
		return motifClassWidgetState;
	}

	public void setMotifClassWidgetState(
			MotifClassWidgetState motifClassWidgetState) {
		this.motifClassWidgetState = motifClassWidgetState;
	}

	public List<Integer> getSelectedMotifIndices() {
		return selectedMotifIndices;
	}

	public void setSelectedMotifIndices(List<Integer> selectedMotifIndices) {
		this.selectedMotifIndices = selectedMotifIndices;
	}

	public List<Integer> getSelectedMotifGroupIndices() {
		return selectedMotifGroupIndices;
	}

	public void setSelectedMotifGroupIndices(
			List<Integer> selectedMotifGroupIndices) {
		this.selectedMotifGroupIndices = selectedMotifGroupIndices;
	}
}
