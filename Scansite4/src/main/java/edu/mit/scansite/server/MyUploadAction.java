package edu.mit.scansite.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.mit.scansite.shared.ScansiteConstants;
import gwtupload.server.AbstractUploadListener;
import gwtupload.server.UploadAction;
import gwtupload.server.UploadServlet;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.server.exceptions.UploadCanceledException;

/**
 * A version of UploadAction that overrides the doPost-method in order to not
 * send the response as CDATA-value. This is the only difference to the original
 * doPost-method. This way, the client can easily parse the result as XML.
 * 
 * @author tobieh
 */
public class MyUploadAction extends UploadAction {
	private static final long serialVersionUID = 8403104909673225152L;
	private static final String TAG_ERROR = ScansiteConstants.MOTIF_UPLOAD_TAG_ERROR;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String error = null;
		String message = null;

		perThreadRequest.set(request);
		try {
			// Receive the files and form elements, updating the progress status
			error = super.parsePostRequest(request, response);
			if (error == null) {
				// Call to the user code
				message = executeAction(request, getSessionFileItems(request));
			}
		} catch (UploadCanceledException e) {
			renderXmlResponse(request, response, "<cancelled>true</cancelled>");
			return;
		} catch (UploadActionException e) {
			logger.info("ExecuteUploadActionException: " + e);
			error = e.getMessage();
		} catch (Exception e) {
			logger.info("Exception " + e);
			error = e.getMessage();
		} finally {
			perThreadRequest.set(null);
		}

		AbstractUploadListener listener = getCurrentListener(request);
		if (error != null) {
			renderXmlResponse(request, response, "<" + TAG_ERROR + ">" + error
					+ "</" + TAG_ERROR + ">");
			if (listener != null) {
				listener.setException(new RuntimeException(error));
			}
			UploadServlet.removeSessionFileItems(request);
		} else {
			Map<String, String> stat = new HashMap<String, String>();
			getFileItemsSummary(request, stat);
			if (message != null) {
				stat.put("message", "\n" + message + "\n>\n");
			}
			renderXmlResponse(request, response, statusToString(stat), true);
		}

		finish(request);
	}
}
