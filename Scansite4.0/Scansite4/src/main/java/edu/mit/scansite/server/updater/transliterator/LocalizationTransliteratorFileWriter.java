package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Konstantin Krismer
 */
public class LocalizationTransliteratorFileWriter implements
		LocalizationTransliteratorWriter {
	public static final String LEVEL0_SEPARATOR = "\t";
	public static final String LEVEL1_SEPARATOR = ";";
	public static final String LEVEL2_SEPARATOR_REGEX = "\\|";
	public static final String LEVEL2_SEPARATOR = "|";
	public static final int COLUMN_COUNT = 4;

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private BufferedWriter writer;
	private BufferedWriter errorWriter;

	public LocalizationTransliteratorFileWriter(BufferedWriter writer,
			BufferedWriter errorWriter) {
		this.writer = writer;
		this.errorWriter = errorWriter;
	}

	@Override
	public void saveInvalidEntry(String line) throws ScansiteUpdaterException {
		writeError(line + "\n");
	}

	@Override
	public void saveEntry(String proteinIdentifier, int score,
			String localization, List<String> goTerms)
			throws ScansiteUpdaterException {
		write(proteinIdentifier);
		writeLevel0Separator();
		write(score);
		writeLevel0Separator();
		write(localization);
		writeLevel0Separator();
		boolean isFirst = true;
		for (String goTerm : goTerms) {
			if (!isFirst) {
				writeLevel1Separator();
			}
			write(goTerm);
			isFirst = false;
		}
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

	private void writeLevel0Separator() throws ScansiteUpdaterException {
		write(LEVEL0_SEPARATOR);
	}

	private void writeLevel1Separator() throws ScansiteUpdaterException {
		write(LEVEL1_SEPARATOR);
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
