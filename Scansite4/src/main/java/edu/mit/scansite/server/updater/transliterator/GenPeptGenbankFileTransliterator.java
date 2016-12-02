package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;
import edu.mit.scansite.shared.util.Formatter;

/**
 * @author Tobieh
 */
public class GenPeptGenbankFileTransliterator extends ProteinFileTransliterator {
	private static final String ENTRY_SPACER = "//"; // Spacer between entries
	private static final String SPACER = ";"; // Spacer used within entries
	private static final String END_OF_ENTRY = "."; // Char used to show that an
													// entry ends
	private static final String KEY_ACCESSION = "ACCESSION"; // list of protein
																// primary
																// accession
																// numbers,
																// first one is
																// current one
	private static final String KEY_VERSION = "VERSION";
	private static final String KEY_DEFINITION = "DEFINITION"; // description
																// annotation
	private static final String KEY_ORGANISM = "  ORGANISM"; // species tree
																// (multi-line;
																// first line
																// species, then
																// general->specific)
	private static final String KEY_ORIGIN = "ORIGIN"; // multi-line / contains
														// numbers

	private boolean multiLineRead = false;

	public GenPeptGenbankFileTransliterator(List<BufferedReader> readers,
			ProteinTransliteratorWriter writer) {
		super(readers, writer);
	}

	@Override
	protected boolean readEntry(BufferedReader reader)
			throws ScansiteUpdaterException {
		try {
			String line = nextLine(reader);
			while (line != null && !line.startsWith(ENTRY_SPACER)) {
				if (line.startsWith(KEY_ACCESSION)) {
					line = line.substring(KEY_ACCESSION.length()).trim();
					parseId(reader, line);
				} else if (line.startsWith(KEY_DEFINITION)) {
					line = line.substring(KEY_DEFINITION.length()).trim();
					line = parseDefinitionAnnotation(reader, line);
				} else if (line.startsWith(KEY_ORGANISM)) {
					line = line.substring(KEY_ORGANISM.length()).trim();
					line = parseSpeciesTree(reader, line);
				} else if (line.startsWith(KEY_ORIGIN)) {
					line = parseSequence(reader, line);
					multiLineRead = true;
				}
				if (!multiLineRead) {
					line = nextLine(reader);
				} else {
					multiLineRead = false;
				}
			}
			return line != null;
		} catch (IOException e) {
			return false;
		}
	}

	private String parseSequence(BufferedReader reader, String line)
			throws IOException {
		Formatter formatter = new Formatter();
		String sequence = "";
		line = nextLine(reader); // first sequence line does not contain any
									// sequence parts
		while (line != null && !line.startsWith(ENTRY_SPACER)) {
			sequence += formatter.trimWhitespace(line);
			line = nextLine(reader);
		}
		if (!sequence.isEmpty()) {
			setSequence(sequence);
		}
		return line;
	}

	private String parseSpeciesTree(BufferedReader reader, String line)
			throws IOException {
		setSpecies(line);
		line = nextLine(reader);
		String tree = "";
		while (line != null && !line.endsWith(END_OF_ENTRY)) {
			tree += line.trim();
			line = nextLine(reader);
		}
		tree += line.substring(0, line.length() - 1).trim(); // (line ending
																// with
																// END_OF_ENTRY)
		String lineArr[] = tree.split(SPACER);
		if (lineArr != null) {
			ArrayList<String> taxa = getTaxa();
			if (taxa == null) {
				taxa = new ArrayList<String>();
			}
			for (int i = 0; i < lineArr.length; ++i) {
				taxa.add(lineArr[i].trim());
			}
			setTaxa(taxa);
		}
		return line;
	}

	private String parseDefinitionAnnotation(BufferedReader reader, String line)
			throws IOException {
		String def = "";
		while (line != null && !line.endsWith(END_OF_ENTRY)) {
			def += " " + line.trim();
			line = nextLine(reader);
		}
		if (line.endsWith(END_OF_ENTRY)) {
			def += " " + line.substring(0, line.length() - 1).trim(); // (line
																		// ending
																		// with
																		// END_OF_ENTRY)
		}
		addAnnotation(PROT_DESCRIPTION_ANNOTATION, def.trim());
		return line;
	}

	private void parseId(BufferedReader reader, String line) throws IOException {
		if (line.contains(" ")) {
			String accessionLine = line;
			line = nextLine(reader);
			while (line != null && !line.startsWith(KEY_VERSION)) {
				accessionLine += " " + line.trim();
				line = nextLine(reader);
			}
			multiLineRead = true;
			String[] accs = accessionLine.split("\\s");
			setProteinId(accs[0]);
			for (int i = 1; i < accs.length; ++i) {
				String acc = accs[i];
				if (acc != null) {
					addAnnotation(PROT_ACCESSION_ANNOTATION, acc);
				}
			}
		} else {
			setProteinId(line);
		}
	}

}
