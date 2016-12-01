package edu.mit.scansite.client.ui.view.features;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.widgets.features.ChooseMotifWidget;
import edu.mit.scansite.client.ui.widgets.features.ChooseQuickMotifWidget;
import edu.mit.scansite.client.ui.widgets.features.ChooseSelectedMotifsWidget;
import edu.mit.scansite.client.ui.widgets.features.ChooseUserFileMotifWidget;
import edu.mit.scansite.client.ui.widgets.features.DataSourceWidget;
import edu.mit.scansite.client.ui.widgets.features.DbRestrictionWidget;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.OrganismClass;
import edu.mit.scansite.shared.transferobjects.states.ScanDatabasePageState;

/**
 * @author Konstantin Krismer
 */
public class ScanDatabasePageViewImpl extends ScanDatabasePageView {
	interface ScanDbPageViewImplUiBinder extends
			UiBinder<Widget, ScanDatabasePageViewImpl> {
	}

	private static ScanDbPageViewImplUiBinder uiBinder = GWT
			.create(ScanDbPageViewImplUiBinder.class);

	private Presenter presenter;
	private ChooseMotifWidget motifWidget;

	@UiField
	RadioButton searchMethodDatabaseMotifRadioButton;

	@UiField
	RadioButton searchMethodDatabaseMotifsRadioButton;

	@UiField
	RadioButton searchMethodUserDefinedMotifsRadioButton;

	@UiField
	RadioButton searchMethodQuickMotifsRadioButton;

	@UiField
	ChooseSelectedMotifsWidget chooseSelectedMotifWidget;

	@UiField
	ChooseSelectedMotifsWidget chooseSelectedMotifsWidget;

	@UiField
	ChooseUserFileMotifWidget chooseUserFileMotifWidget;

	@UiField
	ChooseQuickMotifWidget chooseQuickMotifWidget;

	@UiField
	DataSourceWidget dataSourceWidget;

	@UiField
	DbRestrictionWidget dbRestrictionWidget;

	@UiField
	ListBox outputListSizeListBox;

	@UiField
	SubmitButton submitButton;

	public ScanDatabasePageViewImpl() {
		// this.showDomainsCheckBox.getElement().setId("showDomainsCheckBoxId");
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				outputListSizeListBox.getElement().setId(
						"outputListSizeListBoxId");
				motifWidget = chooseSelectedMotifWidget; // set default motif
															// widget
			}
		});
	}

	@UiHandler("searchMethodDatabaseMotifRadioButton")
	public void onSearchMethodDatabaseMotifRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("scanDbDbMotif").setAttribute("style",
				"display: block;");
		DOM.getElementById("scanDbDbMotifs").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanDbUploadMotif").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanDbQuickMotif").setAttribute("style",
				"display: none;");
		motifWidget = chooseSelectedMotifWidget;
	}

	@UiHandler("searchMethodDatabaseMotifsRadioButton")
	public void onSearchMethodDatabaseMotifsRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("scanDbDbMotif").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanDbDbMotifs").setAttribute("style",
				"display: block;");
		DOM.getElementById("scanDbUploadMotif").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanDbQuickMotif").setAttribute("style",
				"display: none;");
		motifWidget = chooseSelectedMotifsWidget;
	}

	@UiHandler("searchMethodUserDefinedMotifsRadioButton")
	public void onSearchMethodUserDefinedMotifsRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("scanDbDbMotif").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanDbDbMotifs").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanDbUploadMotif").setAttribute("style",
				"display: block;");
		DOM.getElementById("scanDbQuickMotif").setAttribute("style",
				"display: none;");
		motifWidget = chooseUserFileMotifWidget;
	}

	@UiHandler("searchMethodQuickMotifsRadioButton")
	public void onSearchMethodQuickMotifsRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("scanDbDbMotif").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanDbDbMotifs").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanDbUploadMotif").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanDbQuickMotif").setAttribute("style",
				"display: block;");
		motifWidget = chooseQuickMotifWidget;
	}

	@UiHandler("dataSourceWidget")
	void onDataSourceWidgetValueChange(ValueChangeEvent<DataSource> event) {
		if (event.getValue().getShortName().equalsIgnoreCase("yeast")) {
			dbRestrictionWidget.setOrganismClass(OrganismClass.FUNGI);
		} else {
			dbRestrictionWidget.setOrganismClass(OrganismClass.MAMMALIA);
		}
	}

	@UiHandler("submitButton")
	public void onSubmitButtonClick(ClickEvent event) {
		if (dbRestrictionWidget.inputValidation()) {
			if (motifWidget.getMotifSelection() != null && (motifWidget
					.getMotifSelection().getUserMotif() != null || !searchMethodUserDefinedMotifsRadioButton.getValue())) {
				setSubmitButtonEnabled(false);
				hideMessage();
				presenter.onSubmitButtonClicked(
						motifWidget.getMotifSelection(), dataSourceWidget
								.getDataSource(), dbRestrictionWidget
								.getRestrictionProperties(), Integer
								.parseInt(outputListSizeListBox
										.getValue(outputListSizeListBox
												.getSelectedIndex())));
			} else {
				showWarningMessage("Input validation failed (no user motif was uploaded)");
			}
		} else {
			showWarningMessage("Input validation failed");
		}
	}

	@Override
	public String getPageTitle() {
		return "Search a Sequence Database for Motifs";
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
		return NavigationEvent.PageId.FEATURE_SCAN_DB.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FEATURE_SCAN_DB;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setSubmitButtonEnabled(boolean enabled) {
		submitButton.setEnabled(enabled);
	}

	@Override
	public void showWaitSymbol() {
		Element waitSpanElement = DOM.getElementById("waitScan");
		waitSpanElement.setAttribute("style", "display: inline;");
	}

	@Override
	public void hideWaitImage() {
		Element waitSpanElement = DOM.getElementById("waitScan");
		waitSpanElement.setAttribute("style", "display: none;");
	}

	@Override
	public ScanDatabasePageState getState() {
		return new ScanDatabasePageState(
				searchMethodDatabaseMotifRadioButton.getValue(),
				searchMethodDatabaseMotifsRadioButton.getValue(),
				searchMethodUserDefinedMotifsRadioButton.getValue(),
				searchMethodQuickMotifsRadioButton.getValue(),
				chooseSelectedMotifWidget.getState(),
				chooseSelectedMotifsWidget.getState(),
				chooseUserFileMotifWidget.getState(),
				chooseQuickMotifWidget.getState(), dataSourceWidget.getState(),
				dbRestrictionWidget.getState(),
				outputListSizeListBox.getSelectedIndex());
	}

	@Override
	public void setState(final ScanDatabasePageState state) {
		if (state != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					if (state.isSearchMethodDatabaseMotifRadioButtonValue()) {
						searchMethodDatabaseMotifRadioButton.setValue(true,
								true);
					} else if (state
							.isSearchMethodDatabaseMotifsRadioButtonValue()) {
						searchMethodDatabaseMotifsRadioButton.setValue(true,
								true);
					} else if (state
							.isSearchMethodUserDefinedMotifsRadioButton()) {
						searchMethodUserDefinedMotifsRadioButton.setValue(true,
								true);
					} else {
						searchMethodQuickMotifsRadioButton.setValue(true, true);
					}
					outputListSizeListBox.setSelectedIndex(state
							.getOutputListSizeListBoxSelectedIndex());
				}
			});
			chooseSelectedMotifWidget.setState(state
					.getChooseSelectedMotifWidgetState());
			chooseSelectedMotifsWidget.setState(state
					.getChooseSelectedMotifsWidgetState());
			chooseUserFileMotifWidget.setState(state
					.getChooseUserFileMotifWidgetState());
			chooseQuickMotifWidget.setState(state
					.getChooseQuickMotifWidgetState());
			dataSourceWidget.setState(state.getDataSourceWidgetState());
			dbRestrictionWidget.setState(state.getDbRestrictionWidgetState());
		}
	}
}
