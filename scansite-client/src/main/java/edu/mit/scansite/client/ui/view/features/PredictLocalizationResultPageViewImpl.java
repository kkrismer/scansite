package edu.mit.scansite.client.ui.view.features;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.features.DisplayGeneralPropertiesWidget;
import edu.mit.scansite.client.ui.widgets.features.DisplayLocalization;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult;
import edu.mit.scansite.shared.dispatch.features.PredictMotifsLocalizationResult;
import edu.mit.scansite.shared.dispatch.features.PredictProteinsLocalizationResult;
import edu.mit.scansite.shared.event.NavigationEvent;
import edu.mit.scansite.shared.transferobjects.Parameter;
import edu.mit.scansite.shared.transferobjects.states.PredictLocalizationResultPageState;

/**
 * @author Konstantin Krismer
 */
public class PredictLocalizationResultPageViewImpl extends
		PredictLocalizationResultPageView {

	private static PredictLocalizationResultPageViewImplUiBinder uiBinder = GWT
			.create(PredictLocalizationResultPageViewImplUiBinder.class);

	interface PredictLocalizationResultPageViewImplUiBinder extends
			UiBinder<Widget, PredictLocalizationResultPageViewImpl> {
	}

	private PredictLocalizationResult result = null;

	@UiField
	DisplayGeneralPropertiesWidget displayScanPropertiesWidget;

	@UiField
	DisplayGeneralPropertiesWidget displayScanResultPropertiesWidget;

	@UiField
	DisplayLocalization displayLocalization;

	public PredictLocalizationResultPageViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setResult(final PredictLocalizationResult result) {
		this.result = result;
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				List<Parameter> scanParameters = new LinkedList<>();
				scanParameters.add(new Parameter("Localization data source",
						result.getLocalizationDataSource().getDisplayName()));
				if (result instanceof PredictProteinsLocalizationResult) {
					scanParameters.add(new Parameter("Protein",
							((PredictProteinsLocalizationResult) result)
									.getProtein().toString()));
				} else {
					scanParameters.add(new Parameter("Motif class",
							((PredictMotifsLocalizationResult) result)
									.getMotifClass().getName()));
				}

				displayScanPropertiesWidget.setProperties(scanParameters);

				List<Parameter> scanResultParameters = new LinkedList<>();
				scanResultParameters.add(new Parameter(
						"Total number of protein localizations", result
								.getTotalProteinLocalizations()));
				displayScanResultPropertiesWidget
						.setProperties(scanResultParameters);

				if (result.isSuccess()) {
					if (result instanceof PredictProteinsLocalizationResult) {
						displayLocalization
								.setLocalization(((PredictProteinsLocalizationResult) result)
										.getLocalization());
					} else {
						displayLocalization
								.setLocalization(((PredictMotifsLocalizationResult) result)
										.getLocalizations());
					}

				} else {
					showErrorMessage(result.getErrorMessage());
				}
			}
		});
	}

	@Override
	public String getPageTitle() {
		return "Predict cellular localization of a protein - Result";
	}

	@Override
	public boolean isMajorNavigationPage() {
		return false;
	}

	@Override
	public String getMajorPageId() {
		return NavigationEvent.PageId.FEATURE_PREDICT_LOCALIZATION.getId();
	}

	@Override
	public String getPageId() {
		return NavigationEvent.PageId.FEATURE_PREDICT_LOCALIZATION_RESULT
				.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FEATURE_PREDICT_LOCALIZATION_RESULT;
	}

	@Override
	public PredictLocalizationResultPageState getState() {
		return new PredictLocalizationResultPageState(result);
	}

	@Override
	public void setState(PredictLocalizationResultPageState state) {
		if (state != null) {
			setResult(state.getPredictLocalizationResult());
		}
	}
}
