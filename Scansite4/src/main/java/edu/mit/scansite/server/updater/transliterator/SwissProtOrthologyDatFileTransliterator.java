package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Konstantin Krismer
 */
public class SwissProtOrthologyDatFileTransliterator extends
		OrthologyFileTransliterator {
	private static final String KEY_ID = "ID "; // identifier / accession number
	private static final String ENTRY_SPACER = "//"; // spacer between entries
	private static final String IDENTIFIER_ORG_SPACER = "_";

	private Map<String, Integer> identifierToGroupIdLUT;

	public SwissProtOrthologyDatFileTransliterator(
			List<BufferedReader> readers, OrthologyTransliteratorWriter writer) {
		super(readers, writer);
		identifierToGroupIdLUT = new HashMap<>();
	}

	@Override
	protected boolean readEntry(BufferedReader reader) {
		try {
			String line = nextLine(reader);
			while (line != null && !line.startsWith(ENTRY_SPACER)) {
				if (line.startsWith(KEY_ID)) {
					String identifier = parseIdentifier(line);
					if (identifier != null && !identifier.isEmpty()) {
						setOrthologsIdentifier(identifier);
						setOrthologsGroupId(convertIdentifierToGroupId(identifier));
					} else {
						logger.error("error occurred during identifier parsing: "
								+ line);
					}
				}
				line = nextLine(reader);
			}
			return line != null;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	private int convertIdentifierToGroupId(String identifier) {
		if (identifier != null && !identifier.isEmpty()) {
			String groupIdentifier = identifier.substring(0,
					identifier.indexOf(IDENTIFIER_ORG_SPACER));
			if (!identifierToGroupIdLUT.containsKey(groupIdentifier)) {
				identifierToGroupIdLUT.put(groupIdentifier,
						identifierToGroupIdLUT.size() + 1);
			}
			return identifierToGroupIdLUT.get(groupIdentifier);
		} else {
			logger.error("error during identifier to group id conversion");
			return -1;
		}
	}

	private String parseIdentifier(String line) {
		String[] cells = splitByWhitespaces(line);
		if (cells != null && cells.length >= 2) {
			return cells[1];
		}
		return null;
	}
}
