package edu.mit.scansite.client.ui.widgets.features;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.event.ProteinScanEvent;
import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.DbSearchScanResultSiteComparator;
import edu.mit.scansite.shared.DbSearchScanResultSiteComparator.ComparableFields;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.URIs;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanResult;
import edu.mit.scansite.shared.dispatch.features.DbSearchHistogramRetrieverAction;
import edu.mit.scansite.shared.dispatch.features.DbSearchHistogramRetrieverResult;
import edu.mit.scansite.shared.dispatch.features.ProteinScanAction;
import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;
import edu.mit.scansite.shared.transferobjects.DatabaseSearchScanResultSite;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.MotifSelection;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DatabaseScanResultSiteTable extends ScansiteWidget {

	public static final String HISTORY_KEY_PROT = "acc";
	public static final String HISTORY_KEY_DB = "db";
	private static final String EMPTY_CELL_TEXT = "";

	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private VerticalPanel mainPanel = new VerticalPanel();

	private DatabaseScanResult result;
	private CellTable<DatabaseSearchScanResultSite> resultTable;
	private ListDataProvider<DatabaseSearchScanResultSite> dataProvider = new ListDataProvider<DatabaseSearchScanResultSite>();

	private Column<DatabaseSearchScanResultSite, String> submitColumn = getColumn(
			new ClickableTextCell(), new GetValue<String>() {
				@Override
				public String getValue(DatabaseSearchScanResultSite site) {
					if (site == null) {
						return EMPTY_CELL_TEXT;
					}
					return "Scan!";
				}
			}, new FieldUpdater<DatabaseSearchScanResultSite, String>() {
				@Override
				public void update(int index,
						DatabaseSearchScanResultSite site, String value) {
					ProteinScanAction action = new ProteinScanAction(
							site.getProtein(),
							new MotifSelection(),
							HistogramStringency.STRINGENCY_HIGH,
							false,
							ScansiteConstants.HIST_DEFAULT_DATASOURCE_SHORTS[ScansiteConstants.HIST_DEFAULT_INDEX],
							ScansiteConstants.HIST_DEFAULT_TAXON_NAMES[ScansiteConstants.HIST_DEFAULT_INDEX],
							result.getLocalizationDataSource());

					final WaitPopup waitPopup = new WaitPopup();
					waitPopup.center();
					dispatch.execute(action,
							new AsyncCallback<ProteinScanResult>() {
								@Override
								public void onFailure(Throwable caught) {
									waitPopup.hide();
									EventBus.instance()
											.fireEvent(
													new MessageEvent(
															MessageEventPriority.ERROR,
															"Server-side error",
															this.getClass()
																	.toString(),
															caught));
								}

								@Override
								public void onSuccess(ProteinScanResult result) {
									waitPopup.hide();
									if (result.isSuccess()) {
										EventBus.instance().fireEvent(
												new ProteinScanEvent(result));
									} else {
										EventBus.instance()
												.fireEvent(
														new MessageEvent(
																MessageEventPriority.ERROR,
																result.getFailureMessage(),
																this.getClass()
																		.toString(),
																null));
									}
								}
							});
				}
			});

	private Column<DatabaseSearchScanResultSite, String> scoreColumn = getColumn(
			new ClickableTextCell(), new GetValue<String>() {
				@Override
				public String getValue(DatabaseSearchScanResultSite site) {
					if (site == null) {
						return EMPTY_CELL_TEXT;
					}
					Double s = site.getScore();
					return ((s == null) ? EMPTY_CELL_TEXT : String
							.valueOf(NumberFormat.getFormat("0.000").format(s)));
				}
			}, new FieldUpdater<DatabaseSearchScanResultSite, String>() {
				@Override
				public void update(int index,
						DatabaseSearchScanResultSite site, String value) {
					if (result.getHistogramBasePath() != null) {
						dispatch.execute(
								new DbSearchHistogramRetrieverAction(site,
										result.getHistogramBasePath(),
										resultTable.getRowElement(index)
												.getAbsoluteBottom(),
										resultTable.getRowElement(index)
												.getAbsoluteLeft()),
								new AsyncCallback<DbSearchHistogramRetrieverResult>() {
									@Override
									public void onFailure(Throwable caught) {
										EventBus.instance()
												.fireEvent(
														new MessageEvent(
																MessageEventPriority.ERROR,
																"Fetching reference histogram from server failed",
																this.getClass()
																		.toString(),
																caught));
									}

									@Override
									public void onSuccess(
											DbSearchHistogramRetrieverResult result) {
										if (result.getHistogramFilePath() != null) {
											SafeHtmlBuilder builder = new SafeHtmlBuilder();
											builder.appendHtmlConstant("<img alt=\"Histogram\" src=\""
													+ result.getHistogramFilePath()
													+ "\"/>");
											showPopupPanel(
													result.getTopPosition(),
													result.getLeftPosition(),
													builder.toSafeHtml());
										}
									}
								});
					}
				}
			});

	private Column<DatabaseSearchScanResultSite, String> proteinAccColumn = getColumn(
			new ClickableTextCell(), new GetValue<String>() {
				@Override
				public String getValue(DatabaseSearchScanResultSite site) {
					if (site == null || site.getProtein() == null) {
						return EMPTY_CELL_TEXT;
					}
					return site.getProtein().getIdentifier();
				}
			}, new FieldUpdater<DatabaseSearchScanResultSite, String>() {
				@Override
				public void update(int index,
						DatabaseSearchScanResultSite site, String value) {
					if (site.getProtein() != null) {
						Window.open(URIs.getDirectIdentifierInfoLink(site
								.getProtein()), "_blank", "");
					}
				}
			});

	private TextColumn<DatabaseSearchScanResultSite> proteinInfoColumn = new TextColumn<DatabaseSearchScanResultSite>() {
		@Override
		public String getValue(DatabaseSearchScanResultSite site) {
			if (site == null || site.getProtein() == null
					|| site.getProtein().getAnnotations() == null
					|| site.getProtein().getAnnotations().isEmpty()) {
				return EMPTY_CELL_TEXT;
			}
			StringBuilder s = new StringBuilder();
			HashMap<String, Set<String>> anns = site.getProtein()
					.getAnnotations();
			boolean first = true;
			for (String key : anns.keySet()) {
				s.append(key.substring(0, 1).toUpperCase()).append(
						key.substring(1));
				if (anns.get(key).size() > 1) {
					s.append('s');
				}
				s.append(": ");
				first = true;
				for (String a : anns.get(key)) {
					if (first) {
						first = false;
					} else {
						s.append(", ");
					}
					s.append(a);
				}
				if (!key.endsWith(";")) {
					s.append("; ");
				}
			}
			return s.toString();
		}
	};

	private SiteSequenceColumn[] siteSequenceColumns;

	private class SiteSequenceColumn extends
			TextColumn<DatabaseSearchScanResultSite> {
		private int index = 0;

		public SiteSequenceColumn(int index) {
			super();
			this.index = index > 0 ? index : 0;
		}

		@Override
		public String getValue(DatabaseSearchScanResultSite site) {
			if (site == null) {
				return EMPTY_CELL_TEXT;
			}
			if (site.isMultiple()) {
				return String.valueOf(site.getSites().get(index)
						.getSiteSequence());
			} else {
				return String.valueOf(site.getSite().getSiteSequence());
			}
		}
	};

	private SitePositionColumn[] sitePositionColumns;

	private class SitePositionColumn extends
			TextColumn<DatabaseSearchScanResultSite> {
		private int index = 0;

		public SitePositionColumn(int index) {
			super();
			this.index = index > 0 ? index : 0;
		}

		@Override
		public String getValue(DatabaseSearchScanResultSite site) {
			if (site == null) {
				return EMPTY_CELL_TEXT;
			}
			if (site.isMultiple()) {
				return String.valueOf(site.getSites().get(index).getSite());
			} else {
				return String.valueOf(site.getSite().getSite());
			}
		}
	};

	private TextColumn<DatabaseSearchScanResultSite> mwColumn = new TextColumn<DatabaseSearchScanResultSite>() {
		@Override
		public String getValue(DatabaseSearchScanResultSite site) {
			if (site == null || site.getProtein() == null) {
				return EMPTY_CELL_TEXT;
			}
			Double mw = site.getProtein().getMolecularWeight();
			return ((mw == null) ? EMPTY_CELL_TEXT : String
					.valueOf(NumberFormat.getFormat("0.0").format(mw)));
		}
	};

	private TextColumn<DatabaseSearchScanResultSite> piColumn = new TextColumn<DatabaseSearchScanResultSite>() {
		@Override
		public String getValue(DatabaseSearchScanResultSite site) {
			if (site == null || site.getProtein() == null) {
				return EMPTY_CELL_TEXT;
			}
			Double pi = site.getProtein().getpI();
			return ((pi == null) ? EMPTY_CELL_TEXT : String
					.valueOf(NumberFormat.getFormat("0.00").format(pi)));
		}
	};

	public DatabaseScanResultSiteTable(DatabaseScanResult result) {
		initWidget(mainPanel);
		this.result = result;
		init();
	}

	public DatabaseScanResult getResult() {
		return result;
	}

	private void init() {
		resultTable = new CellTable<DatabaseSearchScanResultSite>(
				DatabaseSearchScanResultSite.KEY_PROVIDER);
		resultTable.setWidth("100%");

		sitePositionColumns = new SitePositionColumn[result
				.getMotifDisplayNames().size()];
		siteSequenceColumns = new SiteSequenceColumn[result
				.getMotifDisplayNames().size()];

		resultTable.addColumn(submitColumn, "Scan this Protein!",
				"Scan this Protein!");
		resultTable.addColumn(scoreColumn, "Score", "Score");
		resultTable.addColumn(proteinAccColumn, "Accession", "Accession");
		resultTable.addColumn(proteinInfoColumn, "Protein Annotations",
				"Protein Annotations");
		for (int i = 0; i < result.getMotifDisplayNames().size(); ++i) {
			String motifName = result.getMotifDisplayNames().get(i);
			motifName = " [" + motifName + "]";
			sitePositionColumns[i] = new SitePositionColumn(i);
			resultTable.addColumn(sitePositionColumns[i], "Site" + motifName,
					"Site" + motifName);
			resultTable.addColumnStyleName(4 + i, "sequence");
			siteSequenceColumns[i] = new SiteSequenceColumn(i);
			resultTable.addColumn(siteSequenceColumns[i], "Sequence"
					+ motifName, "Sequence" + motifName);
			resultTable.addColumnStyleName(5 + i, "sequence");
		}
		resultTable.addColumn(mwColumn, "Molecular Weight", "Molecular Weight");
		resultTable.addColumn(piColumn, "pI", "pI");

		initSortableCols();
		addData();
		mainPanel.add(resultTable);
		mainPanel.setWidth("100%");
	}

	private void initSortableCols() {
		submitColumn.setCellStyleNames("linkLike");
		scoreColumn.setSortable(true);
		if (result.getHistogramBasePath() != null) {
			scoreColumn.setCellStyleNames("linkLike");
		}
		proteinAccColumn.setSortable(true);
		proteinAccColumn.setCellStyleNames("linkLike sequence");
		for (int i = 0; i < result.getMotifDisplayNames().size(); ++i) {
			sitePositionColumns[i].setSortable(true);
			sitePositionColumns[i].setCellStyleNames("sequence");
			siteSequenceColumns[i].setCellStyleNames("sequence");
		}
		mwColumn.setSortable(true);
		piColumn.setSortable(true);

		dataProvider.addDataDisplay(resultTable);

		List<DatabaseSearchScanResultSite> list = dataProvider.getList();
		ListHandler<DatabaseSearchScanResultSite> sortHandler = new ListHandler<DatabaseSearchScanResultSite>(
				list);

		sortHandler.setComparator(scoreColumn,
				new DbSearchScanResultSiteComparator(ComparableFields.SCORE,
						true));
		sortHandler.setComparator(proteinAccColumn,
				new DbSearchScanResultSiteComparator(
						ComparableFields.PROTEIN_ACC, true));
		for (int i = 0; i < result.getMotifDisplayNames().size(); ++i) {
			sortHandler.setComparator(sitePositionColumns[i],
					new DbSearchScanResultSiteComparator(
							ComparableFields.SITE_POSITION, i, true));
		}
		sortHandler
				.setComparator(mwColumn, new DbSearchScanResultSiteComparator(
						ComparableFields.MW, true));
		sortHandler
				.setComparator(piColumn, new DbSearchScanResultSiteComparator(
						ComparableFields.PI, true));

		resultTable.addColumnSortHandler(sortHandler);
		for (int i = 0; i < result.getMotifDisplayNames().size(); ++i) {
			resultTable.getColumnSortList().push(sitePositionColumns[i]);
		}
		resultTable.getColumnSortList().push(mwColumn);
		resultTable.getColumnSortList().push(piColumn);
		resultTable.getColumnSortList().push(proteinAccColumn);
		resultTable.getColumnSortList().push(scoreColumn);
	}

	private void addData() {
		List<DatabaseSearchScanResultSite> list = dataProvider.getList();
		list.addAll(result.getDbSearchSites());
		Collections.sort(list, new DbSearchScanResultSiteComparator(
				ComparableFields.SCORE, true));
		resultTable.setRowCount(list.size());
		resultTable.setPageSize(list.size());
	}

	private <C> Column<DatabaseSearchScanResultSite, C> getColumn(Cell<C> cell,
			final GetValue<C> getter,
			FieldUpdater<DatabaseSearchScanResultSite, C> fieldUpdater) {
		Column<DatabaseSearchScanResultSite, C> column = new Column<DatabaseSearchScanResultSite, C>(
				cell) {
			@Override
			public C getValue(DatabaseSearchScanResultSite object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		return column;
	}

	public void showPopupPanel(int top, int left, SafeHtml html) {
		final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
		simplePopup.setWidget(new HTML(html));
		simplePopup.setPopupPosition(left, top);
		simplePopup.show();
	}

	private interface GetValue<C> {
		C getValue(DatabaseSearchScanResultSite site);
	}
}
