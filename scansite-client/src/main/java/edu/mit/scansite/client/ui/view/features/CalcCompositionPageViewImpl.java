package edu.mit.scansite.client.ui.view.features;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.features.ChooseProteinWidget;
import edu.mit.scansite.client.ui.widgets.features.ShowAminoAcidCompositionWidget;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.event.NavigationEvent;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.states.ChooseProteinWidgetState;

/**
 * @author Konstantin Krismer
 */
public class CalcCompositionPageViewImpl extends CalcCompositionPageView {
	interface CalcCompositionPageViewImplUiBinder extends
			UiBinder<Widget, CalcCompositionPageViewImpl> {
	}

	private static CalcCompositionPageViewImplUiBinder uiBinder = GWT
			.create(CalcCompositionPageViewImplUiBinder.class);

	private Presenter presenter;

	@UiField
	ChooseProteinWidget chooseProteinWidget;

	@UiField
	FlowPanel result;

	public CalcCompositionPageViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("submitButton")
	public void onSubmitButtonClick(ClickEvent event) {
		if (chooseProteinWidget.inputValidation()) {
			hideMessage();
			presenter.onSubmitButtonClicked(chooseProteinWidget.getProtein());
		} else {
			showWarningMessage("Input validation failed");
		}
	}

	@Override
	public String getPageTitle() {
		return "Calculate amino acid composition around S/T/Y sites";
	}

	@Override
	public boolean isMajorNavigationPage() {
		return true;
	}

	@Override
	public String getMajorPageId() {
		return getPageId();
	}

	@Override
	public String getPageId() {
		return NavigationEvent.PageId.FEATURE_CALC_COMPOSITION.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FEATURE_CALC_COMPOSITION;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setResult(LightWeightProtein protein, boolean isSuccess,
			String errorMessage) {
		result.clear();
		if (isSuccess) {
			hideMessage();
			result.add(new HTML("<h3>Result</h3>"));
			result.add(new ShowAminoAcidCompositionWidget(protein));
		} else {
			showErrorMessage(errorMessage);
		}
	}

	@Override
	public ChooseProteinWidgetState getState() {
		return chooseProteinWidget.getState();
	}

	@Override
	public void setState(ChooseProteinWidgetState state) {
		chooseProteinWidget.setState(state);
	}
}
