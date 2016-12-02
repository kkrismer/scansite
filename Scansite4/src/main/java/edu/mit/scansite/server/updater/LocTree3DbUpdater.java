package edu.mit.scansite.server.updater;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import edu.mit.scansite.server.updater.transliterator.FileTransliterator;
import edu.mit.scansite.server.updater.transliterator.LocTree3LC3FileTransliterator;
import edu.mit.scansite.server.updater.transliterator.LocalizationTransliteratorFileWriter;

/**
 * @author Konstantin Krismer
 */
public class LocTree3DbUpdater extends LocalizationDbUpdater {

	@Override
	protected String doGetVersion() throws ScansiteUpdaterException {
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
			LocalizationTransliteratorFileWriter writer = new LocalizationTransliteratorFileWriter(
					tempFileWriter, errorWriter);
			LocTree3LC3FileTransliterator transliterator = new LocTree3LC3FileTransliterator(
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
