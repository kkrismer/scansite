package edu.mit.scansite.client.ui.widgets.features;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.states.HistogramSelectionWidgetState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramSelectionWidget extends ScansiteWidget implements
		Stateful<HistogramSelectionWidgetState> {
	interface HistogramSelectionWidgetUiBinder extends
			UiBinder<Widget, HistogramSelectionWidget> {
	}

	private static HistogramSelectionWidgetUiBinder uiBinder = GWT
			.create(HistogramSelectionWidgetUiBinder.class);

	private int defaultIdx = 0;

	@UiField
	ListBox referenceProteomeListBox;

	@UiField
	CheckBox referenceProteomeCheckBox;

	public HistogramSelectionWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				for (int i = 0; i < ScansiteConstants.HIST_DEFAULT_TAXON_NAMES.length; ++i) {
					String text = ScansiteConstants.HIST_DEFAULT_TAXON_NAMES[i];
					if (i == ScansiteConstants.HIST_DEFAULT_INDEX) {
						text += " (default)";
						defaultIdx = i;
					}
					referenceProteomeListBox.addItem(text, String.valueOf(i));
				}
				referenceProteomeListBox.setSelectedIndex(defaultIdx);
			}
		});
	}

	@UiHandler("referenceProteomeCheckBox")
	public void onReferenceProteomeCheckBoxValueChange(
			ValueChangeEvent<Boolean> event) {
		if (referenceProteomeCheckBox.getValue()) {
			referenceProteomeListBox.getElement().removeAttribute("disabled");
		} else {
			referenceProteomeListBox.getElement().setAttribute("disabled",
					"disabled");
			referenceProteomeListBox.setSelectedIndex(defaultIdx);
		}
	}

	public String getHistogramTaxon() {
		return ScansiteConstants.HIST_DEFAULT_TAXON_NAMES[referenceProteomeListBox
				.getSelectedIndex()];
	}

	public String getHistogramDataSource() {
		return ScansiteConstants.HIST_DEFAULT_DATASOURCE_SHORTS[referenceProteomeListBox
				.getSelectedIndex()];
	}

	public boolean useNonStandardHistogram() {
		return referenceProteomeCheckBox.getValue();
	}

	public void setMotifClassDefault(final MotifClass motifClass) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				int selectedIdx = ScansiteConstants.HIST_DEFAULT_INDEX;
				if (MotifClass.YEAST.equals(motifClass)) {
					selectedIdx = selectedIdx > 0 ? 0 : 1; // just in case the
															// defaultindex
															// changes
				}
				setSelectedIndex(selectedIdx);
			}
		});
	}

	private void setSelectedIndex(final int idx) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				referenceProteomeListBox.setSelectedIndex(idx);
			}
		});
	}

	@Override
	public HistogramSelectionWidgetState getState() {
		return new HistogramSelectionWidgetState(
				referenceProteomeCheckBox.getValue(),
				referenceProteomeListBox.getSelectedIndex());
	}

	@Override
	public void setState(final HistogramSelectionWidgetState state) {
		if (state != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					referenceProteomeCheckBox.setValue(
							state.isReferenceProteomeCheckBoxValue(), true);
				}
			});
			setSelectedIndex(state.getReferenceProteomeListBoxSelectedIndex());
		}
	}
}
