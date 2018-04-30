package edu.mit.scansite.client.ui.widgets.features;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.Parameter;

/**
 * @author Konstantin Krismer
 */
public class DisplayMotifSelectionWidget extends ScansiteWidget {
	interface DisplayMotifSelectionWidgetUiBinder extends
			UiBinder<Widget, DisplayMotifSelectionWidget> {
	}

	private static DisplayMotifSelectionWidgetUiBinder uiBinder = GWT
			.create(DisplayMotifSelectionWidgetUiBinder.class);

	@UiField
	DisplayGeneralPropertiesWidget displayMotifPropertiesWidget;

	public DisplayMotifSelectionWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setMotifSelection(MotifSelection motifSelection) {
		List<Parameter> parameters = new LinkedList<>();
		boolean isApplicable = false;

		if (motifSelection.isDefault()) {
			displayMotifPropertiesWidget.setTitle("Motif selection (default)");
		} else {
			displayMotifPropertiesWidget.setTitle("Motif selection");
		}

		if (motifSelection.getUserMotif() != null) {
			parameters.add(new Parameter("Selected motifs", motifSelection
					.getUserMotif().getDisplayName()
					+ " <span class=\"default\">(user input)</span>"));
		} else {
			isApplicable = true;
			if (motifSelection.getMotifShortNames() != null
					&& !motifSelection.getMotifShortNames().isEmpty()) {
				String motifs = "";
				for (String motifShortName : motifSelection
						.getMotifShortNames()) {
					if (!motifs.isEmpty()) {
						motifs += ", ";
					}
					motifs += motifShortName;
				}
				parameters.add(new Parameter("Selected motifs", motifs));
			} else {
				parameters.add(new Parameter("Selected motifs",
						"all motifs of motif class (default)", false, true));
			}
		}
		
		if (isApplicable) {
			if(motifSelection.getMotifClass() == null) {
				parameters
				.add(new Parameter("Motif class", MotifClass.MAMMALIAN.getName() + " (default)",
						false, true));
			} else if (motifSelection.getMotifClass().equals(MotifClass.MAMMALIAN)) {
				parameters
						.add(new Parameter("Motif class", motifSelection
								.getMotifClass().getName() + " (default)",
								false, true));
			} else {
				parameters.add(new Parameter("Motif class", motifSelection
						.getMotifClass().getName()));
			}
		} else {
			parameters.add(new Parameter("Motif class", "not applicable",
					false, true));
		}

		Collections.reverse(parameters);
		
		displayMotifPropertiesWidget.setProperties(parameters);
	}
}
