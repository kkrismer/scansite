package edu.mit.scansite.client.ui.widgets.features;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.transferobjects.GOTermEvidence;
import edu.mit.scansite.shared.transferobjects.LightWeightLocalization;
import edu.mit.scansite.shared.transferobjects.Localization;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.Parameter;

/**
 * @author Konstantin Krismer
 */
public class DisplayLocalization extends ScansiteWidget {

	private static DisplayLocalizationUiBinder uiBinder = GWT
			.create(DisplayLocalizationUiBinder.class);

	interface DisplayLocalizationUiBinder extends
			UiBinder<Widget, DisplayLocalization> {
	}

	@UiField
	DisplayGeneralPropertiesWidget displayLocalizationPropertiesWidget;

	public DisplayLocalization() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setLocalization(Localization localization) {
		if (localization != null) {
			List<Parameter> parameters = new LinkedList<>();

			parameters.add(new Parameter("Localization", localization.getType()
					.getName()));
			parameters
					.add(new Parameter(
							"<span title=\"reliability score of a prediction on a 0 - 100 scale with 100 being the most confident prediction\">Prediction Score</span>",
							localization.getScore()));
			if (localization.getGoTerms() != null) {
				parameters.add(new Parameter("GO terms", true));
				for (GOTermEvidence goTerm : localization.getGoTerms()) {
					if (goTerm.getEvidenceCode().getCode() == null) {
						parameters.add(new Parameter(
								goTerm.getGoTerm().getId(), goTerm.getGoTerm()
										.getName()));
					} else {
						parameters.add(new Parameter(
								goTerm.getGoTerm().getId(), goTerm.getGoTerm()
										.getName()
										+ " (<span title=\""
										+ goTerm.getEvidenceCode().getName()
										+ "\">"
										+ goTerm.getEvidenceCode().getCode()
										+ "</span>)"));
					}
				}
				parameters.add(new Parameter("GO terms", false));
			}

			displayLocalizationPropertiesWidget.setProperties(parameters);
		}
	}

	public void setLocalization(
			Map<Motif, LightWeightLocalization> localizations) {
		if (localizations != null && !localizations.isEmpty()) {
			List<Parameter> parameters = new LinkedList<>();
			List<Motif> sortedMotifs = new LinkedList<>(localizations.keySet());
			Collections.sort(sortedMotifs, new Comparator<Motif>() {
				@Override
				public int compare(Motif arg0, Motif arg1) {
					return arg0.getDisplayName().compareTo(
							arg1.getDisplayName());
				}
			});

			for (Motif motif : sortedMotifs) {
				parameters.add(new Parameter(motif.getDisplayName(), true));
				parameters.add(new Parameter("Localization", localizations
						.get(motif).getType().getName()));
				parameters
						.add(new Parameter(
								"<span title=\"reliability score of a prediction on a 0 - 100 scale with 100 being the most confident prediction\">Prediction Score</span>",
								localizations.get(motif).getScore()));
				parameters.add(new Parameter(motif.getDisplayName(), false));
			}

			displayLocalizationPropertiesWidget.setProperties(parameters);
		}
	}
}
