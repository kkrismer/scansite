package edu.mit.scansite.client.ui.widgets.features;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverAction;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverResult;
import edu.mit.scansite.shared.event.EventBus;
import edu.mit.scansite.shared.event.MessageEvent;
import edu.mit.scansite.shared.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.IdentifierType;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.states.ChooseOrthologyProteinWidgetState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ChooseOrthologyProteinWidget extends ScansiteWidget implements
		Stateful<ChooseOrthologyProteinWidgetState> {
	interface ChooseOrthologyProteinWidgetUiBinder extends
			UiBinder<Widget, ChooseOrthologyProteinWidget> {
	}

	private static ChooseOrthologyProteinWidgetUiBinder uiBinder = GWT
			.create(ChooseOrthologyProteinWidgetUiBinder.class);

	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private Map<IdentifierType, List<DataSource>> identifierTypeToDataSourceMapping;

	@UiField
	DataSourceWidget orthologyDataSourceWidget;

	@UiField
	ChooseProteinWidget chooseProteinWidget;

	public ChooseOrthologyProteinWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		initDataSources();
	}

	private void initDataSources() {
		dispatch.execute(new DataSourcesRetrieverAction(false),
				new AsyncCallback<DataSourcesRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										"Fetching data sources from server failed", this.getClass()
												.toString(), caught));
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
						orthologyDataSourceWidget
								.setDataSource(orthologyDataSourceWidget
										.getDataSource()); // raise event
					}
				});
	}

	@UiHandler("orthologyDataSourceWidget")
	void onOrthologyDataSourceWidgetValueChange(
			ValueChangeEvent<DataSource> event) {
		List<DataSource> dataSources = retrieveCompatibleProteinDataSources(event
				.getValue().getIdentifierType());
		chooseProteinWidget.setDataSources(dataSources);
	}

	private List<DataSource> retrieveCompatibleProteinDataSources(
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

	public DataSource getOrthologyDataSource() {
		return orthologyDataSourceWidget.getDataSource();
	}

	public LightWeightProtein getProtein() {
		return chooseProteinWidget.getProtein();
	}

	@Override
	public ChooseOrthologyProteinWidgetState getState() {
		return new ChooseOrthologyProteinWidgetState(
				orthologyDataSourceWidget.getState(),
				chooseProteinWidget.getState());
	}

	@Override
	public void setState(ChooseOrthologyProteinWidgetState state) {
		if (state != null) {
			orthologyDataSourceWidget
					.setState(state.getDataSourceWidgetState());
			chooseProteinWidget.setState(state.getChooseProteinWidgetState());
		}
	}
}
