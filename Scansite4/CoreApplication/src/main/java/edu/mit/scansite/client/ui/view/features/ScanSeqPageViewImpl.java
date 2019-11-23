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
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.widgets.features.DataSourceWidget;
import edu.mit.scansite.client.ui.widgets.features.DbRestrictionWidget;
import edu.mit.scansite.client.ui.widgets.features.RegExMethodWidget;
import edu.mit.scansite.client.ui.widgets.features.SeqPatMethodWidget;
import edu.mit.scansite.client.ui.widgets.features.SequenceMatchMethodWidget;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.OrganismClass;
import edu.mit.scansite.shared.transferobjects.states.ScanSeqPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ScanSeqPageViewImpl extends ScanSeqPageView {
	interface ScanSeqPageViewImplUiBinder extends
			UiBinder<Widget, ScanSeqPageViewImpl> {
	}

	private static ScanSeqPageViewImplUiBinder uiBinder = GWT
			.create(ScanSeqPageViewImplUiBinder.class);

	private Presenter presenter;
	private SequenceMatchMethodWidget methodWidget;

	@UiField
	RadioButton searchMethodSeqPatternRadioButton;

	@UiField
	RadioButton searchMethodRegExRadioButton;

	@UiField
	SeqPatMethodWidget seqPatMethodWidget;

	@UiField
	RegExMethodWidget regExMethodWidget;

	@UiField
	DataSourceWidget dataSourceWidget;

	@UiField
	DbRestrictionWidget dbRestrictionWidget;

	@UiField
	SubmitButton submitButton;

	public ScanSeqPageViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		// set default method
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				methodWidget = seqPatMethodWidget;
			}
		});
	}

	@UiHandler("searchMethodSeqPatternRadioButton")
	public void onSearchMethodSeqPatternRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("scanSeqSeqPattern").setAttribute("style",
				"display: block;");
		DOM.getElementById("scanSeqRegEx").setAttribute("style",
				"display: none;");
		methodWidget = seqPatMethodWidget;
	}

	@UiHandler("searchMethodRegExRadioButton")
	public void onSearchMethodRegExRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("scanSeqSeqPattern").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanSeqRegEx").setAttribute("style",
				"display: block;");
		methodWidget = regExMethodWidget;
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
			setSubmitButtonEnabled(false);
			hideMessage();
			presenter.onSubmitButtonClicked(methodWidget.getSequencePatterns(),
					dataSourceWidget.getDataSource(),
					dbRestrictionWidget.getRestrictionProperties(),
					methodWidget.showPhosphorylatedProteinsOnly());
		} else {
			showWarningMessage("Input validation failed");
		}
	}

	@Override
	public String getPageTitle() {
		return "Find Sequence Match";
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
		return NavigationEvent.PageId.FEATURE_SCAN_SEQ.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FEATURE_SCAN_SEQ;
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
	public void hideWaitSymbol() {
		Element waitSpanElement = DOM.getElementById("waitScan");
		waitSpanElement.setAttribute("style", "display: none;");
	}

	@Override
	public ScanSeqPageState getState() {
		return new ScanSeqPageState(
				searchMethodSeqPatternRadioButton.getValue(),
				searchMethodRegExRadioButton.getValue(),
				seqPatMethodWidget.getState(), regExMethodWidget.getState(),
				dataSourceWidget.getState(), dbRestrictionWidget.getState());
	}

	@Override
	public void setState(final ScanSeqPageState state) {
		if (state != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					if (state.isSearchMethodRegExRadioButtonValue()) {
						searchMethodRegExRadioButton.setValue(true, true);
					} else {
						searchMethodSeqPatternRadioButton.setValue(true, true);
					}
				}
			});
			seqPatMethodWidget.setState(state.getSeqPatMethodWidgetState());
			regExMethodWidget.setState(state.getRegExMethodWidgetState());
			dataSourceWidget.setState(state.getDataSourceWidgetState());
			dbRestrictionWidget.setState(state.getDbRestrictionWidgetState());
		}
	}
}
