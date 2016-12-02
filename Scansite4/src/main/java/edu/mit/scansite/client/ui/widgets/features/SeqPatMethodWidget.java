package edu.mit.scansite.client.ui.widgets.features;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.SequencePattern;
import edu.mit.scansite.shared.transferobjects.states.SeqPatMethodWidgetState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SeqPatMethodWidget extends ScansiteWidget implements
		SequenceMatchMethodWidget, Stateful<SeqPatMethodWidgetState> {
	interface SeqPatMethodWidgetUiBinder extends
			UiBinder<Widget, SeqPatMethodWidget> {
	}

	private static final int MAX_MOTIFS = ScansiteConstants.SEQUENCE_MATCH_MAX_MOTIFS;

	private static SeqPatMethodWidgetUiBinder uiBinder = GWT
			.create(SeqPatMethodWidgetUiBinder.class);

	private ArrayList<SequencePatternWidget> motifWidgets = new ArrayList<SequencePatternWidget>();

	@UiField
	FlowPanel sequencePatternsFlowPanel;

	@UiField
	Button addSequencePatternButton;

	@UiField
	Button removeSequencePatternButton;

	@UiField(provided = true)
	CheckBox limitResultCheckBox = new CheckBox();

	public SeqPatMethodWidget() {
		limitResultCheckBox.getElement().setId("limitResultCheckBoxId");
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				addSequencePatternWidget();
			}
		});
	}

	@UiHandler("addSequencePatternButton")
	public void onAddSequencePatternButtonClick(ClickEvent event) {
		addSequencePatternWidget();
	}

	@UiHandler("removeSequencePatternButton")
	public void onRemoveSequencePatternButtonClick(ClickEvent event) {
		if (motifWidgets.size() > 1) {
			motifWidgets.remove(motifWidgets.size() - 1);
			sequencePatternsFlowPanel.remove(sequencePatternsFlowPanel
					.getWidgetCount() - 1);
		}
		setButtonVisibility();
	}

	private void addSequencePatternWidget() {
		if (motifWidgets.size() <= MAX_MOTIFS) {
			int i = motifWidgets.size() + 1;
			SequencePatternWidget rmw = new SequencePatternWidget(i, true,
					true, false, true);
			motifWidgets.add(rmw);
			sequencePatternsFlowPanel.add(rmw);
		}
		setButtonVisibility();
	}

	private void setButtonVisibility() {
		addSequencePatternButton.setVisible(motifWidgets.size() != MAX_MOTIFS);
		removeSequencePatternButton.setVisible(motifWidgets.size() > 1);
	}

	@Override
	public List<SequencePattern> getSequencePatterns() {
		List<SequencePattern> patterns = new ArrayList<SequencePattern>(
				motifWidgets.size());
		for (SequencePatternWidget mw : motifWidgets) {
			patterns.add(mw.getSequencePattern());
		}
		return patterns;
	}

	@Override
	public boolean showPhosphorylatedProteinsOnly() {
		return limitResultCheckBox.getValue();
	}

	@Override
	public SeqPatMethodWidgetState getState() {
		return new SeqPatMethodWidgetState(getSequencePatterns(),
				showPhosphorylatedProteinsOnly());
	}

	@Override
	public void setState(final SeqPatMethodWidgetState state) {
		if (state != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					for (int i = 0; i < motifWidgets.size(); ++i) {
						if (state.getSequencePatterns().size() < i) {
							motifWidgets.get(i).setSequencePattern(
									state.getSequencePatterns().get(i));
						}
					}
					limitResultCheckBox.setValue(
							state.isShowPhosphorylatedProteinsOnly(), true);
				}
			});
		}
	}
}
