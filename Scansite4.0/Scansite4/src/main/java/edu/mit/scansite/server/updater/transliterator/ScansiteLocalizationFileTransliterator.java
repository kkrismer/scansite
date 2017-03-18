package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Konstantin Krismer
 */
public class ScansiteLocalizationFileTransliterator extends
		LocalizationFileTransliterator {

	public ScansiteLocalizationFileTransliterator(List<BufferedReader> readers,
			LocalizationTransliteratorWriter writer) {
		super(readers, writer);
	}

	@Override
	protected boolean readEntry(BufferedReader reader)
			throws ScansiteUpdaterException {
		try {
			String line = nextLine(reader);
			if (line == null) {
				return false;
			}
			String[] cells = line
					.split(LocalizationTransliteratorFileWriter.LEVEL0_SEPARATOR);
			if (cells.length == LocalizationTransliteratorFileWriter.COLUMN_COUNT) {
				setProteinIdentifier(cells[0].trim());
				setScore(parseInteger(cells[1]));
				setLocalization(cells[2].trim());
				setGoTerms(parseGoTerms(cells[3]));
				return true;
			} else {
				logger.error("invalid line format: " + line);
				return false;
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	private List<String> parseGoTerms(String cell) {
		String[] subcells = cell
				.split(LocalizationTransliteratorFileWriter.LEVEL1_SEPARATOR);
		if (subcells.length == 0) {
			return null;
		} else {
			return Arrays.asList(subcells);
		}
	}

	private int parseInteger(String cell) {
		try {
			return Integer.parseInt(cell);
		} catch (NumberFormatException ex) {
			logger.error("invalid number format: " + cell);
			return -1;
		}
	}
}
