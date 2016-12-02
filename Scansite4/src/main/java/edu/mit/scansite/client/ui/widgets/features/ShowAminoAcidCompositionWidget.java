package edu.mit.scansite.client.ui.widgets.features;

import java.util.ArrayList;
import java.util.HashMap;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.ImagePaths;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.dispatch.features.DomainPlotGetAction;
import edu.mit.scansite.shared.dispatch.features.DomainPlotGetResult;
import edu.mit.scansite.shared.dispatch.features.DomainPositionsGetAction;
import edu.mit.scansite.shared.dispatch.features.DomainPositionsGetResult;
import edu.mit.scansite.shared.transferobjects.AminoAcid;
import edu.mit.scansite.shared.transferobjects.DomainPosition;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ShowAminoAcidCompositionWidget extends ScansiteWidget {
	interface ShowAminoAcidCompositionWidgetUiBinder extends
			UiBinder<Widget, ShowAminoAcidCompositionWidget> {
	}

	// private static final String WAITING_FOR_DOMAINS =
	// "Waiting for protein domains... ";
	private static ShowAminoAcidCompositionWidgetUiBinder uiBinder = GWT
			.create(ShowAminoAcidCompositionWidgetUiBinder.class);

	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private LightWeightProtein protein;
	private AminoAcid centerAminoAcid = AminoAcid.S;
	private ArrayList<AminoAcid> aminoAcids = new ArrayList<AminoAcid>();

	@UiField
	RadioButton centerS;

	@UiField
	RadioButton centerT;

	@UiField
	RadioButton centerY;

	@UiField
	RadioButton centerOther;

	@UiField
	ListBox centerOtherListBox;

	@UiField(provided = true)
	Grid grid;

	@UiField
	FlowPanel showSequencePanel;

	@UiField
	FlowPanel proteinPlotPanel;

	private int nGridRows = ScansiteConstants.WINDOW_SIZE + 2;
	private int nGridCols = 1;

	private HorizontalPanel waitPanel = new HorizontalPanel();

	private ArrayList<AminoAcid> otherAminoAcids = new ArrayList<AminoAcid>();

	private ArrayList<DomainPosition> domainPositions = null;

	private int[] totalCounts;
	private ArrayList<HashMap<AminoAcid, Integer>> aaCountMaps = new ArrayList<HashMap<AminoAcid, Integer>>();

	private class ShowCompositionClickHandler implements ClickHandler {

		private AminoAcid centerAa = AminoAcid.S;
		private AminoAcid aa = AminoAcid.S;
		private int position = 0;
		private LightWeightProtein protein = new LightWeightProtein();

		public ShowCompositionClickHandler(AminoAcid aa, AminoAcid centerAa,
				int position, LightWeightProtein protein) {
			this.position = position;
			this.centerAa = centerAa;
			this.aa = aa;
			this.protein = protein;
		}

		@Override
		public void onClick(ClickEvent event) {
			if (domainPositions != null) {
				proteinPlotPanel.clear();
				proteinPlotPanel.add(ImagePaths
						.getStaticImage(ImagePaths.WAIT_HUGE));
				dispatch.execute(new DomainPlotGetAction(protein,
						domainPositions, centerAa, aa, position),
						new AsyncCallback<DomainPlotGetResult>() {
							@Override
							public void onFailure(Throwable caught) {
								EventBus.instance()
								.fireEvent(
										new MessageEvent(
												MessageEventPriority.ERROR,
												"Retrieving domains failed",
												this.getClass()
														.toString(),
												caught));
								proteinPlotPanel.clear();
								Label l = new Label(
										"Error retrieving domains from server.");
								l.setStyleName("messageLabel");
								waitPanel.add(l);
								setShowSequencePanel(new SiteInSequenceWidget(
										protein, aa, centerAa, position, true,
										domainPositions));
							}

							@Override
							public void onSuccess(DomainPlotGetResult result) {
								proteinPlotPanel.clear();
								waitPanel.clear();
								if (result.isSuccess()) {
									domainPositions = result
											.getDomainPositions();
									proteinPlotPanel.add(new Image(result
											.getDomainPlotUrl()));
									proteinPlotPanel
											.add(new DomainInformationWidget(
													protein, domainPositions));
								} else {
									Label l = new Label(result
											.getErrorMessage());
									l.setStyleName("messageLabel");
									waitPanel.add(l);
								}
								setShowSequencePanel(new SiteInSequenceWidget(
										protein, aa, centerAa, position, true,
										domainPositions));
							}
						});
			} else {
				setShowSequencePanel(new SiteInSequenceWidget(protein, aa,
						centerAa, position, true, domainPositions));
			}
		}
	}

	public ShowAminoAcidCompositionWidget(LightWeightProtein protein) {
		initAminoAcids();
		nGridCols = aminoAcids.size() + 2;
		grid = new Grid(nGridRows, nGridCols);
		this.protein = protein;
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				centerS.setText(AminoAcid.S.getFullName());
				centerT.setText(AminoAcid.T.getFullName());
				centerY.setText(AminoAcid.Y.getFullName());
				centerOther.setText("Other: ");
				for (AminoAcid aa : AminoAcid.values()) {
					if (aa.isSingleAa() && !AminoAcid.isTerminal(aa)
							&& aa != AminoAcid.S && aa != AminoAcid.T
							&& aa != AminoAcid.Y) {
						otherAminoAcids.add(aa);
						centerOtherListBox.addItem(aa.getFullName());
					}
				}
				getDomains();
				selectCenterAminoAcid(AminoAcid.S);
			}
		});
	}

	// private void initWaitPanel(String text) {
	// waitPanel.clear();
	// waitPanel.add(ImagePaths.getStaticImage(ImagePaths.WAIT_SMALL));
	// waitPanel.add(new HTML("<b>" + text + "</b>"));
	// waitPanel.add(ImagePaths.getStaticImage(ImagePaths.WAIT_SMALL));
	// waitPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	// waitPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	// }

	private void getDomains() {
		dispatch.execute(new DomainPositionsGetAction(protein.getSequence()),
				new AsyncCallback<DomainPositionsGetResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance()
						.fireEvent(
								new MessageEvent(
										MessageEventPriority.ERROR,
										"Retrieving domains failed",
										this.getClass()
												.toString(),
										caught));
						Label l = new Label(
								"Error retrieving domains from server.");
						l.setStyleName("messageLabel");
						waitPanel.add(l);
					}

					@Override
					public void onSuccess(DomainPositionsGetResult result) {
						waitPanel.clear();
						if (result.isSuccess()) {
							domainPositions = result.getDomainPositions();
							Label l;
							if (domainPositions != null
									&& !domainPositions.isEmpty()) {
								l = new Label(
										"Domains are ready. Make a selection to display them!");
							} else {
								l = new Label("No domains found!");
							}
							l.setStyleName("messageLabelBlack");
							waitPanel.add(l);
						} else {
							Label l = new Label(result.getErrorMessage());
							l.setStyleName("messageLabel");
							waitPanel.add(l);
						}
					}
				});
	}

	public void setShowSequencePanel(IsWidget widget) {
		showSequencePanel.clear();
		showSequencePanel.add(widget);
	}

	@UiHandler("centerS")
	public void onCenterSRadioButtonClick(ClickEvent event) {
		selectCenterAminoAcid(AminoAcid.S);
	}

	@UiHandler("centerT")
	public void onCenterTRadioButtonClick(ClickEvent event) {
		selectCenterAminoAcid(AminoAcid.T);
	}

	@UiHandler("centerY")
	public void onCenterYRadioButtonClick(ClickEvent event) {
		selectCenterAminoAcid(AminoAcid.Y);
	}

	@UiHandler("centerOther")
	public void onCenterOtherRadioButtonClick(ClickEvent event) {
		selectCenterAminoAcid(otherAminoAcids.get(centerOtherListBox
				.getSelectedIndex()));
	}

	@UiHandler("centerOtherListBox")
	public void onCenterOtherListBoxChange(ChangeEvent event) {
		centerOther.setValue(true);
		selectCenterAminoAcid(otherAminoAcids.get(centerOtherListBox
				.getSelectedIndex()));
	}

	private void selectCenterAminoAcid(AminoAcid aminoAcid) {
		centerAminoAcid = aminoAcid;
		DOM.getElementById("aa").setInnerText(aminoAcid.getFullName());
		initGrid();
		setShowSequencePanel(new SiteInSequenceWidget(protein, centerAminoAcid,
				centerAminoAcid, 0, true, domainPositions));
	}

	// private void init() {
	// // initWaitPanel(WAITING_FOR_DOMAINS);
	// }

	private void initAminoAcids() {
		AminoAcid[] all = AminoAcid.values();
		aminoAcids.clear();
		for (AminoAcid aa : all) {
			if (aa.isSingleAa() && !AminoAcid.isTerminal(aa)) {
				aminoAcids.add(aa);
			}
		}
	}

	private void initGrid() {
		initCountMap();
		grid.addStyleName("aminoAcidCompositionTable");

		for (int row = 0; row < nGridRows; ++row) { // rows
			for (int col = 0; col < nGridCols; ++col) { // columns
				if ((row > 0 && row < nGridRows - 1)
						&& (col == 0 || col == nGridCols - 1)) {
					Integer val = row - 1 - ScansiteConstants.HALF_WINDOW;
					Label posLabel;
					if (val != 0) {
						posLabel = new Label(
								(((val > 0) ? "+" : "") + String.valueOf(val)));
					} else {
						posLabel = new Label(
								centerAminoAcid.getThreeLetterCode());
					}
					posLabel.setStyleName("label");
					grid.setWidget(row, col, posLabel);
				} else if ((col > 0 && col < nGridCols - 1)
						&& (row == 0 || row == nGridRows - 1)) {
					AminoAcid aa = aminoAcids.get(col - 1);
					Label aaLabel = new Label(String.valueOf(aa
							.getOneLetterCode()));
					aaLabel.setTitle(aa.getFullName() + " ("
							+ aa.getThreeLetterCode() + ")");
					aaLabel.setStyleName("label");
					grid.setWidget(row, col, aaLabel);
				} else if (row > 0 && col > 0 && row < nGridRows - 1
						&& col < nGridCols - 1) {
					Double value = 0D;
					if (aaCountMaps.get(row - 1).containsKey(
							aminoAcids.get(col - 1))) {
						value = aaCountMaps.get(row - 1).get(
								aminoAcids.get(col - 1))
								/ (double) totalCounts[row - 1];
					}
					Button valueButton = new Button(NumberFormat.getFormat(
							"0.00").format(roundDouble(value)));
					valueButton
							.addClickHandler(new ShowCompositionClickHandler(
									aminoAcids.get(col - 1), centerAminoAcid,
									row - 1 - ScansiteConstants.HALF_WINDOW,
									protein));
					if (row - 1 == ScansiteConstants.WINDOW_CENTER_INDEX) {
						valueButton.setStyleName("centerValue");
					} else if (row % 2 == 0) {
						valueButton.setStyleName("evenRowValue");
					} else {
						valueButton.setStyleName("oddRowValue");
					}
					grid.setWidget(row, col, valueButton);
				}
			}
		}
	}

	private void initCountMap() {
		aaCountMaps.clear();
		totalCounts = new int[ScansiteConstants.WINDOW_SIZE];
		for (int i = 0; i < ScansiteConstants.WINDOW_SIZE; ++i) {
			aaCountMaps.add(new HashMap<AminoAcid, Integer>());
		}
		char[] seq = protein.getSequence().toCharArray();
		for (int i = 0; i < seq.length; ++i) { // positions in sequence
			if (seq[i] == centerAminoAcid.getOneLetterCode()) {
				for (int pos = 0; pos < ScansiteConstants.WINDOW_SIZE; ++pos) {
					int relativeIndex = pos + i - ScansiteConstants.HALF_WINDOW;
					if (relativeIndex >= 0 && pos + relativeIndex < seq.length) {
						AminoAcid relativeAa = AminoAcid
								.getValue(seq[relativeIndex]);
						Integer currentVal = aaCountMaps.get(pos).get(
								relativeAa);
						aaCountMaps.get(pos).put(relativeAa,
								currentVal == null ? 1 : currentVal + 1);
						++totalCounts[pos];
					}
				}
			}
		}
	}

	private double roundDouble(double value) {
		return Math.round(1000 * value) / 1000.0;
	}
}
