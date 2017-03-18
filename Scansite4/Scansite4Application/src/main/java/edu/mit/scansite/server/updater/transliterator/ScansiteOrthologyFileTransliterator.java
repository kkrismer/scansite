package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Konstantin Krismer
 */
public class ScansiteOrthologyFileTransliterator extends
		OrthologyFileTransliterator {

	public ScansiteOrthologyFileTransliterator(List<BufferedReader> readers,
			OrthologyTransliteratorWriter writer) {
		super(readers, writer);
	}

	@Override
	protected boolean readEntry(BufferedReader reader) throws ScansiteUpdaterException {
		try {
			String line = nextLine(reader);
			if (line == null) {
				return false;
			}
			String[] cells = line
					.split(OrthologyTransliteratorFileWriter.SEPARATOR);
			if (cells.length == OrthologyTransliteratorFileWriter.COLUMN_COUNT) {
				setOrthologsGroupId(parseInteger(cells[0].trim()));
				setOrthologsIdentifier(cells[1].trim());
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

	private int parseInteger(String cell) {
		try {
			return Integer.parseInt(cell);
		} catch (NumberFormatException ex) {
			logger.error("invalid number format: " + cell);
			return -1;
		}
	}
}
