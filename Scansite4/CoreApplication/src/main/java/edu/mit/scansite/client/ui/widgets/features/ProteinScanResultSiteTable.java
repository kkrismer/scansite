package edu.mit.scansite.client.ui.widgets.features;

import java.util.Collections;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.event.OrthologScanMotifEvent;
import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.ScanResultSiteComparator;
import edu.mit.scansite.shared.ScanResultSiteComparator.ComparableFields;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.URIs;
import edu.mit.scansite.shared.dispatch.features.OrthologScanMotifAction;
import edu.mit.scansite.shared.dispatch.features.OrthologScanMotifResult;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationMotifProteinPairAction;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationMotifProteinPairResult;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult;
import edu.mit.scansite.shared.dispatch.features.SiteInSequenceHtmlGetAction;
import edu.mit.scansite.shared.dispatch.features.SiteInSequenceHtmlGetResult;
import edu.mit.scansite.shared.dispatch.motif.HistogramRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.HistogramRetrieverResult;
import edu.mit.scansite.shared.transferobjects.EvidenceResource;
import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.Localization;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import edu.mit.scansite.shared.transferobjects.ScanResults;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author tobieh
 * @author Konstantin Krismer
 */
public class ProteinScanResultSiteTable extends ScansiteWidget {

	private final DispatchAsync dispatch = new StandardDispatchAsync(new DefaultExceptionHandler());
	private VerticalPanel mainPanel = new VerticalPanel();
	private static final String EMPTY_CELL_TEXT = "";

	private ScanResults results;
	private CellTable<ScanResultSite> resultTable;
	private ListDataProvider<ScanResultSite> dataProvider = new ListDataProvider<ScanResultSite>();
	private User user;

	private TextColumn<ScanResultSite> groupColumn = new TextColumn<ScanResultSite>() {
		@Override
		public String getValue(ScanResultSite site) {
			if (site == null) {
				return EMPTY_CELL_TEXT;
			}
			LightWeightMotifGroup g = site.getMotif().getGroup();
			return (g == null || g.getDisplayName().isEmpty()) ? EMPTY_CELL_TEXT
					: g.getDisplayName() + " (" + g.getShortName() + ")";
		}
	};

	private TextColumn<ScanResultSite> motifColumn = new TextColumn<ScanResultSite>() {
		@Override
		public String getValue(ScanResultSite site) {
			if (site == null) {
				return EMPTY_CELL_TEXT;
			}
			Motif m = site.getMotif();
			String s = (m == null || m.getDisplayName().isEmpty()) ? EMPTY_CELL_TEXT : m.getDisplayName();
			return s + ((m.getShortName() == null || m.getShortName().isEmpty()) ? EMPTY_CELL_TEXT
					: " (" + m.getShortName() + ")");
		}
	};

	private Column<ScanResultSite, String> sequenceColumn = getColumn(
			new ClickableTextCell(new SafeHtmlRenderer<String>() {
				@Override
				public SafeHtml render(String s) {
					return SafeHtmlUtils.fromTrustedString(s);
				}

				@Override
				public void render(String s, SafeHtmlBuilder safeHtmlBuilder) {
					safeHtmlBuilder.appendHtmlConstant(s);
				}
			}), site -> {
				return site == null ? EMPTY_CELL_TEXT : site.getSiteSequence();
			}, new FieldUpdater<ScanResultSite, String>() {
				@Override
				public void update(int index, ScanResultSite site, String value) {
					dispatch.execute(
							new SiteInSequenceHtmlGetAction(site, resultTable.getRowElement(index).getAbsoluteBottom(),
									resultTable.getRowElement(index).getAbsoluteLeft()),
							new AsyncCallback<SiteInSequenceHtmlGetResult>() {
								@Override
								public void onFailure(Throwable caught) {
									EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.ERROR,
											"Displaying site in sequence failed", this.getClass().toString(), caught));
								}

								@Override
								public void onSuccess(SiteInSequenceHtmlGetResult result) {
									showPopupPanel(result.getTopPosition(), result.getLeftPosition(), result.getHtml());
								}
							});
				}
			});

	private Column<ScanResultSite, String> scoreColumn = getColumn(new ClickableTextCell(), new GetValue<String>() {
		@Override
		public String getValue(ScanResultSite site) {
			if (site == null) {
				return EMPTY_CELL_TEXT;
			}
			Double s = site.getScore();
			return ((s == null) ? EMPTY_CELL_TEXT : String.valueOf(NumberFormat.getFormat("0.000").format(s)));
		}
	}, new FieldUpdater<ScanResultSite, String>() {
		@Override
		public void update(int index, ScanResultSite site, String value) {
			if (results.getMotifSelection().getUserMotif() == null) {
				dispatch.execute(
						new HistogramRetrieverAction(site.getMotif().getId(), results.getHistogramTaxonName(),
								results.getHistogramDataSourceSelection(), site,
								resultTable.getRowElement(index).getAbsoluteBottom(),
								resultTable.getRowElement(index).getAbsoluteLeft()),
						new AsyncCallback<HistogramRetrieverResult>() {
							@Override
							public void onFailure(Throwable caught) {
								EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.ERROR,
										"Fetching reference histogram failed", this.getClass().toString(), caught));
							}

							@Override
							public void onSuccess(HistogramRetrieverResult result) {
								if (result.getHistogram() != null) {
									SafeHtmlBuilder builder = new SafeHtmlBuilder();
									builder.appendHtmlConstant("<img alt=\"Histogram\" src=\""
											+ result.getHistogram().getImageFilePath() + "\"/>");
									showPopupPanel(result.getTopPosition(), result.getLeftPosition(),
											builder.toSafeHtml());
								}
							}
						});
			}
		}
	});

	private TextColumn<ScanResultSite> percentileColumn = new TextColumn<ScanResultSite>() {
		@Override
		public String getValue(ScanResultSite site) {
			if (site == null) {
				return EMPTY_CELL_TEXT;
			}
			Double p = site.getPercentile();
			return ((p == null) ? EMPTY_CELL_TEXT
					: String.valueOf(NumberFormat.getFormat("0.000").format(p * 100)) + "%");
		}
	};

	private TextColumn<ScanResultSite> surfaceAccessColumn = new TextColumn<ScanResultSite>() {
		@Override
		public String getValue(ScanResultSite site) {
			if (site == null) {
				return EMPTY_CELL_TEXT;
			}
			Double p = site.getSurfaceAccessValue();
			return ((p == null) ? EMPTY_CELL_TEXT : String.valueOf(NumberFormat.getFormat("0.0000").format(p)));
		}
	};

	private TextColumn<ScanResultSite> siteColumn = new TextColumn<ScanResultSite>() {
		@Override
		public String getValue(ScanResultSite site) {
			if (site == null) {
				return EMPTY_CELL_TEXT;
			}
			return site.getSite();
		}
	};

	private Column<ScanResultSite, SafeHtml> evidenceLinkColumn = getColumn(new SafeHtmlCell(),
			new GetValue<SafeHtml>() {
				@Override
				public SafeHtml getValue(ScanResultSite site) {
					SafeHtmlBuilder shb = new SafeHtmlBuilder();
					if (site != null && site.getEvidence() != null && !site.getEvidence().isEmpty()) {
						for (int i = 0; i < site.getEvidence().size(); ++i) {
							if (i > 0) {
								shb.appendHtmlConstant("<span>, </span>");
							}
							EvidenceResource res = site.getEvidence().get(i);
							if (res.getUri() != null) {
								shb.appendHtmlConstant("<a href='" + res.getUri() + "' target='_blank'>"
										+ res.getResourceName() + "</a>");
							} else {
								shb.appendHtmlConstant("<span>" + res.getResourceName() + "</span>");
							}
						}
					}
					return shb.toSafeHtml();
				}
			}, new FieldUpdater<ScanResultSite, SafeHtml>() {
				@Override
				public void update(int index, ScanResultSite site, SafeHtml value) {
				}
			});

	private Column<ScanResultSite, SafeHtml> geneInfoColumn = getColumn(new SafeHtmlCell(), new GetValue<SafeHtml>() {
		@Override
		public SafeHtml getValue(ScanResultSite site) {
			SafeHtmlBuilder shb = new SafeHtmlBuilder();
			if (site == null || site.getMotif().getIdentifiers() == null
					|| site.getMotif().getIdentifiers().size() == 0) {
				shb.appendEscaped("");
			} else {
				List<Identifier> identifiers = site.getMotif().getIdentifiers();
				for (int i = 0; i < identifiers.size(); ++i) {
					if (i > 0) {
						shb.appendHtmlConstant("<br/>");
					}
					shb.appendHtmlConstant("<a href='" + URIs.getDirectIdentifierInfoLink(identifiers.get(i))
							+ "' target='_blank'>" + identifiers.get(i).getValue() + "</a>");
				}
			}
			return shb.toSafeHtml();
		}
	}, new FieldUpdater<ScanResultSite, SafeHtml>() {
		@Override
		public void update(int index, ScanResultSite site, SafeHtml value) {
		}
	});

	private Column<ScanResultSite, String> conservedSitesColumn = getColumn(new ClickableTextCell(),
			new GetValue<String>() {
				@Override
				public String getValue(ScanResultSite site) {
					return "Scan orthologs";
				}
			}, new FieldUpdater<ScanResultSite, String>() {
				@Override
				public void update(int index, ScanResultSite site, String value) {
					OrthologScanMotifAction action = new OrthologScanMotifAction(site.getMotif().getGroup(),
							site.getPosition(), site.getProtein(), results.getHistogramThreshold(), 40,
							user == null ? "" : user.getSessionId());
					final WaitPopup waitPopup = new WaitPopup();
					waitPopup.center();
					dispatch.execute(action, new AsyncCallback<OrthologScanMotifResult>() {
						@Override
						public void onFailure(Throwable caught) {
							waitPopup.hide();
							EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.ERROR,
									"Ortholog Scan failed", this.getClass().toString(), caught));
						}

						@Override
						public void onSuccess(OrthologScanMotifResult result) {
							waitPopup.hide();
							hideMessage();
							EventBus.instance().fireEvent(new OrthologScanMotifEvent(result));
						}
					});
				}
			});

	private Column<ScanResultSite, SafeHtml> colocalizationColumn = getColumn(new ClickableSafeHtmlCell(),
			new GetValue<SafeHtml>() {
				@Override
				public SafeHtml getValue(ScanResultSite site) {
					SafeHtmlBuilder builder = new SafeHtmlBuilder();
					if (site == null || site.getMotifLocalization() == null) {
						builder.appendHtmlConstant(EMPTY_CELL_TEXT);
						return builder.toSafeHtml();
					}
					if (results.getProteinLocalization() == null) {
						builder.appendHtmlConstant(site.getMotifLocalization().getType().getName());
						return builder.toSafeHtml();
					} else {
						if (results.getProteinLocalization().getType().equals(site.getMotifLocalization().getType())) {
							builder.appendHtmlConstant("<span style=\"color: darkgreen;\">"
									+ site.getMotifLocalization().getType().getName() + "</span>");
							return builder.toSafeHtml();
						} else if (site.getMotifLocalization().getType().getName().equals("unknown / NA")) {
							builder.appendHtmlConstant(
									site.getMotifLocalization().getType().getName());
							return builder.toSafeHtml();
						} else {
							builder.appendHtmlConstant("<span style=\"color: red;\">"
									+ site.getMotifLocalization().getType().getName() + "</span>");
							return builder.toSafeHtml();
						}
					}

				}
			}, new FieldUpdater<ScanResultSite, SafeHtml>() {
				@Override
				public void update(final int index, final ScanResultSite site, SafeHtml value) {
					dispatch.execute(
							new PredictLocalizationMotifProteinPairAction(site.getLocalizationDataSource(),
									site.getMotif(), site.getProtein()),
							new AsyncCallback<PredictLocalizationResult>() {
								@Override
								public void onFailure(Throwable caught) {
									EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.ERROR,
											"Motif localization failed", this.getClass().toString(), caught));
								}

								@Override
								public void onSuccess(PredictLocalizationResult result) {
									if (result.isSuccess()) {
										PredictLocalizationMotifProteinPairResult pairResult = (PredictLocalizationMotifProteinPairResult) result;
										SafeHtmlBuilder builder = new SafeHtmlBuilder();
										builder.appendHtmlConstant("<h3>Colocalization analysis - "
												+ pairResult.getLocalizationDataSource().getDisplayName() + "</h3>");
										builder.appendHtmlConstant(
												"<h4>Motif " + pairResult.getMotif().getDisplayName() + "</h4>");
										builder.append(displayLocalization(pairResult.getMotifLocalization()));
										builder.appendHtmlConstant(
												"<h4>Protein " + pairResult.getProtein().getIdentifier() + "</h4>");
										builder.append(displayLocalization(pairResult.getProteinLocalization()));

										showPopupPanel(resultTable.getRowElement(index).getAbsoluteBottom(),
												resultTable.getRowElement(index).getAbsoluteLeft(),
												builder.toSafeHtml());
									}
								}
							});
				}
			});

	private SafeHtml displayLocalization(Localization localization) {
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendHtmlConstant("Localization type: ");
		if (localization != null) {
			builder.appendEscaped(localization.getType().getName());
			builder.appendHtmlConstant("<br />");
			builder.appendEscaped("Score: " + localization.getScore());
			builder.appendHtmlConstant("<br />");
			builder.appendEscaped("GO terms: ");
			if (localization.getGoTerms() != null) {
				for (int i = 0; i < localization.getGoTerms().size(); ++i) {
					builder.appendEscaped(localization.getGoTerms().get(i).getGoTerm().getName());
					if (localization.getGoTerms().get(i).getEvidenceCode().getName() != null) {
						builder.appendHtmlConstant(" (<span title=\""
								+ localization.getGoTerms().get(i).getEvidenceCode().getName() + "\">"
								+ localization.getGoTerms().get(i).getEvidenceCode().getCode() + "</span>)");
					}
					if (i + 1 < localization.getGoTerms().size()) {
						builder.appendEscaped(", ");
					}
				}
			}
		} else {
			builder.appendEscaped("Unknown");
		}
		return builder.toSafeHtml();
	}

	public ProteinScanResultSiteTable(ScanResults results, User user) {
		this.user = user;
		initWidget(mainPanel);
		this.results = results;
		init();
	}

	public ScanResults getResults() {
		return results;
	}

	private void init() {
		resultTable = new CellTable<ScanResultSite>(ScanResultSite.KEY_PROVIDER);
		resultTable.setWidth("100%");

		initSortableCols();
		resultTable.addColumn(scoreColumn, "Score", "Score");
		if (results.getMotifSelection().getUserMotif() == null) {
			resultTable.addColumn(percentileColumn, "Percentile", "Percentile");
		}
		resultTable.addColumn(motifColumn, "Motif", "Motif");
		if (results.getMotifSelection().getUserMotif() == null) {
			resultTable.addColumn(groupColumn, "Motif Group", "Motif Group");
		}
		resultTable.addColumn(siteColumn, "Site", "Site");
		resultTable.addColumn(sequenceColumn, "Sequence", "Sequence");
		resultTable.addColumnStyleName(5, "sequence");
		resultTable.addColumn(surfaceAccessColumn, "Surface Accessibility", "Surface Accessibility");

		if (results.getMotifSelection().getUserMotif() == null) {
			resultTable.addColumn(geneInfoColumn, "Gene Info", "Gene Info");
		}
		if (results.getProtein().getDataSource() != null) {// &&
			// results.getProtein().getDatasource().getShortName().equalsIgnoreCase("swissprot"))
			// {
			resultTable.addColumn(evidenceLinkColumn, "Previously Mapped Site", "Previously Mapped Site");
		}
		if (ScansiteConstants.IDENTIFIER_MAPPING
				|| (results.getOrthologyDataSources() != null && !results.getOrthologyDataSources().isEmpty())) {
			resultTable.addColumn(conservedSitesColumn, "Evolutionary conservation", "Evolutionary Conservation");
		}

		if (results.getLocalizationDataSource() != null) {
			resultTable.addColumn(colocalizationColumn, "Colocalization", "Colocalization");
		}

		addData();
		mainPanel.add(resultTable);
		mainPanel.setWidth("100%");
	}

	private void initSortableCols() {
		scoreColumn.setSortable(true);

		if (results.getMotifSelection().getUserMotif() == null) {
			scoreColumn.setCellStyleNames("linkLike");
		}
		percentileColumn.setSortable(true);
		motifColumn.setSortable(true);
		groupColumn.setSortable(true);
		siteColumn.setSortable(true);
		siteColumn.setCellStyleNames("sequence");
		surfaceAccessColumn.setSortable(true);
		sequenceColumn.setSortable(true);
		sequenceColumn.setCellStyleNames("linkLike sequence");
		geneInfoColumn.setCellStyleNames("linkLike");
		evidenceLinkColumn.setCellStyleNames("linkLike");
		conservedSitesColumn.setCellStyleNames("linkLike");

		dataProvider.addDataDisplay(resultTable);

		List<ScanResultSite> list = dataProvider.getList();
		ListHandler<ScanResultSite> sortHandler = new ListHandler<ScanResultSite>(list);

		sortHandler.setComparator(scoreColumn, new ScanResultSiteComparator(ComparableFields.SCORE));
		sortHandler.setComparator(percentileColumn, new ScanResultSiteComparator(ComparableFields.PERCENTILE));
		sortHandler.setComparator(motifColumn, new ScanResultSiteComparator(ComparableFields.MOTIF));
		sortHandler.setComparator(groupColumn, new ScanResultSiteComparator(ComparableFields.GROUP));
		sortHandler.setComparator(siteColumn, new ScanResultSiteComparator(ComparableFields.POSITION));
		sortHandler.setComparator(surfaceAccessColumn,
				new ScanResultSiteComparator(ComparableFields.SURFACE_ACCESSIBILITY));
		sortHandler.setComparator(sequenceColumn, new ScanResultSiteComparator(ComparableFields.SITE_AA));

		resultTable.addColumnSortHandler(sortHandler);
		resultTable.getColumnSortList().push(siteColumn);
		resultTable.getColumnSortList().push(surfaceAccessColumn);
		resultTable.getColumnSortList().push(sequenceColumn);
		resultTable.getColumnSortList().push(percentileColumn);
		resultTable.getColumnSortList().push(motifColumn);
		resultTable.getColumnSortList().push(groupColumn);
		resultTable.getColumnSortList().push(scoreColumn);
	}

	private void addData() {
		List<ScanResultSite> list = dataProvider.getList();
		list.addAll(results.getHits());
		Collections.sort(list, new ScanResultSiteComparator(ComparableFields.SCORE, true));
		resultTable.setRowCount(list.size());
		resultTable.setPageSize(list.size());
	}

	public void showPopupPanel(int top, int left, SafeHtml html) {
		final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
		simplePopup.setWidget(new HTML(html));
		simplePopup.setPopupPosition(left, top);
		simplePopup.show();
	}

	private <C> Column<ScanResultSite, C> getColumn(Cell<C> cell, final GetValue<C> getter,
			FieldUpdater<ScanResultSite, C> fieldUpdater) {
		Column<ScanResultSite, C> column = new Column<ScanResultSite, C>(cell) {
			@Override
			public C getValue(ScanResultSite object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		return column;
	}

	private interface GetValue<C> {
		C getValue(ScanResultSite site);
	}
}
