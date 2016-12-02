package edu.mit.scansite.server.updater;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import edu.mit.scansite.server.dataaccess.file.Downloader;
import edu.mit.scansite.server.updater.transliterator.FileTransliterator;
import edu.mit.scansite.server.updater.transliterator.OrthologyTransliteratorFileWriter;
import edu.mit.scansite.server.updater.transliterator.SwissProtOrthologyDatFileTransliterator;

/**
 * @author Konstantin Krismer
 */
public class SwissProtOrthologyDbUpdater extends OrthologyDbUpdater {

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
			OrthologyTransliteratorFileWriter writer = new OrthologyTransliteratorFileWriter(
					tempFileWriter, errorWriter);
			SwissProtOrthologyDatFileTransliterator transliterator = new SwissProtOrthologyDatFileTransliterator(
					getReaders(), writer);
			return transliterator;
		} catch (IOException e) {
			throw new ScansiteUpdaterException(
					"Error accessing temporary files!", e);
		}
	}

	@Override
	protected List<URL> prepareDownloadUrls(List<URL> dbDownloadUrl)
			throws ScansiteUpdaterException {
		return dbDownloadUrl;
	}
}
