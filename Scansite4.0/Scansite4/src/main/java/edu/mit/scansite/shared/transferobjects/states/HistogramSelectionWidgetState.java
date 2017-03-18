package edu.mit.scansite.shared.transferobjects.states;

/**
 * @author Konstantin Krismer
 */
public class HistogramSelectionWidgetState extends State {
	private boolean referenceProteomeCheckBoxValue;
	private int referenceProteomeListBoxSelectedIndex;

	public HistogramSelectionWidgetState() {

	}

	public HistogramSelectionWidgetState(
			boolean referenceProteomeCheckBoxValue,
			int referenceProteomeListBoxSelectedIndex) {
		super();
		this.referenceProteomeCheckBoxValue = referenceProteomeCheckBoxValue;
		this.referenceProteomeListBoxSelectedIndex = referenceProteomeListBoxSelectedIndex;
	}

	public boolean isReferenceProteomeCheckBoxValue() {
		return referenceProteomeCheckBoxValue;
	}

	public void setReferenceProteomeCheckBoxValue(
			boolean referenceProteomeCheckBoxValue) {
		this.referenceProteomeCheckBoxValue = referenceProteomeCheckBoxValue;
	}

	public int getReferenceProteomeListBoxSelectedIndex() {
		return referenceProteomeListBoxSelectedIndex;
	}

	public void setReferenceProteomeListBoxSelectedIndex(
			int referenceProteomeListBoxSelectedIndex) {
		this.referenceProteomeListBoxSelectedIndex = referenceProteomeListBoxSelectedIndex;
	}
}
