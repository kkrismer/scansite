package edu.mit.scansite.server.updater;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import edu.mit.scansite.server.dataaccess.file.Downloader;
import edu.mit.scansite.server.updater.transliterator.FileTransliterator;
import edu.mit.scansite.server.updater.transliterator.ProteinTransliteratorFileWriter;
import edu.mit.scansite.server.updater.transliterator.UniProtDatFileTransliterator;

/**
 * @author Tobieh
 */
public class UniProtDbUpdater extends ProteinDbUpdater {

	@Override
	protected String doGetVersion() throws ScansiteUpdaterException {
		Downloader downloader = new Downloader();
		String version = downloader.downloadString(versionFileDownloadUrl);
		version = version.substring(0, version.indexOf("\n"));
		if (version != null) {
			return version.trim();
		}
		return null;
	}

	@Override
	protected FileTransliterator getDbFileTransliterator(String tempFilePath,
			String errorFilePath) throws ScansiteUpdaterException {
		BufferedWriter tempFileWriter;
		BufferedWriter errorWriter;
		try {
			tempFileWriter = new BufferedWriter(new FileWriter(new File(
					tempFilePath)));
			errorWriter = new BufferedWriter(new FileWriter(new File(
					errorFilePath)));
			ProteinTransliteratorFileWriter writer = new ProteinTransliteratorFileWriter(
					tempFileWriter, errorWriter);
			UniProtDatFileTransliterator transliterator = new UniProtDatFileTransliterator(
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
		return from;
	}
}
