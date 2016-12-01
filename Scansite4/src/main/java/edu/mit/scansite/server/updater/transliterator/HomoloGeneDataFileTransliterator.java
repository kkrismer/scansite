package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * @author Konstantin Krismer
 */
public class HomoloGeneDataFileTransliterator extends
		OrthologyFileTransliterator {
	public static final String SEPARATOR = "\t";
	public static final int COLUMN_COUNT = 6;

	public HomoloGeneDataFileTransliterator(List<BufferedReader> readers,
			OrthologyTransliteratorWriter writer) {
		super(readers, writer);
	}

	@Override
	protected boolean readEntry(BufferedReader reader) {
		try {
			String line = nextLine(reader);
			if (line == null) {
				return false;
			}
			String[] cells = line.split(SEPARATOR);
			if (cells.length == COLUMN_COUNT) {
				setOrthologsGroupId(parseInteger(cells[0]));
				setOrthologsIdentifier(parseIdentifier(cells[5]));
				return true;
			} else {
				logger.error("invalid line format: " + line);
				return false;
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	private String parseIdentifier(String cell) {
		return cell.substring(0, cell.indexOf('.')).trim();
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
