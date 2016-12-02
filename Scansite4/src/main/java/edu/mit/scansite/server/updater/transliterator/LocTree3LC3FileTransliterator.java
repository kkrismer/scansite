package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Konstantin Krismer
 */
public class LocTree3LC3FileTransliterator extends
		LocalizationFileTransliterator {
	public static final String LEVEL0_SEPARATOR = "\t";
	public static final String LEVEL1_SEPARATOR = ";";
	public static final int COLUMN_COUNT = 4;

	public LocTree3LC3FileTransliterator(List<BufferedReader> readers,
			LocalizationTransliteratorWriter writer) {
		super(readers, writer);
	}

	@Override
	protected boolean readEntry(BufferedReader reader) {
		try {
			String line = nextLine(reader);
			if (line == null) {
				return false;
			}
			while (line.startsWith("#")) {
				line = nextLine(reader);
				if (line == null) {
					return false;
				}
			}
			String[] cells = line.split(LEVEL0_SEPARATOR);
			if (cells.length == COLUMN_COUNT) {
				setProteinIdentifier(parseIdentifier(cells[0]));
				setScore(parseInteger(cells[1]));
				setLocalization(cells[2].trim());
				setGoTerms(parseGoTerms(cells[3]));
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

	private List<String> parseGoTerms(String cell) {
		String[] subcells = cell.split(LEVEL1_SEPARATOR);
		if (subcells.length == 0) {
			return null;
		} else {
			List<String> goTerms = new LinkedList<>();
			Pattern pattern = Pattern.compile("(.+)(GO:\\d{7})");
			Pattern patternEvidence = Pattern
					.compile("(.+)(GO:\\d{7})\\((\\w{1,3})\\)");
			Matcher matcher;
			// cell leading edge GO:0031252(IEA)
			for (String goTerm : subcells) {
				if (goTerm.contains("(")) {
					matcher = patternEvidence.matcher(goTerm);
					if (matcher.find()) {
						goTerms.add(matcher.group(2).trim()
								+ LocalizationTransliteratorFileWriter.LEVEL2_SEPARATOR
								+ matcher.group(1).trim()
								+ LocalizationTransliteratorFileWriter.LEVEL2_SEPARATOR
								+ matcher.group(3).trim());
					} else {
						logger.error("invalid GO term format: " + goTerm);
					}
				} else {
					matcher = pattern.matcher(goTerm);
					if (matcher.find()) {
						goTerms.add(matcher.group(2).trim()
								+ LocalizationTransliteratorFileWriter.LEVEL2_SEPARATOR
								+ matcher.group(1).trim()
								+ LocalizationTransliteratorFileWriter.LEVEL2_SEPARATOR);
					} else {
						logger.error("invalid GO term format: " + goTerm);
					}
				}
			}
			return goTerms;
		}
	}

	private String parseIdentifier(String cell) {
		String[] subcells = cell.split("\\|");
		if (subcells.length != 3) {
			logger.error("protein identifier format invalid");
			return null;
		}
		return subcells[2].trim();
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
