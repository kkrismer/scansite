package edu.mit.scansite.client.ui.widgets.features;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.states.ChooseUserFileMotifWidgetState;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import gwtupload.client.Utils;

/**
 * A widget that allows the user to upload and edit a motif. The widget is
 * uploaded automatically after a file is selected. If the upload was
 * successful, the motif is displayed. Also, for the user's convenience, the
 * filename of the uploaded file is being displayed. In case the upload fails, a
 * message is displayed.
 * 
 * @author tobieh
 * @author Konstantin Krismer
 */
public class ChooseUserFileMotifWidget extends ChooseMotifWidget implements
		Stateful<ChooseUserFileMotifWidgetState> {
	interface ChooseUserFileMotifWidgetUiBinder extends
			UiBinder<Widget, ChooseUserFileMotifWidget> {
	}

	private static ChooseUserFileMotifWidgetUiBinder uiBinder = GWT
			.create(ChooseUserFileMotifWidgetUiBinder.class);

	private static final String TAG_FILENAME = ScansiteConstants.MOTIF_UPLOAD_TAG_FILENAME;
	private static final String TAG_MOTIF = ScansiteConstants.MOTIF_UPLOAD_TAG_MOTIF;
	private static final String TAG_ERROR = ScansiteConstants.MOTIF_UPLOAD_TAG_ERROR;
	private static final String DEFAULT_NAME = "USER_MOTIF";

	private EditMotifDataWidget motifEditWidget;
	private Motif motif = null;
	
	private boolean showMotifNameSection = true;

	@UiField
	FlowPanel singleUploaderFlowPanel;

	@UiField
	TextBox motifNameTextBox;

	@UiField
	FlowPanel motifDisplayFlowPanel;

	public ChooseUserFileMotifWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				init();
			}
		});

	}

	protected void init() {
		// singleUploader.setServletPath("/scansite/motifFileUpload");
		SingleUploader uploader = new SingleUploader();
		uploader.addOnFinishUploadHandler(onFinishFileUploadHandler);
		uploader.avoidRepeatFiles(false);
		uploader.setAutoSubmit(true);
		uploader.setEnabled(true);
		singleUploaderFlowPanel.add(uploader);
	}

	/**
	 * @return The uploaded motif, or NULL if no motif has been uploaded yet (or
	 *         upload has failed).
	 */
	@Override
	public MotifSelection getMotifSelection() {
		if (motif != null) {
			motif.setDisplayName((motifNameTextBox.getText().isEmpty() ? DEFAULT_NAME
					: motifNameTextBox.getText()));
			motif.setShortName(motif.getDisplayName().replace(" ", ""));
			return new MotifSelection(motif);
		} else {
			return null;
		}
	}

	private IUploader.OnFinishUploaderHandler onFinishFileUploadHandler = new IUploader.OnFinishUploaderHandler() {
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.SUCCESS) {
				// parse server response text
				Document doc = XMLParser.parse(uploader.getServerRawResponse());
				String motifText = Utils.getXmlNodeValue(doc, TAG_MOTIF);
				String error = Utils.getXmlNodeValue(doc, TAG_ERROR);
				if ((error == null || error.isEmpty()) && motifText != null
						&& !motifText.isEmpty()) { // really successful!
					motif = new Motif(motifText);
					String fileName = Utils.getXmlNodeValue(doc, TAG_FILENAME);
					prepareMotifDisplayWidget(fileName);
				} else { // an error has occurred
					showErrorMessage("Upload has failed: " + error);
				}
			} else { // an exception has been thrown?
				showErrorMessage("Upload has failed!");
			}
			// motifDisplayPanel.setVisible(true);
		}
	};

	private void prepareMotifDisplayWidget(String fileName) {
		motifDisplayFlowPanel.clear();
		motifEditWidget = new EditMotifDataWidget();
		motifEditWidget.setMotif(motif);
		HTML checkMotifLabel = new HTML("<h3>User motif verification</h3>");
		HTML fileNameLabel = new HTML("<b>File:</b> " + fileName);
		motifDisplayFlowPanel.add(fileNameLabel);
		motifDisplayFlowPanel.add(checkMotifLabel);
		motifDisplayFlowPanel.add(motifEditWidget);
	}

	@Override
	public ChooseUserFileMotifWidgetState getState() {
		return new ChooseUserFileMotifWidgetState(motifNameTextBox.getValue());
	}

	@Override
	public void setState(final ChooseUserFileMotifWidgetState state) {
		if (state != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					motifNameTextBox.setValue(state.getMotifName());
				}
			});
		}
	}

	public boolean isShowMotifNameSection() {
		return showMotifNameSection;
	}

	public void setShowMotifNameSection(final boolean showMotifNameSection) {
		this.showMotifNameSection = showMotifNameSection;
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				if(showMotifNameSection) {
					DOM.getElementById("motifNameSection").setAttribute("style",
							"display: block;");
				} else {
					DOM.getElementById("motifNameSection").setAttribute("style",
							"display: none;");
				}
			}
		});
	}
}