package edu.mit.scansite.client.ui.widgets.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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

import edu.mit.scansite.shared.OrthologComparator;
import edu.mit.scansite.shared.URIs;
import edu.mit.scansite.shared.OrthologComparator.ComparableFields;
import edu.mit.scansite.shared.dispatch.features.OrthologScanResult;
import edu.mit.scansite.shared.dispatch.features.OrthologScanSequencePatternResult;
import edu.mit.scansite.shared.dispatch.features.ShowMotifsForExpectedSiteGetAction;
import edu.mit.scansite.shared.dispatch.features.ShowMotifsForExpectedSiteGetResult;
import edu.mit.scansite.shared.event.EventBus;
import edu.mit.scansite.shared.event.MessageEvent;
import edu.mit.scansite.shared.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.shared.transferobjects.Ortholog;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import edu.mit.scansite.shared.transferobjects.SequencePattern;

/**
 * @author Konstantin Krismer
 */
public class OrthologScanResultTable extends Composite {

	private interface GetValue<C> {
		C getValue(Ortholog site);
	}

	private static final String EMPTY_CELL_TEXT = "";
	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private VerticalPanel mainPanel = new VerticalPanel();
	private OrthologScanResult result;
	private CellTable<Ortholog> resultTable;
	private ListDataProvider<Ortholog> dataProvider = new ListDataProvider<Ortholog>();

	private Column<Ortholog, String> proteinInfoColumn = getColumn(
			new ClickableTextCell(), new GetValue<String>() {
				@Override
				public String getValue(Ortholog match) {
					if (match == null || match.getProtein() == null
							|| match.getProtein().getAnnotations() == null
							|| match.getProtein().getAnnotations().isEmpty()) {
						return EMPTY_CELL_TEXT;
					}

					if (match.getProtein().getAnnotations() == null) {
						return EMPTY_CELL_TEXT;
					} else {
						String proteinInfo = getProteinInformation(match);
						int skipLength = (new String("Description: ").length());
						int maxLength = 50;
						int substr = (proteinInfo.length() < maxLength ? proteinInfo.length() : maxLength);
						proteinInfo = proteinInfo.substring(skipLength, substr);
						proteinInfo += " (click to expand)";
						return proteinInfo;
					}
				}
			}, new FieldUpdater<Ortholog, String>() {
				@Override
				public void update(int index, Ortholog match, String value) {
					String description = getProteinInformation(match);
					description = description.replaceAll("Description: ", "<strong>Description:</strong> </br>");
					description = description.replaceAll(";;", ";");
					description = description.replaceAll(";", "</br>");
					SafeHtmlBuilder builder = new SafeHtmlBuilder();
					builder.appendHtmlConstant(description);

					showPopupPanel(resultTable.getRowElement(index).getAbsoluteBottom(),
							resultTable.getRowElement(index).getAbsoluteLeft(), builder.toSafeHtml());

				}
			});

	private String getProteinInformation(Ortholog match) {
		if (match != null && match.getProtein() != null
				&& match.getProtein().getAnnotations() != null
				&& !match.getProtein().getAnnotations().isEmpty()) {
			StringBuilder s = new StringBuilder();
			HashMap<String, Set<String>> anns = match.getProtein()
					.getAnnotations();
			for (String key : anns.keySet()) {
				s.append(key.substring(0, 1).toUpperCase()).append(
						key.substring(1));
				if (anns.get(key).size() > 1) {
					s.append('s');
				}
				s.append(": ");
				boolean first = true;
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
		return "";
	}

	private TextColumn<Ortholog> mwColumn = new TextColumn<Ortholog>() {
		@Override
		public String getValue(Ortholog match) {
			if (match == null || match.getProtein() == null) {
				return EMPTY_CELL_TEXT;
			}
			Double mw = match.getProtein().getMolecularWeight();
			return NumberFormat.getFormat("0.000").format(mw);
		}
	};

	private TextColumn<Ortholog> piColumn = new TextColumn<Ortholog>() {
		@Override
		public String getValue(Ortholog match) {
			if (match == null || match.getProtein() == null) {
				return EMPTY_CELL_TEXT;
			}
			Double pI = match.getProtein().getpI();
			return NumberFormat.getFormat("0.000").format(pI);
		}
	};

	private Column<Ortholog, String> phosphoSitesColumn = getColumn(
			new ClickableTextCell(), new GetValue<String>() {
				@Override
				public String getValue(Ortholog match) {
					if (match == null || match.getProtein() == null) {
						return EMPTY_CELL_TEXT;
					}
					String kinaseHits = "";
					if (match.getPhosphorylationSites() != null) {
						for (ScanResultSite hit : match
								.getPhosphorylationSites()) {
							kinaseHits += hit.getMotif().getShortName() + ", ";
						}
					}
					if (kinaseHits.isEmpty()) {
						return "";
					} else {
						return kinaseHits.substring(0, kinaseHits.length() - 2);
					}
				}
			}, new FieldUpdater<Ortholog, String>() {
				@Override
				public void update(int index, Ortholog match, String value) {
					if (result instanceof OrthologScanSequencePatternResult) {
						List<SequencePattern> patterns = new ArrayList<SequencePattern>(
								1);
						patterns.add(((OrthologScanSequencePatternResult) result)
								.getSequencePattern());
						dispatch.execute(
								new ShowMotifsForExpectedSiteGetAction(match
										.getPhosphorylationSites(), patterns,
										resultTable.getRowElement(index)
												.getAbsoluteBottom(),
										resultTable.getRowElement(index)
												.getAbsoluteLeft()),
								new AsyncCallback<ShowMotifsForExpectedSiteGetResult>() {
									@Override
									public void onFailure(Throwable caught) {
										EventBus.instance()
										.fireEvent(
												new MessageEvent(
														MessageEventPriority.ERROR,
														"Fetching motifs for expected site failed",
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
				}
			});

	private Column<Ortholog, String> proteinAccColumn = getColumn(
			new ClickableTextCell(), new GetValue<String>() {
				@Override
				public String getValue(Ortholog match) {
					if (match == null) {
						return EMPTY_CELL_TEXT;
					}
					return (match.getProtein() == null) ? EMPTY_CELL_TEXT
							: match.getProtein().getIdentifier();
				}
			}, new FieldUpdater<Ortholog, String>() {
				@Override
				public void update(int index, Ortholog match, String value) {
					Window.open(URIs.getDirectIdentifierInfoLink(match
							.getProtein()), "_blank", "");
				}
			});;

	public OrthologScanResultTable(OrthologScanResult result) {
		initWidget(mainPanel);
		this.result = result;
		init();
	}

	private void addData() {
		List<Ortholog> list = dataProvider.getList();
		list.addAll(result.getOrthologs());
		resultTable.setRowCount(list.size());
		resultTable.setPageSize(list.size());
	}

	private <C> Column<Ortholog, C> getColumn(Cell<C> cell,
			final GetValue<C> getter, FieldUpdater<Ortholog, C> fieldUpdater) {
		Column<Ortholog, C> column = new Column<Ortholog, C>(cell) {
			@Override
			public C getValue(Ortholog object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		return column;
	}

	private void init() {
		resultTable = new CellTable<Ortholog>(Ortholog.KEY_PROVIDER);
		resultTable.setWidth("100%");

		resultTable.addColumn(proteinAccColumn, "Protein ID", "Protein ID");
		resultTable.addColumn(proteinInfoColumn, "Protein Annotations",
				"Protein Annotations");
		resultTable.addColumn(mwColumn, "Molecular Weight", "Molecular Weight");
		resultTable.addColumn(piColumn, "Isoelectric Point",
				"Isoelectric Point");
		resultTable.addColumn(phosphoSitesColumn, "Motifs at expected sites",
				"Motifs at expected sites");

		phosphoSitesColumn.setCellStyleNames("linkLike");
		initSortableCols();
		addData();
		mainPanel.add(resultTable);
		mainPanel.setWidth("100%");
	}

	public void showPopupPanel(int top, int left, SafeHtml html) {
		final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
		simplePopup.setWidget(new HTML(html));
		simplePopup.setPopupPosition(left, top);
		simplePopup.show();
	}

	private void initSortableCols() {
		proteinAccColumn.setSortable(true);
		proteinAccColumn.setCellStyleNames("sequence linkLike");
		mwColumn.setSortable(true);
		piColumn.setSortable(true);

		dataProvider.addDataDisplay(resultTable);

		List<Ortholog> list = dataProvider.getList();
		ListHandler<Ortholog> sortHandler = new ListHandler<Ortholog>(list);

		sortHandler.setComparator(proteinAccColumn, new OrthologComparator());
		sortHandler.setComparator(mwColumn, new OrthologComparator(
				ComparableFields.MOLECULAR_WEIGHT));
		sortHandler.setComparator(piColumn, new OrthologComparator(
				ComparableFields.ISOELECTRIC_POINT));

		resultTable.addColumnSortHandler(sortHandler);

		resultTable.getColumnSortList().push(piColumn);
		resultTable.getColumnSortList().push(mwColumn);
		resultTable.getColumnSortList().push(proteinAccColumn);
	}
}
