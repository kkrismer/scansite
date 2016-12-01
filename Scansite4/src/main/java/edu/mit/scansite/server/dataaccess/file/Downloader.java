package edu.mit.scansite.server.dataaccess.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Tobieh
 */
public class Downloader {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// private static final boolean DEBUG = false; // set to true for debugging
	private String ENCODING = "UTF-8";

	public Downloader() {
	}

	public void setEncoding(String enc) {
		ENCODING = enc;
	}

	/**
	 * Downloads the file specified by the given URL and saves it in the given
	 * path.
	 * 
	 * @param fileUrl
	 *            URL of the file that is going to be downloaded.
	 * @param outputPath
	 *            The path+filename where the new file is going to be saved.
	 * @throws ScansiteUpdaterException
	 */
	public void downloadFile(URL fileUrl, String localOutputPath)
			throws ScansiteUpdaterException {
		final int BUFFER_SIZE = 1024;
		OutputStream outstream = null;
		URLConnection URLConn = null;
		InputStream instream = null;
		try {
			byte[] buffer;
			int bytesRead = 0;
			// long bytesWritten=0;
			File f = new File(localOutputPath);
			outstream = new BufferedOutputStream(new FileOutputStream(f));

			URLConn = fileUrl.openConnection();
			instream = URLConn.getInputStream();
			logger.info("downloading large file...");

			buffer = new byte[BUFFER_SIZE];

			final int flushStepSize = (BUFFER_SIZE * BUFFER_SIZE * 10); // flush
																		// in
																		// steps
																		// of
																		// 10MB
			long byteCount = 0;
			// if (DEBUG) {
			// System.out.println("DOWNLOAD STARTED - " + localOutputPath);
			// }
			while ((bytesRead = instream.read(buffer)) != -1) {
				outstream.write(buffer, 0, bytesRead);
				// bytesWritten += bytesRead;
				byteCount += bytesRead;
				if (byteCount >= flushStepSize) {
					byteCount = 0;
					// if (DEBUG) {
					// System.out.println(bytesWritten / (1024 * 1024) +
					// "MB downloaded (" + localOutputPath + ")");
					// }
					outstream.flush();
				}
			}
			// if (DEBUG) {
			// System.out.println(bytesWritten / (1024 * 1024) +
			// "MB downloaded\n" + "DOWNLOAD FINISHED - " + localOutputPath +
			// "\n");
			// }
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new ScansiteUpdaterException(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ScansiteUpdaterException(e.getMessage(), e);
		} finally {
			try {
				if (instream != null) {
					instream.close();
				}
				if (outstream != null) {
					outstream.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new ScansiteUpdaterException(e.getMessage(), e);
			}
		}
	}

	/**
	 * Downloads the file specified by the given URL and saves it in a String.
	 * 
	 * @param fileUrl
	 *            URL of the file that is going to be downloaded.
	 * @return The String where the downloaded content is going to be saved.
	 * @throws ScansiteUpdaterException
	 */
	public String downloadString(URL fileUrl) throws ScansiteUpdaterException {
		final int BUFFER_SIZE = 1024;
		URLConnection URLConn = null;
		InputStream instream = null;
		StringBuilder s = new StringBuilder();
		try {
			byte[] buffer;

			URLConn = fileUrl.openConnection();
			instream = URLConn.getInputStream();
			buffer = new byte[BUFFER_SIZE];

			@SuppressWarnings("unused")
			int bytesRead = 0;
			while ((bytesRead = instream.read(buffer)) != -1) {
				String str = new String(buffer, ENCODING);
				s.append(String.valueOf(str));
			}
			return s.toString();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ScansiteUpdaterException(e.getMessage(), e);
		} finally {
			try {
				if (instream != null) {
					instream.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new ScansiteUpdaterException(e.getMessage(), e);
			}
		}
	}
}
