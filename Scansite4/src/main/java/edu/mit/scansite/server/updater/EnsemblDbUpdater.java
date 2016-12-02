package edu.mit.scansite.server.updater;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import edu.mit.scansite.server.dataaccess.file.Downloader;
import edu.mit.scansite.server.updater.transliterator.EnsemblFastaFileTransliterator;
import edu.mit.scansite.server.updater.transliterator.FileTransliterator;
import edu.mit.scansite.server.updater.transliterator.ProteinTransliteratorFileWriter;

/**
 * @author Tobieh
 */
public class EnsemblDbUpdater extends ProteinDbUpdater {

	private static final String ENSEMBLE_FILE_SUFFIX = ".pep.all.fa.gz";

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
			ProteinTransliteratorFileWriter writer = new ProteinTransliteratorFileWriter(
					tempFileWriter, errorWriter);
			EnsemblFastaFileTransliterator transliterator = new EnsemblFastaFileTransliterator(
					getReaders(), writer, getDatabase().getOrganismName());
			return transliterator;
		} catch (IOException e) {
			throw new ScansiteUpdaterException(
					"Error accessing temporary files!", e);
		}
	}

	@Override
	protected List<URL> prepareDownloadUrls(List<URL> from)
			throws ScansiteUpdaterException {
		Downloader downloader = new Downloader();
		URL url = from.get(0);
		from.remove(0);
		String s = downloader.downloadString(url);
		String lines[] = s.trim().split("\n");
		boolean fileFound = false;
		for (String line : lines) {
			if (line.endsWith(ENSEMBLE_FILE_SUFFIX) && !fileFound) {
				line = line.substring(line.lastIndexOf(" ") + 1);
				try {
					from.add(new URL(url.toString() + line));
				} catch (MalformedURLException e) {
					throw new ScansiteUpdaterException(
							"Ensembl file can not be found on server :(");
				}
				fileFound = true;
			}
		}
		if (!fileFound) {
			throw new ScansiteUpdaterException(
					"Ensembl file can not be found on server :(");
		}
		return from;
	}

}
