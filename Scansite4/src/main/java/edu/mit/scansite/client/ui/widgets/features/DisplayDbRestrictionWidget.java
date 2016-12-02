package edu.mit.scansite.client.ui.widgets.features;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.transferobjects.OrganismClass;
import edu.mit.scansite.shared.transferobjects.Parameter;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;

/**
 * @author Konstantin Krismer
 */
public class DisplayDbRestrictionWidget extends ScansiteWidget {

	private static DisplayDbRestrictionWidgetUiBinder uiBinder = GWT
			.create(DisplayDbRestrictionWidgetUiBinder.class);

	interface DisplayDbRestrictionWidgetUiBinder extends
			UiBinder<Widget, DisplayDbRestrictionWidget> {
	}

	private boolean showSequenceRegExField = true;

	@UiField
	DisplayGeneralPropertiesWidget displayRestrictionPropertiesWidget;

	public DisplayDbRestrictionWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setRestrictionProperties(
			RestrictionProperties restrictionProperties) {
		List<Parameter> parameters = new LinkedList<>();
		boolean isApplicable = false;

		if (restrictionProperties.isDefault()) {
			displayRestrictionPropertiesWidget
					.setTitle("Restrictions (default)");
		} else {
			displayRestrictionPropertiesWidget.setTitle("Restrictions");
		}

		if (restrictionProperties.getOrganismClass().equals(
				OrganismClass.MAMMALIA)) {
			parameters.add(new Parameter("Organism class",
					restrictionProperties.getOrganismClass().getDisplayName()
							+ " (default)", false, true));
		} else {
			parameters.add(new Parameter("Organism class",
					restrictionProperties.getOrganismClass().getDisplayName()));
		}

		String temp = "from "
				+ (restrictionProperties.getIsoelectricPointFrom() != null ? String
						.valueOf(restrictionProperties
								.getIsoelectricPointFrom()) : "0");
		if (restrictionProperties.getIsoelectricPointTo() != null) {
			temp += " to "
					+ String.valueOf(restrictionProperties
							.getIsoelectricPointTo());
		}
		if (restrictionProperties.getIsoelectricPointFrom() != null
				|| restrictionProperties.getIsoelectricPointTo() != null) {
			parameters.add(new Parameter("Isoelectric point", temp));
			isApplicable = true;
		} else {
			parameters.add(new Parameter("Isoelectric point", "not set", false,
					true));
		}

		temp = "from "
				+ (restrictionProperties.getMolecularWeightFrom() != null ? String
						.valueOf(restrictionProperties.getMolecularWeightFrom())
						: "0");
		if (restrictionProperties.getMolecularWeightTo() != null) {
			temp += " to "
					+ String.valueOf(restrictionProperties
							.getMolecularWeightTo());
		}
		if (restrictionProperties.getMolecularWeightFrom() != null
				|| restrictionProperties.getMolecularWeightTo() != null) {
			parameters.add(new Parameter("Molecular weight", temp));
			isApplicable = true;
		} else {
			parameters.add(new Parameter("Molecular weight", "not set", false,
					true));
		}

		if (isApplicable) {
			parameters.add(new Parameter("Number of phoshorylation sites",
					Integer.toString(restrictionProperties
							.getPhosphorylatedSites())));
		} else {
			parameters.add(new Parameter("Number of phoshorylation sites",
					"not applicable", false, true));
		}

		if (restrictionProperties.getSpeciesRegEx() != null) {
			parameters.add(new Parameter("Species restriction",
					restrictionProperties.getSpeciesRegEx()));
		} else {
			parameters.add(new Parameter("Species restriction", "not set",
					false, true));
		}

		if (restrictionProperties.getKeywordRegEx() != null) {
			parameters.add(new Parameter("Keyword restriction",
					restrictionProperties.getKeywordRegEx()));
		} else {
			parameters.add(new Parameter("Keyword restriction", "not set",
					false, true));
		}

		if (showSequenceRegExField) {
			if (restrictionProperties.getSequenceRegEx() != null
					&& !restrictionProperties.getSequenceRegEx().isEmpty()) {
				parameters.add(new Parameter("Sequence restriction",
						restrictionProperties.getSequenceRegEx().get(0)));
			} else {
				parameters.add(new Parameter("Sequence restriction", "not set",
						false, true));
			}
		}

		displayRestrictionPropertiesWidget.setProperties(parameters);
	}

	public boolean isShowSequenceRegExField() {
		return showSequenceRegExField;
	}

	public void setShowSequenceRegExField(boolean showSequenceRegExField) {
		this.showSequenceRegExField = showSequenceRegExField;
	}
}
