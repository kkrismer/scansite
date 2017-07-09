package edu.mit.scansite.server.dataaccess.file;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.commands.motif.MotifCountGetCommand;
import edu.mit.scansite.server.updater.ScansiteUpdaterException;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @author Thomas Bernwinkler
 * @author Tobieh
 */
public class Downloader {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String ENCODING = "UTF-8";

	public Downloader() {
	}


	/**
      * Edits the SSL default context in a way that allows any self signed encryption certificate
      * This is OK at this point since the data source is known and trusted
      * Any other way one would have to edit the code each time the certificate would change
      *
      * @param url URL which is used for connecting to the data source
      *
      * @return HttpsURLConnection Actual connection which is used for downloading
      *
      * @throws NoSuchAlgorithmException Based on used SSLContext functions
      * @throws KeyManagementException Based on used SSLContext functions
      * @throws IOException Based on used SSLContext functions
      */
	private HttpsURLConnection prepareConnection(URL url) throws
			NoSuchAlgorithmException, KeyManagementException, IOException {
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
		SSLContext.setDefault(ctx);

		return (HttpsURLConnection) url.openConnection();
	}


	/**
	 * Downloads the file specified by the given URL and saves it in the given path.
	 * 
	 * @param fileUrl
	 *            URL of the file that is going to be downloaded.
	 * @param localOutputPath
	 *            The path+filename where the new file is going to be saved.
	 * @throws ScansiteUpdaterException
	 */
	public void downloadFile(URL fileUrl, String localOutputPath) throws ScansiteUpdaterException {
        final int unit_factor = 1024;
//        OutputStream outstream = null;
//        InputStream instream = null;

        HttpsURLConnection httpsConn;
        URLConnection httpConn;

        //final int position = 0;
        //final Long count = Long.MAX_VALUE; //maximum transfer 2^63 bytes, more than any meaningful file size at this point
        long downloadSize = -1;
        long formattedSize = 0;

        try {
            if (fileUrl.toString().startsWith("https")) {
                httpsConn = prepareConnection(fileUrl);
                downloadSize = httpsConn.getContentLength();
            } else if (fileUrl.toString().startsWith("ftp://ftp.ebi.ac.uk")) {
                String downloadFileName = FilenameUtils.getName(fileUrl.getFile());
                String metaLink = fileUrl.toString().replaceFirst(downloadFileName, "RELEASE.metalink");
                try {
                    downloadSize = getSizeFromMetalink(metaLink, downloadFileName);
                } catch (MalformedURLException | ScansiteUpdaterException e) {
                    logger.warn("Could not retrieve file size from metalink file. Setting to undefined (-1) instead.");
                    downloadSize = -1;
                }

            } else {
                httpConn = fileUrl.openConnection();
                downloadSize = httpConn.getContentLength();
            }

            formattedSize = downloadSize / (unit_factor * unit_factor);
            if (formattedSize < 1000) {
                logger.info("Attempting to download " + downloadSize + " bytes (" + formattedSize + " MB).");
            } else {
                logger.info("Attempting to download " + downloadSize + " bytes (" + formattedSize + " MB , or " + (formattedSize / unit_factor) + " GB).");
            }

//			FileOutputStream fos = new FileOutputStream(localOutputPath);
//			ReadableByteChannel rbc = Channels.newChannel(fileUrl.openStream());
//			fos.getChannel().transferFrom(rbc, position, count);

//			fos.close();
//			rbc.close();
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        //##############################################################################################################
        //##############################################################################################################

        logger.info("Current system: " + System.getProperty("os.name"));
        String command = "";
        if (System.getProperty("os.name").contains("Windows")) {
            command = "powershell -Command \"Invoke-WebRequest " + fileUrl + " -OutFile " + localOutputPath + "\"";
            logger.info("Launching Windows specific download using PowerShell... (This might take some time)");
        } else {
            command = "wget -O " + localOutputPath + " " + fileUrl;
            logger.info("Launching Linux specific download using wget... (This might take some time)");
        }

        try {
            Process p = Runtime.getRuntime().exec(command, null);
            // ##############################################

            new Thread(new Runnable() {
                public void run() {
                    BufferedReader stdInput = new BufferedReader(new
                            InputStreamReader(p.getInputStream()));

                    // read the output from the command
                    String s = null;
                    try {
                        while ((s = stdInput.readLine()) != null) {
                            System.out.println("[Download] (I) Process: " + s);
                        }
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                }
            }).start();

            new Thread(new Runnable() {
                public void run() {
                    BufferedReader stdError = new BufferedReader(new
                            InputStreamReader(p.getErrorStream()));

                    // read any errors from the attempted command, also wget progress logs on Linux
                    String s = null;
                    try {
                        while ((s = stdError.readLine()) != null) {
                            System.out.println("[Download] (E) Process: " + s);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            long expectedSize = downloadSize; // separate variable due to threading
            final boolean[] downloadSucceeded = {true};

            String genpeptUrl = "ftp://ftp.ncbi.nlm.nih.gov/refseq/release/complete/";
            if(!fileUrl.toString().startsWith(genpeptUrl)) {

                Thread healthCheck = new Thread(new Runnable() {
                    public void run() {
                        long oneGigaByte = unit_factor; //MB
                        //monitor progress
                        long previousFileSize = 0;
                        long currentFileSize = 1;

                        try {
                            while (previousFileSize < currentFileSize &&
                                    (currentFileSize < expectedSize || expectedSize < 0)) { //could not read download size
                                previousFileSize = currentFileSize;
                                long megaBytes = expectedSize / (unit_factor * unit_factor);

                                if (megaBytes < oneGigaByte) {
                                    TimeUnit.MINUTES.sleep(5);
                                } else {
                                    TimeUnit.MINUTES.sleep(10);
                                }
                                File tempStatus = new File(localOutputPath);
                                currentFileSize = tempStatus.length();

                                if (megaBytes < oneGigaByte) {
                                    logger.info("[Download Health Check] Progress: "
                                            + (currentFileSize / (unit_factor * unit_factor)) + "/" + megaBytes + " MB");
                                } else {
                                    logger.info("[Download Health Check] Progress: "
                                            + (currentFileSize / (unit_factor * unit_factor)) + "/" + megaBytes + " MB, "
                                            + "or " + (megaBytes / oneGigaByte) + " GB");
                                }
                            }

                            if (currentFileSize < expectedSize || expectedSize < 0) {
                                logger.warn("[Download Health Check] Download failed or paused for at least 5 minutes. Attempting to restart...");
                                downloadSucceeded[0] = false;
                                p.destroy();
                            } else {
                                logger.info("Waiting for 15 minutes for the download process to finish...");
                                TimeUnit.MINUTES.sleep(15);
                                logger.info("The download process has exceeded the allowed time in idle. Attepting to restart the download...");
                                downloadSucceeded[0] = false;
                                p.destroy();
                            }
                        } catch (InterruptedException e) {
                            logger.info("Health check was interrupted as the download finished.");
                        }
                    }
                });
                healthCheck.start();

                // ##############################################

                int exitVal = p.waitFor(); //exit val is for debugging purposes only

                healthCheck.interrupt();
                healthCheck.join();
            } else {
                int exitVal = p.waitFor();
            }
            if (!downloadSucceeded[0]) {
                downloadFile(fileUrl, localOutputPath);
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        //#############################################################################################################

        logger.info("Completed download from URL: " + fileUrl);
        logger.info("Saved content in file: " + localOutputPath);
    }


    private long getSizeFromMetalink(String uri, String downloadFileName) throws MalformedURLException, ScansiteUpdaterException {
	    URL url = new URL(uri);
	    String metalinkContent = downloadString(url);
	    long downloadFileSize = -1;

	    if (metalinkContent.contains(downloadFileName)) {
	        String [] lines = metalinkContent.split("\n");
	        boolean isFileName = false;
            for (String line : lines) {
                if (isFileName) {
                    String prefix = "<size>";
                    String postfix = "</size>";
                    String fileSizeStr = line.replaceAll(prefix, "");
                    fileSizeStr = fileSizeStr.replaceAll(postfix, "");
                    fileSizeStr = fileSizeStr.trim().replaceAll("\n ", "");
                    downloadFileSize = Long.valueOf(fileSizeStr);
                }
                isFileName = line.contains(downloadFileName) && !line.contains("<url");
            }
        }
        return downloadFileSize;
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
		InputStream instream = null;
		StringBuilder s = new StringBuilder();
		try {
			byte[] buffer;

			if(fileUrl.toString().startsWith("https")) {
				HttpsURLConnection httpsConn = prepareConnection(fileUrl);
				instream = httpsConn.getInputStream();
			} else {
				URLConnection httpConn = fileUrl.openConnection();
				instream = httpConn.getInputStream();
			}
			buffer = new byte[BUFFER_SIZE];

			@SuppressWarnings("unused")
			int bytesRead = 0;
			while ((bytesRead = instream.read(buffer)) != -1) {
				String str = new String(buffer, ENCODING);
				s.append(String.valueOf(str));
			}
			return s.toString();
		} catch (IOException | NoSuchAlgorithmException
                | KeyManagementException e) {
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


	private static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
