package edu.mit.scansite.client.ui.view.features;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.features.ChooseProteinWidget;
import edu.mit.scansite.client.ui.widgets.features.DataSourceWidget;
import edu.mit.scansite.client.ui.widgets.motifs.MotifClassWidget;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverAction;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverResult;
import edu.mit.scansite.shared.event.NavigationEvent;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.IdentifierType;
import edu.mit.scansite.shared.transferobjects.states.PredictLocalizationPageState;

/**
 * @author Konstantin Krismer
 */
public class PredictLocalizationPageViewImpl extends
		PredictLocalizationPageView {

	private static PredictLocalizationPageViewImplUiBinder uiBinder = GWT
			.create(PredictLocalizationPageViewImplUiBinder.class);

	interface PredictLocalizationPageViewImplUiBinder extends
			UiBinder<Widget, PredictLocalizationPageViewImpl> {
	}

	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private Map<IdentifierType, List<DataSource>> identifierTypeToDataSourceMapping;
	private Presenter presenter;
	private boolean isProteinsScope = true;

	@UiField
	DataSourceWidget localizationDataSourceWidget;

	@UiField
	RadioButton scopeProteinsRadioButton;

	@UiField
	RadioButton scopeMotifsRadioButton;

	@UiField
	ChooseProteinWidget chooseProteinWidget;

	@UiField
	MotifClassWidget motifClassWidget;
	
	@UiField
	SubmitButton submitButton;

	public PredictLocalizationPageViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		initDataSources();
	}

	private void initDataSources() {
		dispatch.execute(new DataSourcesRetrieverAction(false),
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
						localizationDataSourceWidget
								.setDataSource(localizationDataSourceWidget
										.getDataSource()); // raise event
					}
				});
	}

	@UiHandler("localizationDataSourceWidget")
	void onLocalizationDataSourceWidgetValueChange(
			ValueChangeEvent<DataSource> event) {
		List<DataSource> dataSources = retrieveCompatibleProteinDataSource(event
				.getValue().getIdentifierType());
		chooseProteinWidget.setDataSources(dataSources);

	}

	private List<DataSource> retrieveCompatibleProteinDataSource(
			IdentifierType identifierType) {
		List<DataSource> dataSources = new LinkedList<DataSource>();
		if (identifierTypeToDataSourceMapping != null && identifierType != null) {
			List<DataSource> compatibleDataSources = identifierTypeToDataSourceMapping
					.get(identifierType);
			if (compatibleDataSources != null) {
				for (DataSource dataSource : compatibleDataSources) {
					if (dataSource.getType().getShortName().equals("proteins")) {
						dataSources.add(dataSource);
					}
				}
			}
		}
		return dataSources;
	}

	@UiHandler("scopeProteinsRadioButton")
	public void onScopeProteinsRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("scopeProteins").setAttribute("style",
				"display: block;");
		DOM.getElementById("scopeMotifs").setAttribute("style",
				"display: none;");
		isProteinsScope = true;
	}

	@UiHandler("scopeMotifsRadioButton")
	public void onScopeMotifsRadioButtonValueChange(ValueChangeEvent<Boolean> e) {
		DOM.getElementById("scopeProteins").setAttribute("style",
				"display: none;");
		DOM.getElementById("scopeMotifs").setAttribute("style",
				"display: block;");
		isProteinsScope = false;
	}

	@UiHandler("submitButton")
	public void onSubmitButtonClick(ClickEvent event) {
		setSubmitButtonEnabled(false);
		hideMessage();
		if(isProteinsScope) {
			presenter.onSubmitButtonClicked(
					localizationDataSourceWidget.getDataSource(),
					chooseProteinWidget.getProtein());
		} else {
			presenter.onSubmitButtonClicked(
					localizationDataSourceWidget.getDataSource(),
					motifClassWidget.getMotifClass());
		}
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
	public String getPageTitle() {
		return "Predict cellular localization of a protein";
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
		return NavigationEvent.PageId.FEATURE_PREDICT_LOCALIZATION.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FEATURE_PREDICT_LOCALIZATION;
	}

	@Override
	public PredictLocalizationPageState getState() {
		return new PredictLocalizationPageState(
				localizationDataSourceWidget.getState(),
				chooseProteinWidget.getState());
	}

	@Override
	public void setState(PredictLocalizationPageState state) {
		if (state != null) {
			localizationDataSourceWidget.setState(state
					.getDataSourceWidgetState());
			chooseProteinWidget.setState(state.getChooseProteinWidgetState());
		}
	}
}
