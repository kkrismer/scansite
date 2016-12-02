package edu.mit.scansite.client.ui.widgets.features;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.transferobjects.PatternPosition;
import edu.mit.scansite.shared.transferobjects.SequencePattern;
import edu.mit.scansite.shared.transferobjects.states.RegExMethodWidgetState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class RegExMethodWidget extends ScansiteWidget implements
		SequenceMatchMethodWidget, Stateful<RegExMethodWidgetState> {
	interface RegExMethodWidgetUiBinder extends
			UiBinder<Widget, RegExMethodWidget> {
	}

	private static RegExMethodWidgetUiBinder uiBinder = GWT
			.create(RegExMethodWidgetUiBinder.class);

	@UiField
	TextBox regExTextBox;

	public RegExMethodWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				regExTextBox.getElement().setId("regExTextBoxId");
			}
		});
	}

	@Override
	public List<SequencePattern> getSequencePatterns() {
		List<SequencePattern> patterns = new LinkedList<SequencePattern>();
		SequencePattern pattern = new SequencePattern();
		pattern.addPosition(new PatternPosition(regExTextBox.getValue(), false,
				false));
		patterns.add(pattern);
		return patterns;
	}

	@Override
	public boolean showPhosphorylatedProteinsOnly() {
		return false;
	}

	@Override
	public RegExMethodWidgetState getState() {
		return new RegExMethodWidgetState(regExTextBox.getValue());
	}

	@Override
	public void setState(final RegExMethodWidgetState state) {
		if (state != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					regExTextBox.setValue(state.getRegEx());
				}
			});
		}
	}
}
