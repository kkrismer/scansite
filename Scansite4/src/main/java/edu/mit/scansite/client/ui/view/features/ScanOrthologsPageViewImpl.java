package edu.mit.scansite.client.ui.view.features;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.widgets.features.ChooseOrthologyProteinWidget;
import edu.mit.scansite.client.ui.widgets.features.SeqPatMethodWidget;
import edu.mit.scansite.client.ui.widgets.features.StringencyLevelWidget;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.states.ScanOrthologsPageState;

/**
 * @author Konstantin Krismer
 */
public class ScanOrthologsPageViewImpl extends ScanOrthologsPageView {
	interface ScanOrthologsPageViewImplUiBinder extends
			UiBinder<Widget, ScanOrthologsPageViewImpl> {
	}

	private static ScanOrthologsPageViewImplUiBinder uiBinder = GWT
			.create(ScanOrthologsPageViewImplUiBinder.class);

	private Presenter presenter;
	private List<LightWeightMotifGroup> motifGroups;

	@UiField
	ChooseOrthologyProteinWidget chooseOrthologyProteinWidget;

	@UiField
	RadioButton searchMethodSequencePatternRadioButton;

	@UiField
	RadioButton searchMethodMotifGroupsRadioButton;

	@UiField
	SeqPatMethodWidget seqPatMethodWidget;

	@UiField
	ListBox motifGroupListBox;

	@UiField
	TextBox sitePositionTextBox;

	@UiField
	StringencyLevelWidget stringencyLevelWidget;

	@UiField
	RadioButton alignmentRadius10RadioButton;

	@UiField
	RadioButton alignmentRadius20RadioButton;

	@UiField
	RadioButton alignmentRadius40RadioButton;

	@UiField
	RadioButton alignmentRadius80RadioButton;

	@UiField
	SubmitButton submitButton;

	public ScanOrthologsPageViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("searchMethodSequencePatternRadioButton")
	public void onSearchMethodSequencePatternRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("scanOrthologsSequencePattern").setAttribute(
				"style", "display: block;");
		DOM.getElementById("scanOrthologsMotifGroups").setAttribute("style",
				"display: none;");
	}

	@UiHandler("searchMethodMotifGroupsRadioButton")
	public void onSearchMethodMotifGroupsRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("scanOrthologsSequencePattern").setAttribute(
				"style", "display: none;");
		DOM.getElementById("scanOrthologsMotifGroups").setAttribute("style",
				"display: block;");
	}

	@UiHandler("submitButton")
	public void onSubmitButtonClick(ClickEvent event) {
		setSubmitButtonEnabled(false);
		hideMessage();
		if (searchMethodSequencePatternRadioButton.getValue()) {
			presenter.onSequencePatternSubmitButtonClicked(seqPatMethodWidget
					.getSequencePatterns().get(0), chooseOrthologyProteinWidget
					.getOrthologyDataSource(), chooseOrthologyProteinWidget
					.getProtein(), stringencyLevelWidget.getStringency(),
					getAlignmentRadius());
		} else {
			presenter
					.onMotifGroupSubmitButtonClicked(motifGroups
							.get(motifGroupListBox.getSelectedIndex()), Integer
							.parseInt(sitePositionTextBox.getValue()),
							chooseOrthologyProteinWidget
									.getOrthologyDataSource(),
							chooseOrthologyProteinWidget.getProtein(),
							stringencyLevelWidget.getStringency(),
							getAlignmentRadius());
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
	public void hideWaitImage() {
		Element waitSpanElement = DOM.getElementById("waitScan");
		waitSpanElement.setAttribute("style", "display: none;");
	}

	@Override
	public String getPageTitle() {
		return "Scan for evolutionary conserved phosphorylation sites";
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
		return NavigationEvent.PageId.FEATURE_SCAN_ORTHOLOGS.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FEATURE_SCAN_ORTHOLOGS;
	}

	private int getAlignmentRadius() {
		if (alignmentRadius10RadioButton.getValue()) {
			return 10;
		}
		if (alignmentRadius20RadioButton.getValue()) {
			return 20;
		}
		if (alignmentRadius80RadioButton.getValue()) {
			return 80;
		}
		// default:
		return 40;
	}

	private void setAlignmentRadius(final int radius) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				alignmentRadius10RadioButton.setValue(radius < 11);
				alignmentRadius20RadioButton.setValue(radius > 10
						&& radius < 21);
				alignmentRadius40RadioButton.setValue(radius > 20
						&& radius < 41);
				alignmentRadius80RadioButton.setValue(radius > 40);
			}
		});
	}

	@Override
	public void initMotifGroups(List<LightWeightMotifGroup> motifGroups) {
		this.motifGroups = motifGroups;
		for (LightWeightMotifGroup motifGroup : motifGroups) {
			motifGroupListBox.addItem(motifGroup.getDisplayName(),
					motifGroup.getShortName());
		}
	}

	@Override
	public ScanOrthologsPageState getState() {
		int sitePosition;
		try {
			sitePosition = Integer.parseInt(sitePositionTextBox.getValue());
		} catch (NumberFormatException ex) {
			sitePosition = 0;
		}
		return new ScanOrthologsPageState(
				chooseOrthologyProteinWidget.getState(),
				searchMethodSequencePatternRadioButton.getValue(),
				searchMethodMotifGroupsRadioButton.getValue(),
				seqPatMethodWidget.getState(),
				motifGroupListBox.getSelectedIndex(), sitePosition,
				stringencyLevelWidget.getState(), getAlignmentRadius());
	}

	@Override
	public void setState(final ScanOrthologsPageState state) {
		if (state != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					if (state.isSearchMethodMotifGroupsRadioButtonValue()) {
						searchMethodMotifGroupsRadioButton.setValue(true, true);
					} else {
						searchMethodSequencePatternRadioButton.setValue(true,
								true);
					}
					motifGroupListBox.setSelectedIndex(state
							.getMotifGroupListBoxSelectedIndex());
					sitePositionTextBox.setText(Integer.toString(state
							.getSitePosition()));
				}
			});
			chooseOrthologyProteinWidget.setState(state
					.getChooseOrthologyProteinWidgetState());
			seqPatMethodWidget.setState(state.getSeqPatMethodWidgetState());
			stringencyLevelWidget.setState(state
					.getStringencyLevelWidgetState());
			setAlignmentRadius(state.getAlignmentRadius());
		}
	}
}
