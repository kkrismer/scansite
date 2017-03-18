package edu.mit.scansite.server.updater;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import edu.mit.scansite.server.dataaccess.file.Downloader;
import edu.mit.scansite.server.updater.transliterator.GenPeptGenbankFileTransliterator;
import edu.mit.scansite.server.updater.transliterator.ProteinFileTransliterator;
import edu.mit.scansite.server.updater.transliterator.ProteinTransliteratorFileWriter;

/**
 * @author Tobieh
 */
public class GenPeptDbUpdater extends ProteinDbUpdater {
	private String FILE_PREFIX = "complete.";
	private String FILE_SUFFIX = ".protein.gpff.gz";

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

}
