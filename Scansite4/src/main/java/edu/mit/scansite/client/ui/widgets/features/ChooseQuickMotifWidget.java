package edu.mit.scansite.client.ui.widgets.features;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.AminoAcid;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.states.ChooseQuickMotifWidgetState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ChooseQuickMotifWidget extends ChooseMotifWidget implements
		Stateful<ChooseQuickMotifWidgetState> {

	private static ChooseQuickMotifWidgetUiBinder uiBinder = GWT
			.create(ChooseQuickMotifWidgetUiBinder.class);

	interface ChooseQuickMotifWidgetUiBinder extends
			UiBinder<Widget, ChooseQuickMotifWidget> {
	}

	public ChooseQuickMotifWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	TextBox motifNameTextBox;

	@UiField
	SequencePatternWidget firstSequencePatternWidget;

	@UiField
	SequencePatternWidget secondSequencePatternWidget;

	@Override
	public ChooseQuickMotifWidgetState getState() {
		return new ChooseQuickMotifWidgetState(motifNameTextBox.getValue(),
				firstSequencePatternWidget.getSequencePattern(),
				secondSequencePatternWidget.getSequencePattern());
	}

	@Override
	public void setState(final ChooseQuickMotifWidgetState state) {
		if (state != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					motifNameTextBox.setValue(state.getMotifName());
					firstSequencePatternWidget.setSequencePattern(state
							.getFirstSequencePattern());
					secondSequencePatternWidget.setSequencePattern(state
							.getSecondSequencePattern());
				}
			});
		}
	}

	@Override
	public MotifSelection getMotifSelection() {
		MotifSelection motifSelection = new MotifSelection();
		String[] primary = firstSequencePatternWidget.getInput();
		String[] secondary = secondSequencePatternWidget.getInput();

		Motif motif = new Motif();
		motif.setDisplayName((motifNameTextBox.getText().isEmpty() ? "unnamed motif"
				: motifNameTextBox.getText()));
		double currentValue = 0.0;
		for (int pos = 0; pos < primary.length; ++pos) { // primary.length ==
															// secondary.length
			String primVal = primary[pos];
			String secVal = secondary[pos];
			for (AminoAcid aa : AminoAcid.values()) {
				if (pos == ScansiteConstants.WINDOW_CENTER_INDEX) { // Fixed
																	// position
																	// row
					currentValue = 0.0;
					if (primVal.contains(String.valueOf(aa.getOneLetterCode()))) {
						currentValue = ScansiteConstants.FIXED_SITE_SCORE;
					}
				} else { // All other rows
					currentValue = 1.0;
					if (primVal.contains(String.valueOf(aa.getOneLetterCode()))) {
						currentValue = 9.0;
					} else if (secVal.contains(String.valueOf(aa
							.getOneLetterCode()))) {
						currentValue = 4.5;
					}

					// special values
					if (aa.equals(AminoAcid.X)) {
						currentValue = 1.0;
					} else if (aa.equals(AminoAcid._C)
							|| aa.equals(AminoAcid._N)) {
						currentValue = 0.0;
					}
				}
				motif.setValue(aa, pos, currentValue);
			}
		}
		motif.resetNumbers();
		motifSelection.setUserMotif(motif);
		return motifSelection;
	}
}
