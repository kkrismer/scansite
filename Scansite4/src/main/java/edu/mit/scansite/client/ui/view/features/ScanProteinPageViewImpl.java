package edu.mit.scansite.client.ui.view.features;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.mit.scansite.shared.transferobjects.User;
import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.widgets.features.ChooseAllMotifsWidget;
import edu.mit.scansite.client.ui.widgets.features.ChooseMotifWidget;
import edu.mit.scansite.client.ui.widgets.features.ChooseProteinWidget;
import edu.mit.scansite.client.ui.widgets.features.ChooseSelectedMotifsWidget;
import edu.mit.scansite.client.ui.widgets.features.ChooseUserFileMotifWidget;
import edu.mit.scansite.client.ui.widgets.features.DataSourceWidget;
import edu.mit.scansite.client.ui.widgets.features.HistogramSelectionWidget;
import edu.mit.scansite.client.ui.widgets.features.StringencyLevelWidget;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverAction;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverResult;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.IdentifierType;
import edu.mit.scansite.shared.transferobjects.states.ScanProteinPageState;

/**
 * @author Konstantin Krismer
 */
public class ScanProteinPageViewImpl extends ScanProteinPageView {
	interface ScanProteinPageViewImplUiBinder extends
			UiBinder<Widget, ScanProteinPageViewImpl> {
	}

	private static ScanProteinPageViewImplUiBinder uiBinder = GWT
			.create(ScanProteinPageViewImplUiBinder.class);

	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private Presenter presenter;
	private ChooseMotifWidget motifWidget;
	private Map<IdentifierType, List<DataSource>> identifierTypeToDataSourceMapping;

	@UiField
	ChooseProteinWidget chooseProteinWidget;

	@UiField
	RadioButton motifSpaceAllRadioButton;

	@UiField
	RadioButton motifSpaceSelectedRadioButton;

	@UiField
	RadioButton motifSpaceUserDefinedRadioButton;

	@UiField
	ChooseAllMotifsWidget chooseAllMotifsWidget;

	@UiField
	ChooseSelectedMotifsWidget chooseSelectedMotifsWidget;

	@UiField
	ChooseUserFileMotifWidget chooseUserFileMotifWidget;

	@UiField
	StringencyLevelWidget stringencyLevelWidget;

	@UiField(provided = true)
	CheckBox showDomainsCheckBox = new CheckBox();

	@UiField
	HistogramSelectionWidget histogramSelectionWidget;

	@UiField
	DataSourceWidget localizationDataSourceWidget;

	@UiField
	SubmitButton submitButton;

	public ScanProteinPageViewImpl(User user) {
		this.showDomainsCheckBox.getElement().setId("showDomainsCheckBoxId");
		initWidget(uiBinder.createAndBindUi(this));
		initDataSources();
		// set default motif widget
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				motifWidget = chooseAllMotifsWidget;
				chooseAllMotifsWidget.setUser(user);
                chooseSelectedMotifsWidget.setUser(user);
			}
		});
	}

	private void initDataSources() {
		dispatch.execute(new DataSourcesRetrieverAction(),
				new AsyncCallback<DataSourcesRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						showErrorMessage("Error fetching databases from server");
					}

					@Override
					public void onSuccess(DataSourcesRetrieverResult result) {
						hideMessage();
						identifierTypeToDataSourceMapping = new HashMap<>();
						for (DataSource dataSource : result.getDataSources()) {
							if (!identifierTypeToDataSourceMapping
									.containsKey(dataSource.getIdentifierType())) {
								identifierTypeToDataSourceMapping.put(
										dataSource.getIdentifierType(),
										new LinkedList<DataSource>());
							}
							identifierTypeToDataSourceMapping.get(
									dataSource.getIdentifierType()).add(
									dataSource);
						}
					}
				});
	}

	@UiHandler("chooseProteinWidget")
	void onChooseProteinWidgetDataSourceValueChange(
			ValueChangeEvent<DataSource> event) {
		List<DataSource> dataSources = retrieveCompatibleLocalizationDataSources(event
				.getValue().getIdentifierType());
		localizationDataSourceWidget.setDataSources(dataSources);
	}

	private List<DataSource> retrieveCompatibleLocalizationDataSources(
			IdentifierType identifierType) {
		List<DataSource> dataSources = new LinkedList<DataSource>();
		if (identifierTypeToDataSourceMapping != null && identifierType != null) {
			List<DataSource> compatibleDataSources = identifierTypeToDataSourceMapping
					.get(identifierType);
			if (compatibleDataSources != null) {
				for (DataSource dataSource : compatibleDataSources) {
					if (dataSource.getType().getShortName()
							.equals("localization")) {
						dataSources.add(dataSource);
					}
				}
			}
		}
		return dataSources;
	}

	@UiHandler("motifSpaceAllRadioButton")
	public void onMotifSpaceAllRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("scanMotifsAll").setAttribute("style",
				"display: block;");
		DOM.getElementById("scanMotifsSelected").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanMotifsUserDefined").setAttribute("style",
				"display: none;");
		motifWidget = chooseAllMotifsWidget;
	}

	@UiHandler("motifSpaceSelectedRadioButton")
	public void onMotifSpaceSelectedRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("scanMotifsAll").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanMotifsSelected").setAttribute("style",
				"display: block;");
		DOM.getElementById("scanMotifsUserDefined").setAttribute("style",
				"display: none;");
		motifWidget = chooseSelectedMotifsWidget;
	}

	@UiHandler("motifSpaceUserDefinedRadioButton")
	public void onMotifSpaceUserDefinedRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("scanMotifsAll").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanMotifsSelected").setAttribute("style",
				"display: none;");
		DOM.getElementById("scanMotifsUserDefined").setAttribute("style",
				"display: block;");
		motifWidget = chooseUserFileMotifWidget;
	}

	@UiHandler("submitButton")
	public void onSubmitButtonClick(ClickEvent event) {
		if (chooseProteinWidget.inputValidation()) {
			setSubmitButtonEnabled(false);
			hideMessage();
			presenter.onSubmitButtonClicked(chooseProteinWidget.getProtein(),
					motifWidget.getMotifSelection(), stringencyLevelWidget
							.getStringency(), showDomainsCheckBox.getValue(),
					histogramSelectionWidget.getHistogramDataSource(),
					histogramSelectionWidget.getHistogramTaxon(),
					localizationDataSourceWidget.getDataSource() != null
							&& localizationDataSourceWidget.getDataSource()
									.getId() == -1 ? null
							: localizationDataSourceWidget.getDataSource());
		} else {
			showWarningMessage("Input validation failed");
		}
	}

	@Override
	public String getPageTitle() {
		return "Scan Protein for Motifs";
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
		return NavigationEvent.PageId.FEATURE_SCAN_PROTEIN.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FEATURE_SCAN_PROTEIN;
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
	public ScanProteinPageState getState() {
		return new ScanProteinPageState(chooseProteinWidget.getState(),
				motifSpaceAllRadioButton.getValue(),
				motifSpaceSelectedRadioButton.getValue(),
				motifSpaceUserDefinedRadioButton.getValue(),
				chooseAllMotifsWidget.getState(),
				chooseSelectedMotifsWidget.getState(),
				chooseUserFileMotifWidget.getState(),
				stringencyLevelWidget.getState(),
				showDomainsCheckBox.getValue(),
				histogramSelectionWidget.getState(),
				localizationDataSourceWidget.getState());
	}

	@Override
	public void setState(final ScanProteinPageState state) {
		if (state != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					if (state.isMotifSpaceSelectedRadioButtonValue()) {
						motifSpaceSelectedRadioButton.setValue(true, true);
					} else if (state.isMotifSpaceUserDefinedRadioButton()) {
						motifSpaceUserDefinedRadioButton.setValue(true, true);
					} else {
						motifSpaceAllRadioButton.setValue(true, true);
					}
				}
			});
			chooseProteinWidget.setState(state.getChooseProteinWidgetState());
			chooseAllMotifsWidget.setState(state
					.getChooseAllMotifsWidgetState());
			chooseSelectedMotifsWidget.setState(chooseSelectedMotifsWidget
					.getState());
			chooseUserFileMotifWidget.setState(state
					.getChooseUserFileMotifWidgetState());
			stringencyLevelWidget.setState(state
					.getStringencyLevelWidgetState());
			showDomainsCheckBox.setValue(state.isShowDomains());
			histogramSelectionWidget.setState(state
					.getHistogramSelectionWidgetState());
			localizationDataSourceWidget.setState(state
					.getLocalizationDataSourceWidgetState());
		}
	}
}
