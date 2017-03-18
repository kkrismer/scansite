package edu.mit.scansite.client.ui.widgets.features;

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
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.event.OrthologScanSequencePatternEvent;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.SequenceMatchComparator;
import edu.mit.scansite.shared.SequenceMatchComparator.ComparableFields;
import edu.mit.scansite.shared.URIs;
import edu.mit.scansite.shared.dispatch.features.OrthologScanSequencePatternAction;
import edu.mit.scansite.shared.dispatch.features.OrthologScanSequencePatternResult;
import edu.mit.scansite.shared.dispatch.features.SequenceMatchResult;
import edu.mit.scansite.shared.dispatch.features.ShowMotifsForExpectedSiteGetAction;
import edu.mit.scansite.shared.dispatch.features.ShowMotifsForExpectedSiteGetResult;
import edu.mit.scansite.shared.dispatch.features.ShowSequenceMatchesHtmlGetAction;
import edu.mit.scansite.shared.dispatch.features.ShowSequenceMatchesHtmlGetResult;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.ProteinSequenceMatch;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SequenceMatchResultTable extends Composite {

	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());
	private VerticalPanel mainPanel = new VerticalPanel();

	private static final String EMPTY_CELL_TEXT = "";

	private SequenceMatchResult results;
	private CellTable<ProteinSequenceMatch> resultTable;
	private ListDataProvider<ProteinSequenceMatch> dataProvider = new ListDataProvider<ProteinSequenceMatch>();
	private boolean isCompatibleIdentifierType;
	private User user;

	private TextColumn<ProteinSequenceMatch> proteinInfoColumn = new TextColumn<ProteinSequenceMatch>() {
		@Override
		public String getValue(ProteinSequenceMatch match) {
			if (match == null || match.getProtein() == null) {
				return EMPTY_CELL_TEXT;
			}
			HashMap<String, Set<String>> annos = match.getProtein()
					.getAnnotations();
			String info = "";
			for (String title : annos.keySet()) {
				for (String val : annos.get(title)) {
					info += val;
					if (!val.endsWith(";")) {
						info += ";";
					}
					info += " ";
				}
			}
			return info;
		}
	};

	private TextColumn<ProteinSequenceMatch> mwColumn = new TextColumn<ProteinSequenceMatch>() {
		@Override
		public String getValue(ProteinSequenceMatch match) {
			if (match == null || match.getProtein() == null) {
				return EMPTY_CELL_TEXT;
			}
			Double mw = match.getProtein().getMolecularWeight();
			return NumberFormat.getFormat("0.000").format(mw);
		}
	};

	private TextColumn<ProteinSequenceMatch> piColumn = new TextColumn<ProteinSequenceMatch>() {
		@Override
		public String getValue(ProteinSequenceMatch match) {
			if (match == null || match.getProtein() == null) {
				return EMPTY_CELL_TEXT;
			}
			Double pI = match.getProtein().getpI();
			return NumberFormat.getFormat("0.000").format(pI);
		}
	};

	private Column<ProteinSequenceMatch, String> phosphoSitesColumn = getColumn(
			new ClickableTextCell(), new GetValue<String>() {
				@Override
				public String getValue(ProteinSequenceMatch match) {
					if (match == null || match.getProtein() == null) {
						return EMPTY_CELL_TEXT;
					}
					String kinaseHits = "";
					for (ScanResultSite hit : match.getPhosphorylationSites()) {
						kinaseHits += hit.getMotif().getShortName() + ", ";
					}
					if (kinaseHits.isEmpty()) {
						return "";
					} else {
						return kinaseHits.substring(0, kinaseHits.length() - 2);
					}
				}
			}, new FieldUpdater<ProteinSequenceMatch, String>() {
				@Override
				public void update(int index, ProteinSequenceMatch match,
						String value) {
					dispatch.execute(
							new ShowMotifsForExpectedSiteGetAction(match
									.getPhosphorylationSites(), results
									.getSequencePatterns(), resultTable
									.getRowElement(index).getAbsoluteBottom(),
									resultTable.getRowElement(index)
											.getAbsoluteLeft()),
							new AsyncCallback<ShowMotifsForExpectedSiteGetResult>() {
								@Override
								public void onFailure(Throwable caught) {
									EventBus.instance()
											.fireEvent(
													new MessageEvent(
															MessageEventPriority.ERROR,
															"Displaying motifs for expected site failed",
															this.getClass()
																	.toString(),
															caught));
								}

								@Override
								public void onSuccess(
										ShowMotifsForExpectedSiteGetResult result) {
									showPopupPanel(result.getTopPosition(),
											result.getLeftPosition(),
											result.getHtml());
								}
							});
				}
			});

	private Column<ProteinSequenceMatch, String> conservedSitesColumn = getColumn(
			new ClickableTextCell(), new GetValue<String>() {
				@Override
				public String getValue(ProteinSequenceMatch match) {
					if (match == null || match.getProtein() == null) {
						return EMPTY_CELL_TEXT;
					}
					if (match.getPhosphorylationSites().size() > 0) {
						return "Scan orthologs";
					} else {
						return "";
					}
				}
			}, new FieldUpdater<ProteinSequenceMatch, String>() {
				@Override
				public void update(int index, ProteinSequenceMatch match,
						String value) {
					if (!value.isEmpty()) {
						// TODO select correct sequence pattern, not first one
						OrthologScanSequencePatternAction action = new OrthologScanSequencePatternAction(
								results.getSequencePatterns().get(0), match
										.getProtein(),
								HistogramStringency.STRINGENCY_MEDIUM, 40,
								user != null ? user.getSessionId() : "");

						final WaitPopup waitPopup = new WaitPopup();
						waitPopup.center();
						dispatch.execute(
								action,
								new AsyncCallback<OrthologScanSequencePatternResult>() {
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
									public void onSuccess(
											OrthologScanSequencePatternResult result) {
										waitPopup.hide();
										EventBus.instance()
												.fireEvent(
														new OrthologScanSequencePatternEvent(
																result));
									}
								});
					}
				}
			});

	private Column<ProteinSequenceMatch, String> proteinAccColumn = getColumn(
			new ClickableTextCell(), new GetValue<String>() {
				@Override
				public String getValue(ProteinSequenceMatch match) {
					if (match == null) {
						return EMPTY_CELL_TEXT;
					}
					return (match.getProtein() == null) ? EMPTY_CELL_TEXT
							: match.getProtein().getIdentifier();
				}
			}, new FieldUpdater<ProteinSequenceMatch, String>() {
				@Override
				public void update(int index, ProteinSequenceMatch match,
						String value) {
					Window.open(URIs.getDirectIdentifierInfoLink(match
							.getProtein()), "_blank", "");
				}
			});

	private RegexColumn[] regexColumns;

	private class RegexColumn extends Column<ProteinSequenceMatch, String> {
		private int index = 0;

		private int getIndex() {
			return index;
		}

		public RegexColumn(int index) {
			super(new ClickableTextCell());
			this.index = index >= 0 ? index : 0;
			super.setFieldUpdater(new FieldUpdater<ProteinSequenceMatch, String>() {
				@Override
				public void update(int index, ProteinSequenceMatch match,
						String value) {
					match.getProtein().setDataSource(results.getDataSource());
					dispatch.execute(
							new ShowSequenceMatchesHtmlGetAction(results
									.getSequencePatterns().get(getIndex())
									.getRegEx(), match.getProtein(),
									resultTable.getRowElement(index)
											.getAbsoluteBottom(), resultTable
											.getRowElement(index)
											.getAbsoluteLeft()),
							new AsyncCallback<ShowSequenceMatchesHtmlGetResult>() {
								@Override
								public void onFailure(Throwable caught) {
									EventBus.instance()
											.fireEvent(
													new MessageEvent(
															MessageEventPriority.ERROR,
															"Show sequence matches feature failed",
															this.getClass()
																	.toString(),
															caught));
								}

								@Override
								public void onSuccess(
										ShowSequenceMatchesHtmlGetResult result) {
									showPopupPanel(result.getTopPosition(),
											result.getLeftPosition(),
											result.getHtml());
								}
							});
				}
			});
		}

		@Override
		public String getValue(ProteinSequenceMatch match) {
			if (match == null) {
				return EMPTY_CELL_TEXT;
			}
			Integer val = match.getNrOfSequenceMatches()[index];
			return "Show " + val + " match" + (val == 1 ? "" : "es");
		}
	};

	public SequenceMatchResultTable(SequenceMatchResult results, User user) {
		initWidget(mainPanel);
		this.results = results;
		this.user = user;
		init();
	}

	private void init() {
		resultTable = new CellTable<ProteinSequenceMatch>(
				ProteinSequenceMatch.KEY_PROVIDER);
		resultTable.setWidth("100%");

		resultTable.addColumn(proteinAccColumn, "Protein ID", "Protein ID");

		regexColumns = new RegexColumn[results.getSequencePatterns().size()];
		RegexColumn current;
		for (int i = 0; i < regexColumns.length; ++i) {
			current = new RegexColumn(i);
			resultTable.addColumn(current, "Pattern: "
					+ results.getSequencePatterns().get(i).getRegEx(),
					"Pattern: "
							+ results.getSequencePatterns().get(i).getRegEx());
			regexColumns[i] = current;
		}
		resultTable.addColumn(proteinInfoColumn, "Protein Annotations",
				"Protein Annotations");
		resultTable.addColumn(mwColumn, "Molecular Weight", "Molecular Weight");
		resultTable.addColumn(piColumn, "Isoelectric Point",
				"Isoelectric Point");
		resultTable.addColumn(phosphoSitesColumn, "Motifs at expected sites",
				"Motifs at expected sites");

		if (ScansiteConstants.IDENTIFIER_MAPPING
				|| (results.getCompatibleOrthologyDataSources() != null && !results
						.getCompatibleOrthologyDataSources().isEmpty())) {
			resultTable.addColumn(conservedSitesColumn,
					"Evolutionary conservation", "Evolutionary conservation");
		}
		phosphoSitesColumn.setCellStyleNames("linkLike");
		conservedSitesColumn.setCellStyleNames("linkLike");
		initSortableCols();
		addData();
		mainPanel.add(resultTable);
		mainPanel.setWidth("100%");
	}

	private void initSortableCols() {
		proteinAccColumn.setSortable(true);
		proteinAccColumn.setCellStyleNames("sequence linkLike");
		mwColumn.setSortable(true);
		piColumn.setSortable(true);
		for (int i = 0; i < regexColumns.length; ++i) {
			regexColumns[i].setCellStyleNames("linkLike");
		}
		dataProvider.addDataDisplay(resultTable);

		List<ProteinSequenceMatch> list = dataProvider.getList();
		ListHandler<ProteinSequenceMatch> sortHandler = new ListHandler<ProteinSequenceMatch>(
				list);

		sortHandler.setComparator(proteinAccColumn,
				new SequenceMatchComparator());
		sortHandler.setComparator(mwColumn, new SequenceMatchComparator(
				ComparableFields.MOLECULAR_WEIGHT));
		sortHandler.setComparator(piColumn, new SequenceMatchComparator(
				ComparableFields.ISOELECTRIC_POINT));
		for (int i = 0; i < regexColumns.length; ++i) {
			regexColumns[i].setSortable(true);
			sortHandler.setComparator(regexColumns[i],
					new SequenceMatchComparator(ComparableFields.SITE_MATCHES,
							i));
		}

		resultTable.addColumnSortHandler(sortHandler);
		for (int i = 0; i < regexColumns.length; ++i) {
			resultTable.getColumnSortList().push(regexColumns[i]);
		}
		resultTable.getColumnSortList().push(piColumn);
		resultTable.getColumnSortList().push(mwColumn);
		resultTable.getColumnSortList().push(proteinAccColumn);
	}

	private void addData() {
		List<ProteinSequenceMatch> list = dataProvider.getList();
		list.addAll(results.getMatches());
		resultTable.setRowCount(list.size());
		resultTable.setPageSize(list.size());
	}

	private <C> Column<ProteinSequenceMatch, C> getColumn(Cell<C> cell,
			final GetValue<C> getter,
			FieldUpdater<ProteinSequenceMatch, C> fieldUpdater) {
		Column<ProteinSequenceMatch, C> column = new Column<ProteinSequenceMatch, C>(
				cell) {
			@Override
			public C getValue(ProteinSequenceMatch object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		return column;
	}

	public boolean isCompatibleIdentifierType() {
		return isCompatibleIdentifierType;
	}

	public void setCompatibleIdentifierType(boolean isCompatibleIdentifierType) {
		this.isCompatibleIdentifierType = isCompatibleIdentifierType;
	}

	public void showPopupPanel(int top, int left, SafeHtml html) {
		final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
		simplePopup.setWidget(new HTML(html));
		simplePopup.setPopupPosition(left, top);
		simplePopup.show();
	}

	private interface GetValue<C> {
		C getValue(ProteinSequenceMatch site);
	}
}
