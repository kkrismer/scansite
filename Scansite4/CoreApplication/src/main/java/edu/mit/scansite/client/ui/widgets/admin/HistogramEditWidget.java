package edu.mit.scansite.client.ui.widgets.admin;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.HistogramEditChangeEvent;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.shared.ImagePaths;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.Histogram;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramEditWidget extends Composite {
	private int histogramNr;

	private TextBox stringencyHighTextBox = new TextBox();
	private TextBox stringencyMediumTextBox = new TextBox();
	private TextBox stringencyLowTextBox = new TextBox();

	private Label titleLabel;
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel imagePanel = new VerticalPanel();
	private Grid stringencyPanel = new Grid(2, 1);

	private Button applyStringencyButton = new Button("Apply");

	private Histogram histogram;

	public HistogramEditWidget() {
		initWidget(mainPanel);
	}

	private boolean allValuesSet() {
		boolean allOk = true;
		if (stringencyHighTextBox.getText().isEmpty()) {
			stringencyHighTextBox.addStyleName("highlightInputField");
			allOk = false;
		} else {
			try {
				Double.valueOf(stringencyHighTextBox.getText());
				stringencyHighTextBox.removeStyleName("highlightInputField");
			} catch (Exception e) {
				stringencyHighTextBox.addStyleName("highlightInputField");
				allOk = false;
			}
		}
		if (stringencyMediumTextBox.getText().isEmpty()) {
			stringencyMediumTextBox.addStyleName("highlightInputField");
			allOk = false;
		} else {
			try {
				Double.valueOf(stringencyMediumTextBox.getText());
				stringencyMediumTextBox.removeStyleName("highlightInputField");
			} catch (Exception e) {
				stringencyMediumTextBox.addStyleName("highlightInputField");
				allOk = false;
			}
		}
		if (stringencyLowTextBox.getText().isEmpty()) {
			stringencyLowTextBox.addStyleName("highlightInputField");
			allOk = false;
		} else {
			try {
				Double.valueOf(stringencyLowTextBox.getText());
				stringencyLowTextBox.removeStyleName("highlightInputField");
			} catch (Exception e) {
				stringencyLowTextBox.addStyleName("highlightInputField");
				allOk = false;
			}
		}
		if (!allOk) {
			EventBus.instance()
					.fireEvent(
							new MessageEvent(
									MessageEventPriority.ERROR,
									"The marked input field(s) do not contain a valid value",
									this.getClass().toString(), null));
		}
		return allOk;
	}

	public void setHistogram(Histogram histogram) {
		this.histogram = histogram;
		imagePanel.clear();
		Image img = new Image(histogram.getImageFilePath());
		imagePanel.add(img);

		Image popImg = new Image(histogram.getImageFilePath());
		final PopupPanel imagePopup = new PopupPanel(true);
		imagePopup.setAnimationEnabled(true);
		imagePopup.setWidget(popImg);

		popImg.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				imagePopup.hide();
			}
		});

		img.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!imagePopup.isShowing()) {
					imagePopup.center();
				}
			}
		});

		stringencyHighTextBox.setText(String.valueOf(histogram
				.getThresholdHigh()));
		stringencyMediumTextBox.setText(String.valueOf(histogram
				.getThresholdMedium()));
		stringencyLowTextBox
				.setText(String.valueOf(histogram.getThresholdLow()));

		applyStringencyButton.setEnabled(true);
	}

	public void init(int histNr, String databaseName, String speciesName,
			String motifName) {
		this.histogramNr = histNr;
		titleLabel = new Label("Preview of Histogram for Motif: " + motifName
				+ "; Database: " + databaseName + "; Taxon: " + speciesName);
		titleLabel.addStyleName("subtitleLabel");
		initImagePanel();
		initStringencyPanel();
		mainPanel.add(titleLabel);
		mainPanel.add(imagePanel);
		mainPanel.add(stringencyPanel);

		applyStringencyButton.setEnabled(false);
		applyStringencyButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (allValuesSet()) {
					fireHistogramChangeEvent();
				}
			}
		});
	}

	public Histogram getHistogram() {
		try {
			histogram.setThresholdHigh(Double.valueOf(stringencyHighTextBox
					.getValue()));
		} catch (Exception e) {
		}
		try {
			histogram.setThresholdMedium(Double.valueOf(stringencyMediumTextBox
					.getValue()));
		} catch (Exception e) {
		}
		try {
			histogram.setThresholdLow(Double.valueOf(stringencyLowTextBox
					.getValue()));
		} catch (Exception e) {
		}
		return histogram;
	}

	protected void fireHistogramChangeEvent() {
		HistogramEditChangeEvent event = new HistogramEditChangeEvent();
		histogram.setThresholdHigh(Double.valueOf(stringencyHighTextBox
				.getValue()));
		histogram.setThresholdMedium(Double.valueOf(stringencyMediumTextBox
				.getValue()));
		histogram.setThresholdLow(Double.valueOf(stringencyLowTextBox
				.getValue()));
		event.setHistogram(histogram);
		event.setHistogramNumber(histogramNr);
		EventBus.instance().fireEvent(event);
	}

	public void initImagePanel() {
		imagePanel.clear();
		imagePanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		imagePanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		imagePanel.setSize(String.valueOf(ScansiteConstants.HIST_IMAGE_WIDTH)
				+ "px", String.valueOf(ScansiteConstants.HIST_IMAGE_HEIGHT)
				+ "px");

		VerticalPanel labelPanel = new VerticalPanel();
		labelPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		labelPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);

		labelPanel.setPixelSize(imagePanel.getOffsetWidth(),
				imagePanel.getOffsetHeight());
		Label waitLabel = new Label("Waiting for histogram...");

		waitLabel.setWidth("100%");
		Label takesTimeLabel = new Label("This may take some time.");
		waitLabel.setWidth("100%");

		labelPanel.add(waitLabel);
		labelPanel.add(ImagePaths.getStaticImage(ImagePaths.WAIT_HUGE));
		labelPanel.add(takesTimeLabel);
		labelPanel.setStyleName("someCellSpacing");

		imagePanel.add(labelPanel);
	}

	private void initStringencyPanel() {
		Label stringencyLabel = new Label("Stringency values:");
		stringencyLabel.addStyleName("subsubtitleLabel");
		HorizontalPanel valuePanel = new HorizontalPanel();

		Grid gLow = new Grid(1, 2);
		gLow.setWidget(0, 0, new Label("Low:"));
		gLow.setWidget(0, 1, stringencyLowTextBox);

		Grid gMed = new Grid(1, 2);
		gMed.setWidget(0, 0, new Label("Medium:"));
		gMed.setWidget(0, 1, stringencyMediumTextBox);

		Grid gHigh = new Grid(1, 2);
		gHigh.setWidget(0, 0, new Label("High:"));
		gHigh.setWidget(0, 1, stringencyHighTextBox);

		valuePanel.add(gLow);
		valuePanel.add(gMed);
		valuePanel.add(gHigh);
		valuePanel.add(applyStringencyButton);
		valuePanel.addStyleName("setStringencyPanelSpacing");

		stringencyPanel.setWidget(0, 0, stringencyLabel);
		stringencyPanel.setWidget(1, 0, valuePanel);

	}

	public void setApplyButtonEnabled(boolean isEnabled) {
		applyStringencyButton.setEnabled(isEnabled);
	}
}
