package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Konstantin Krismer
 */
public abstract class LocalizationFileTransliterator extends FileTransliterator {
	protected LocalizationTransliteratorWriter writer;

	private String proteinIdentifier = null;
	private int score = -1;
	private String localization = null;
	private List<String> goTerms = null;

	private String currentLine = null;

	/**
	 * @param reader
	 *            A reader, initialized with the database-file that is going to
	 *            be read.
	 * @param transWriter
	 *            A writer, initialized with the output destination.
	 */
	public LocalizationFileTransliterator(List<BufferedReader> readers,
			LocalizationTransliteratorWriter writer) {
		this.readers = readers;
		this.writer = writer;
	}

	/**
	 * Reads the input file (reader) and saves the needed information in a given
	 * format to a file (writer). Invalid entries (entries that cannot be parsed
	 * correctly) are saved to another file (errorWriter).
	 * 
	 * @throws ScansiteUpdaterException
	 *             Is thrown if an error occurs, usually, if the input file
	 *             cannot be read.
	 */
	@Override
	public void transliterate() throws ScansiteUpdaterException {
		for (BufferedReader reader : readers) {
			while (readEntry(reader)) {
				if (allSet()) {
					writer.saveEntry(proteinIdentifier, score, localization,
							goTerms);
				} else {
					writer.saveInvalidEntry(currentLine);
				}
				unsetAll();
			}
		}
		writer.close();
	}

	private void unsetAll() {
		proteinIdentifier = null;
		score = -1;
		localization = null;
		goTerms = null;
		currentLine = null;
	}

	private boolean allSet() { // goTerms are optional
		return isSet(proteinIdentifier) && isSet(score) && isSet(localization);
	}

	protected boolean isSet(int value) {
		return value != -1;
	}

	protected boolean isSet(Object value) {
		return value != null;
	}

	/**
	 * @return TRUE if a whole entry has been read, otherwise FALSE.
	 * @throws ScansiteUpdaterException
	 *             Is thrown if the file cannot be accessed.
	 */
	protected abstract boolean readEntry(BufferedReader reader)
			throws ScansiteUpdaterException;

	protected String getProteinIdentifier() {
		return proteinIdentifier;
	}

	protected void setProteinIdentifier(String proteinIdentifier) {
		this.proteinIdentifier = proteinIdentifier;
	}

	protected int getScore() {
		return score;
	}

	protected void setScore(int score) {
		this.score = score;
	}

	protected String getLocalization() {
		return localization;
	}

	protected void setLocalization(String localization) {
		this.localization = localization;
	}

	protected List<String> getGoTerms() {
		return goTerms;
	}

	protected void setGoTerms(List<String> goTerms) {
		this.goTerms = goTerms;
	}

	protected String nextLine(BufferedReader reader) throws IOException {
		currentLine = reader.readLine();
		return currentLine;
	}

	protected String[] splitByWhitespaces(String str) {
		return str.split("\\s+");
	}
}