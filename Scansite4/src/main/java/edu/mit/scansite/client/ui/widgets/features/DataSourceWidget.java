package edu.mit.scansite.client.ui.widgets.features;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.dispatch.datasource.DataSourceTypesRetrieverAction;
import edu.mit.scansite.shared.dispatch.datasource.DataSourceTypesRetrieverResult;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverAction;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverResult;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.DataSourceType;
import edu.mit.scansite.shared.transferobjects.states.DataSourceWidgetState;

/**
 * @author Konstantin Krismer
 */
public class DataSourceWidget extends ScansiteWidget implements
		HasValueChangeHandlers<DataSource>, Stateful<DataSourceWidgetState> {

	private static final String DEFAULT_DB = "swissprot";

	private static DataSourceWidgetUiBinder uiBinder = GWT
			.create(DataSourceWidgetUiBinder.class);

	interface DataSourceWidgetUiBinder extends
			UiBinder<Widget, DataSourceWidget> {
	}

	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private Map<String, DataSource> dataSources = new HashMap<>();
	private Map<String, DataSourceType> dataSourceTypes = new HashMap<>();
	private DataSource dataSource;
	private String dataSourceTypeShortName = "proteins";

	@UiField
	LabelElement dataSourceLabel;

	@UiField
	ListBox dataSourceListBox;

	public @UiConstructor DataSourceWidget(final boolean initDataSources) {
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				dataSourceListBox.getElement().setId("dataSourceListBoxId");
				if (initDataSources) {
					initMappings(true);
				}
				setDataSourceLabel();
			}
		});
	}

	private void initMappings(final boolean retrieveDataSources) {
		dataSourceListBox.setEnabled(false);
		dispatch.execute(new DataSourceTypesRetrieverAction(),
				new AsyncCallback<DataSourceTypesRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance()
								.fireEvent(
										new MessageEvent(
												MessageEventPriority.ERROR,
												"Fetching data source types from server failed",
												this.getClass().toString(),
												caught));
					}

					@Override
					public void onSuccess(DataSourceTypesRetrieverResult result) {
						hideMessage();
						dataSourceListBox.setEnabled(true);
						for (DataSourceType dataSourceType : result
								.getDataSourceTypes()) {
							dataSourceTypes.put(dataSourceType.getShortName(),
									dataSourceType);
						}
						if (retrieveDataSources) {
							dispatch.execute(
									new DataSourcesRetrieverAction(
											dataSourceTypes
													.get(dataSourceTypeShortName)),
									new AsyncCallback<DataSourcesRetrieverResult>() {
										@Override
										public void onFailure(Throwable caught) {
											EventBus.instance()
													.fireEvent(
															new MessageEvent(
																	MessageEventPriority.ERROR,
																	"Fetching data sources from server failed",
																	this.getClass()
																			.toString(),
																	caught));
										}

										@Override
										public void onSuccess(
												DataSourcesRetrieverResult result) {
											if (result.getDataSources() != null
													&& !result.getDataSources()
															.isEmpty()) {
												hideMessage();
												dataSourceListBox
														.setEnabled(true);
												setDataSources(result
														.getDataSources());
											} else {
												dataSourceListBox
														.setEnabled(false);
												EventBus.instance()
														.fireEvent(
																new MessageEvent(
																		MessageEventPriority.ERROR,
																		"No "
																				+ dataSourceTypes
																						.get(dataSourceTypeShortName)
																						.getDisplayName()
																						.toLowerCase()
																				+ " data source available",
																		this.getClass()
																				.toString(),
																		null));
											}
										}
									});
						}
					}
				});
	}

	private void setDataSourceLabel() {
		if (dataSourceTypeShortName.equals("orthologs")) {
			dataSourceLabel.setInnerText("Orthology data source");
		} else if (dataSourceTypeShortName.equals("localization")) {
			dataSourceLabel.setInnerText("Localization data source");
		} else {
			dataSourceLabel.setInnerText("Protein data source");
		}
	}

	@UiHandler("dataSourceListBox")
	void onDataSourceListBoxChange(ChangeEvent event) {
		fireChanged(dataSources.get(dataSourceListBox
				.getValue(dataSourceListBox.getSelectedIndex())));
	}

	public void setDataSources(List<DataSource> dataSources) {
		dataSourceListBox.clear();
		this.dataSources.clear();
		DataSource defaultDataSource = null;
		if (dataSources != null && !dataSources.isEmpty()) {
			for (DataSource dataSource : dataSources) {
				dataSourceListBox.addItem(dataSource.getDisplayName(),
						dataSource.getShortName());
				if (dataSource.getShortName().equals(DEFAULT_DB)) {
					defaultDataSource = dataSource;
				}
				this.dataSources.put(dataSource.getShortName(), dataSource);
			}
			if (defaultDataSource == null && dataSources.size() > 0) {
				defaultDataSource = dataSources.get(0);
			}
			fireChanged(defaultDataSource);
		} else {
			fireChanged(new DataSource(-1));
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(final DataSource dataSource) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				fireChanged(dataSource);
			}
		});
	}

	protected void fireChanged(DataSource dataSource) {
		if (dataSource != null) {
			this.dataSource = dataSource;
			if (dataSource.getId() != -1) {
				for (int i = 0; i < dataSourceListBox.getItemCount(); ++i) {
					if (dataSourceListBox.getValue(i).equalsIgnoreCase(
							dataSource.getShortName())) {
						dataSourceListBox.setSelectedIndex(i);
					}
				}
				dataSourceListBox.setEnabled(true);
			} else {
				dataSourceListBox.setEnabled(false);
			}
			ValueChangeEvent.fire(this, dataSource);
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<DataSource> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public String getDataSourceTypeShortName() {
		return dataSourceTypeShortName;
	}

	public void setDataSourceTypeShortName(String dataSourceTypeShortName) {
		this.dataSourceTypeShortName = dataSourceTypeShortName;
		setDataSourceLabel();
		initMappings(true);
	}

	@Override
	public DataSourceWidgetState getState() {
		return new DataSourceWidgetState(getDataSource());
	}

	@Override
	public void setState(final DataSourceWidgetState state) {
		if (state != null) {
			setDataSource(state.getDataSource());
		}
	}
}
