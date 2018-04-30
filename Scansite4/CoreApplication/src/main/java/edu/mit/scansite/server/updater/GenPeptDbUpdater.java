package edu.mit.scansite.server.updater;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.mit.scansite.server.dataaccess.file.Downloader;
import edu.mit.scansite.server.updater.transliterator.GenPeptGenbankFileTransliterator;
import edu.mit.scansite.server.updater.transliterator.ProteinFileTransliterator;
import edu.mit.scansite.server.updater.transliterator.ProteinTransliteratorFileWriter;
import org.apache.commons.io.FilenameUtils;

/**
 * @author Tobieh
 */
public class GenPeptDbUpdater extends ProteinDbUpdater {
	private static String FILE_PREFIX = "complete.";
	private static String FILE_SUFFIX = ".protein.gpff.gz";

	private String VERSION_PREFIX = "RefSeq-release";
	private String VERSION_SUFFIX = ".txt";

	@Override
	protected String doGetVersion() throws ScansiteUpdaterException {
		Downloader downloader = new Downloader();
		String s = downloader.downloadString(versionFileDownloadUrl);
		String lines[] = s.trim().split("\n");
		for (String line : lines) {
			if (line.endsWith(VERSION_SUFFIX)) {
				line = line.substring(line.lastIndexOf(" ") + 1).trim();
				if (line.startsWith(VERSION_PREFIX)) {
					line = line.substring(0, line.lastIndexOf(VERSION_SUFFIX));
					return line;
				}
			}
		}
		return null;
	}

	@Override
	protected ProteinFileTransliterator getDbFileTransliterator(
			String tempFilePath, String errorFilePath)
			throws ScansiteUpdaterException {
		BufferedWriter tempFileWriter;
		BufferedWriter errorWriter;
		try {
			tempFileWriter = new BufferedWriter(new FileWriter(new File(
					tempFilePath), true));
			errorWriter = new BufferedWriter(new FileWriter(new File(
					errorFilePath), true));
			ProteinTransliteratorFileWriter writer = new ProteinTransliteratorFileWriter(
					tempFileWriter, errorWriter);
			GenPeptGenbankFileTransliterator transliterator = new GenPeptGenbankFileTransliterator(
					getReaders(), writer);
			return transliterator;
		} catch (IOException e) {
			throw new ScansiteUpdaterException(
					"Error accessing temporary files!", e);
		}
	}

	@Override
	protected List<URL> prepareDownloadUrls(List<URL> from)
			throws ScansiteUpdaterException {
		try {
			Downloader downloader = new Downloader();
			URL url = from.get(0);
			from.remove(0);
			String s = downloader.downloadString(url);
			String lines[] = s.trim().split("\n");
			for (String line : lines) {
				if (line.endsWith(FILE_SUFFIX)) {
					line = line.substring(line.lastIndexOf(" ") + 1).trim();
					if (line.startsWith(FILE_PREFIX)) {
						from.add(new URL(url.toString() + line));
					}
				}
			}
		} catch (Exception e) {
			throw new ScansiteUpdaterException(e.getMessage(), e);
		}
		return from;
	}



	public void runDownloads(URL baseURL) throws IOException, InterruptedException {
        if (System.getProperty("os.name").contains("Windows")) {
            logger.info("Genpept is not supposed to be setup on a test environment and is currently not available.");
            logger.info("Please make sure to isntall wget for Windows and add " +
                    "'C:\\Program Files (x86)\\GnuWin32\\bin' to the system path! " +
                    "Any other way mirroring the database does not work");
        }

	    List<URL> from = new ArrayList<>();
	    from.add(baseURL);
        try {
            List<URL> downloadUrls = prepareDownloadUrls(from);

            int count = 0;
            logger.info("Starting wget downloads...");
            for(URL downloadUrl : downloadUrls) {
                //String command = "wget -P ./temp/ -m " + downloadUrl;
                String fileName = FilenameUtils.getBaseName(downloadUrl.toString())
                        + "." + FilenameUtils.getExtension(downloadUrl.toString());
                String outputFileName = "genpept_" + fileName;
                String command = "wget -O " + outputFileName + " " + downloadUrl;
                Process p = Runtime.getRuntime().exec(command, null, new File("./temp/"));

                Thread infoLogs = getInfoLogger(p);
                Thread errorLogs = getErrorLogger(p);

                infoLogs.start();
                errorLogs.start();
//                logger.info("Downloading " + outputFileName);

                p.waitFor();
                count++;
                logger.info("Downloads finished " + count + "/" + downloadUrls.size());

                infoLogs.interrupt();
                errorLogs.interrupt();

                infoLogs.join();
                errorLogs.join();
            }
        } catch (ScansiteUpdaterException e) {
            e.printStackTrace();
        }
	}


	private Thread getInfoLogger(Process p) {
		return new Thread(() -> {
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
		});
	}

	private Thread getErrorLogger(Process p) {
		return new Thread(() -> {
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
		});
	}

}
