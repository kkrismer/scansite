package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * An ABC for reading orthology database files and saving the relevant
 * information to Scansite.
 * 
 * @author Konstantin Krismer
 */
public abstract class OrthologyFileTransliterator extends FileTransliterator {

	protected OrthologyTransliteratorWriter writer;

	private int orthologsGroupId = -1;
	private String orthologsIdentifier = null;

	private String currentLine = null;

	/**
	 * @param reader
	 *            A reader, initialized with the database-file that is going to
	 *            be read.
	 * @param transWriter
	 *            A writer, initialized with the output destination.
	 */
	public OrthologyFileTransliterator(List<BufferedReader> readers,
			OrthologyTransliteratorWriter writer) {
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
		for(BufferedReader reader : readers) {
			while (readEntry(reader)) {
				if (allSet()) {
					writer.saveEntry(orthologsGroupId, orthologsIdentifier);
				} else {
					writer.saveInvalidEntry(currentLine);
				}
				unsetAll();
			}
		}
		writer.close();
	}

	private void unsetAll() {
		orthologsGroupId = -1;
		orthologsIdentifier = null;
		currentLine = null;
	}

	private boolean allSet() {
		return isSet(orthologsGroupId) && isSet(orthologsIdentifier); // orthologsTaxonId
																		// optional
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
	protected abstract boolean readEntry(BufferedReader reader) throws ScansiteUpdaterException;

	protected int getOrthologsGroupId() {
		return orthologsGroupId;
	}

	protected void setOrthologsGroupId(int orthologsGroupId) {
		this.orthologsGroupId = orthologsGroupId;
	}

	protected String getOrthologsIdentifier() {
		return orthologsIdentifier;
	}

	protected void setOrthologsIdentifier(String orthologsIdentifier) {
		this.orthologsIdentifier = orthologsIdentifier;
	}

	protected String nextLine(BufferedReader reader) throws IOException {
		currentLine = reader.readLine();
		return currentLine;
	}

	protected String[] splitByWhitespaces(String str) {
		return str.split("\\s+");
	}
}
