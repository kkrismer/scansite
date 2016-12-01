package edu.mit.scansite.client.ui.widgets.features;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LegendElement;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.AminoAcidRegex;
import edu.mit.scansite.shared.transferobjects.PatternPosition;
import edu.mit.scansite.shared.transferobjects.SequencePattern;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SequencePatternWidget extends ScansiteWidget {
	interface SequencePatternWidgetUiBinder extends
			UiBinder<Widget, SequencePatternWidget> {
	}

	private static final String TEXTBOX_WIDTH = "5ex";
	private static final String LABEL_WIDTH = "6ex";

	private static SequencePatternWidgetUiBinder uiBinder = GWT
			.create(SequencePatternWidgetUiBinder.class);

	private boolean withFixedPosition = true;
	private boolean displayCheckBoxes = false;

	private TextBox[] textBoxes = new TextBox[ScansiteConstants.WINDOW_SIZE];
	private CheckBox[] checkBoxes = new CheckBox[ScansiteConstants.WINDOW_SIZE];

	@UiField
	LegendElement regExLegend;

	@UiField(provided = true)
	Grid mainGrid;

	@UiField
	FlowPanel displayFlowPanel;

	@UiField
	HTMLPanel wildcardsDescriptionHTMLPanel;

	public SequencePatternWidget(final int number, boolean withFixedPosition,
			final boolean displayCheckBoxes,
			final boolean showWildcardExplanation, final boolean displayInput) {
		this(
				Integer.toString(number)
						+ ". Enter sequence pattern below using single-letter amino acid code and wild cards."
						+ (displayCheckBoxes ? " Check the boxes below the position specific pattern to indicate phosphorylation sites (optionally)."
								: ""), withFixedPosition, displayCheckBoxes,
				showWildcardExplanation, displayInput);
	}

	public @UiConstructor SequencePatternWidget(final String legend,
			boolean withFixedPosition, final boolean displayCheckBoxes,
			final boolean showWildcardExplanation, final boolean displayInput) {
		this.withFixedPosition = withFixedPosition;
		this.displayCheckBoxes = displayCheckBoxes;
		initGrid();
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				regExLegend.setInnerText(legend);
				if (displayInput) {
					initDisplayPanel();
				}
				displayFlowPanel.setVisible(displayInput);
				wildcardsDescriptionHTMLPanel
						.setVisible(showWildcardExplanation);
			}
		});
	}

	public void initGrid() {
		if (displayCheckBoxes) {
			mainGrid = new Grid(3, ScansiteConstants.WINDOW_SIZE);
		} else {
			mainGrid = new Grid(2, ScansiteConstants.WINDOW_SIZE);
		}

		for (int i = 0; i < ScansiteConstants.WINDOW_SIZE; ++i) {
			// adding description labels to the grid
			int pos = i - ScansiteConstants.HALF_WINDOW;
			Label label = new Label((pos > 0 ? "+" : "") + String.valueOf(pos));
			label.setWidth(LABEL_WIDTH);
			label.addStyleName("sequencePatternWidgetLabelColor");
			if (i == ScansiteConstants.WINDOW_CENTER_INDEX && withFixedPosition) {
				label.removeStyleName("sequencePatternWidgetLabelColor");
				label.addStyleName("sequencePatternWidgetFixedPosLabelColor");
			}
			label.addStyleName("alignCenter");
			mainGrid.setWidget(1, i, label);

			// adding text boxes to the grid
			textBoxes[i] = new TextBox();
			textBoxes[i].setWidth(TEXTBOX_WIDTH);
			if (i != ScansiteConstants.WINDOW_CENTER_INDEX || withFixedPosition) {
				mainGrid.setWidget(0, i, textBoxes[i]);
			} else {
				Label empty = new Label("");
				empty.setWidth(TEXTBOX_WIDTH);
				mainGrid.setWidget(0, i, empty);
			}

			// adding check boxes to the grid
			if (displayCheckBoxes) {
				checkBoxes[i] = new CheckBox();
				checkBoxes[i]
						.addStyleName("sequencePatternWidgetPhosphorylationCheckBox");
				mainGrid.setWidget(2, i, checkBoxes[i]);
			}
		}
	}

	private void initDisplayPanel() {
		for (int i = 0; i < textBoxes.length; ++i) {
			textBoxes[i].addKeyUpHandler(new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					applyUserPattern(getSequencePattern()
							.getHtmlFormattedRegEx());
				}
			});
		}

		if (displayCheckBoxes) {
			for (int i = 0; i < checkBoxes.length; ++i) {
				checkBoxes[i]
						.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
							@Override
							public void onValueChange(
									ValueChangeEvent<Boolean> event) {
								applyUserPattern(getSequencePattern()
										.getHtmlFormattedRegEx());
							}
						});
			}
		}
	}

	private void applyUserPattern(String userPatternHtml) {
		displayFlowPanel.clear();
		HTML pattern = new HTML("<div class='displayQuickPattern' >Pattern: "
				+ userPatternHtml + "</div>");
		displayFlowPanel.add(pattern);
	}

	/**
	 * @return A String array of size WINDOW_SIZE (15) that contains all chosen
	 *         AA - one letter codes for each position in the window.
	 */
	public String[] getInput() {
		String[] values = new String[ScansiteConstants.WINDOW_SIZE];
		for (int i = 0; i < ScansiteConstants.WINDOW_SIZE; ++i) {
			String tboxVal = textBoxes[i].getValue().trim();
			tboxVal.replaceAll("\\s", "");
			StringBuilder val = new StringBuilder();
			for (int j = 0; j < tboxVal.length(); ++j) {
				val.append(AminoAcidRegex.getAaValues(tboxVal.charAt(j)));
			}
			values[i] = val.toString();
		}
		return values;
	}

	/**
	 * @return A regular expression of AA - one letter codes and whether a
	 *         phosphorylation is expected for each position.
	 */
	public SequencePattern getSequencePattern() {
		SequencePattern pattern = new SequencePattern();

		int iFirst = 0;
		boolean iFirstSet = false;
		int iLast = ScansiteConstants.WINDOW_SIZE;
		for (int i = 0; i < ScansiteConstants.WINDOW_SIZE; ++i) {
			String tboxVal = textBoxes[i].getValue().trim().toUpperCase();
			boolean checkBoxVal = displayCheckBoxes ? checkBoxes[i].getValue()
					: false;
			tboxVal = tboxVal.replaceAll("\\s", "");
			textBoxes[i].setValue(tboxVal);
			if (!iFirstSet && (!tboxVal.isEmpty() || checkBoxVal)) {
				iFirst = i;
				iFirstSet = true;
				pattern.setStartIndex(iFirst);
			}
			if (!tboxVal.isEmpty() || checkBoxVal) {
				iLast = i + 1;
			}
		}
		for (int i = iFirst; i < iLast; ++i) {
			String tboxVal = textBoxes[i].getValue();
			boolean checkBoxVal = displayCheckBoxes ? checkBoxes[i].getValue()
					: false;
			StringBuilder val = new StringBuilder();
			if (tboxVal != null) {
				for (int j = 0; j < tboxVal.length(); ++j) {
					val.append(AminoAcidRegex.getAaRegex(tboxVal.charAt(j)));
				}
				if (val.toString().isEmpty()) {
					val.append(AminoAcidRegex.getAaRegex('X'));
				}
				pattern.addPosition(new PatternPosition(val.toString(),
						checkBoxVal));
			}
		}
		return pattern;
	}

	public void setSequencePattern(SequencePattern pattern) {
		int i = pattern.getStartIndex();
		for (PatternPosition position : pattern.getPositions()) {
			textBoxes[i].setValue(position.getAminoAcidCodes());
			if (displayCheckBoxes) {
				checkBoxes[i]
						.setValue(position.isExpectedPhosphorylationSite());
			}
			++i;
		}
	}

	public TextBox[] getTextBoxes() {
		return textBoxes;
	};

	public void setEnabled(boolean enabled) {
		for (TextBox textBox : textBoxes) {
			textBox.setEnabled(enabled);
		}
		for (CheckBox checkBox : checkBoxes) {
			checkBox.setEnabled(enabled);
		}
	}
}
