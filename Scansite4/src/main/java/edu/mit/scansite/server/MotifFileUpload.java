package edu.mit.scansite.server;

import java.io.File;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.webmacro.util.Settings;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.mit.scansite.server.dataaccess.file.MotifFileReader;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.Motif;

import gwtupload.server.exceptions.UploadActionException;

import javax.servlet.http.HttpServletRequest;

/**
 * A servlet for uploading files that uses the GwtUpload-package. It sends
 * either an error-tag or a motif-tag back to the client, dependent on whether
 * the file represents a valid motif, or not.
 * 
 * @author tobieh
 */
@Singleton
public class MotifFileUpload extends MyUploadAction {
	private static final long serialVersionUID = 7029929743201492948L;
	private static final String TAG_FILENAME = ScansiteConstants.MOTIF_UPLOAD_TAG_FILENAME;
	private static final String TAG_MOTIF = ScansiteConstants.MOTIF_UPLOAD_TAG_MOTIF;
	private static final String TAG_ERROR = ScansiteConstants.MOTIF_UPLOAD_TAG_ERROR;

	private File file;

	@Inject
	public MotifFileUpload(Settings settings) {
		this.maxSize = settings.getIntegerSetting("upload.maxupload",
				DEFAULT_REQUEST_LIMIT_KB);
		this.uploadDelay = settings.getIntegerSetting("upload.uploadDelay", 0);
	}

	/**
	 * Retrieves a motif file from the client, parses it, and returns either the
	 * motif, or an error message.
	 */
	@Override
	public String executeAction(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		String response = "";
		if (sessionFiles != null && !sessionFiles.isEmpty()) {
			FileItem item = sessionFiles.get(0);
			if (!item.isFormField()) { // file-upload is not a simple form
										// field!
				try {
					// Create a temporary file placed in the default system
					// temp-folder in order to read from it
					file = File.createTempFile("scansiteMotif-"
							+ item.getName().replaceAll("\\s", "_") + "-",
							".motif");
					item.write(file);
					MotifFileReader mfReader = new MotifFileReader();
					Motif m;
					try {
						m = mfReader.getMotif(file.getAbsolutePath());
						response += "<" + TAG_FILENAME + ">" + item.getName()
								+ "</" + TAG_FILENAME + ">";
						response += "<" + TAG_MOTIF + ">" + m.toString() + "</"
								+ TAG_MOTIF + ">\n";
						response += "<" + TAG_ERROR + "></" + TAG_ERROR + ">\n";
					} catch (Exception e) {
						response += "<" + TAG_ERROR + ">" + e.getMessage()
								+ "</" + TAG_ERROR + ">";
					}
				} catch (Exception e) {
					response += "<" + TAG_ERROR + ">" + e.getMessage() + "</"
							+ TAG_ERROR + ">";
				}
			}
		}
		removeItem(request);
		return "<response>\n" + response + "</response>\n";
	}

	/**
	 * Remove a file when the user sends a delete request.
	 */
	public void removeItem(HttpServletRequest request)
			throws UploadActionException {
		removeSessionFileItems(request); // removes the file from the SESSION
											// variable
		if (file != null) {
			file.delete();
		}
	}
}
