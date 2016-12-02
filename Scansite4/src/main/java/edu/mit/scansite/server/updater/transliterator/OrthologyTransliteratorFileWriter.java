package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Konstantin Krismer
 */
public class OrthologyTransliteratorFileWriter implements
		OrthologyTransliteratorWriter {
	public static final String SEPARATOR = "\t";
	public static final int COLUMN_COUNT = 2;
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private BufferedWriter writer;
	private BufferedWriter errorWriter;

	public OrthologyTransliteratorFileWriter(BufferedWriter writer,
			BufferedWriter errorWriter) {
		this.writer = writer;
		this.errorWriter = errorWriter;
	}

	@Override
	public void saveInvalidEntry(String line) throws ScansiteUpdaterException {
		writeError(line + "\n");

	}

	@Override
	public void saveEntry(int orthologsGroupId, String orthologsIdentifier)
			throws ScansiteUpdaterException {
		write(orthologsGroupId);
		writeSeparator();
		write(orthologsIdentifier);
		writeNewLine();
	}

	@Override
	public void close() throws ScansiteUpdaterException {
		try {
			writer.close();
			errorWriter.close();
		} catch (IOException e) {
			logger.error("Error closing temporary files", e);
			throw new ScansiteUpdaterException("Error closing temporary files",
					e);
		}
	}

	private void writeNewLine() throws ScansiteUpdaterException {
		write("\n");
	}

	private void writeSeparator() throws ScansiteUpdaterException {
		write(SEPARATOR);
	}

	private void write(int value) throws ScansiteUpdaterException {
		write(Integer.toString(value));
	}

	private void write(String value) throws ScansiteUpdaterException {
		try {
			writer.write(value);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ScansiteUpdaterException(e);
		}
	}

	private void writeError(String line) throws ScansiteUpdaterException {
		try {
			errorWriter.write(line);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ScansiteUpdaterException(e);
		}
	}
}
