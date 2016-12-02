package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.mit.scansite.shared.util.Formatter;

/**
 * Parses UniProt-specific plain-text dat-files.
 * 
 * @author tobieh
 */
public class UniProtDatFileTransliterator extends ProteinFileTransliterator {
	private static final String KEY_ID = "ID "; // ID / accession number
	private static final String KEY_AC = "AC "; // additional accession numbers
												// --- saved as annotation
	private static final String KEY_OS = "OS "; // organism spp. --- saved as
												// taxon
	private static final String KEY_OC = "OC "; // organism classes --- saved as
												// taxon
	private static final String KEY_SQ = "SQ "; // SEQUENCE
	private static final String KEY_DE = "DE "; // description
	private static final String KEY_KW = "KW "; // keywords
	private static final String ENTRY_SPACER = "//"; // Spacer between entries
	private static final String SPACER = ";"; // Spacer used within entries

	private boolean nextRead = false;

	public UniProtDatFileTransliterator(List<BufferedReader> readers,
			ProteinTransliteratorWriter writer) {
		super(readers, writer);
	}

	@Override
	protected boolean readEntry(BufferedReader reader) {
		try {
			String line = nextLine(reader);
			nextRead = false;
			while (line != null && !line.startsWith(ENTRY_SPACER)) {
				if (line.startsWith(KEY_ID)) {
					parseId(line);
				} else if (line.startsWith(KEY_AC)) {
					parseAccessionAnnotations(line);
				} else if (line.startsWith(KEY_KW)) {
					parseKeywordAnnotations(line);
				} else if (line.startsWith(KEY_DE)) {
					line = parseDescriptionAnnotations(reader, line);
				} else if (line.startsWith(KEY_OS)) {
					parseSpecies(line);
				} else if (line.startsWith(KEY_OC)) {
					parseClassification(line);
				} else if (line.startsWith(KEY_SQ)) {
					line = parseSequence(reader, line);
					nextRead = true;
				}
				if (!nextRead) {
					line = nextLine(reader);
				} else {
					nextRead = false;
				}
			}
			return line != null;
		} catch (IOException e) {
			return false;
		}
	}

	private String parseDescriptionAnnotations(BufferedReader reader,
			String line) throws IOException {
		String desc = "";
		while (line != null && line.startsWith(KEY_DE)) {
			desc += " ";
			line = line.substring(KEY_DE.length(),
					((line.endsWith(".")) ? line.length() - 1 : line.length()));
			if (line != null) {
				desc += line.trim();
			}
			line = nextLine(reader);
		}
		addAnnotation(PROT_DESCRIPTION_ANNOTATION, desc.trim());
		nextRead = true;
		return line;
	}

	private void parseKeywordAnnotations(String line) {
		line = line.substring(KEY_KW.length(),
				((line.endsWith(".")) ? line.length() - 1 : line.length()));
		if (line != null) {
			String[] vals = line.split(";");
			if (vals.length > 0) {
				for (String val : vals) {
					if (val != null) {
						addAnnotation(PROT_KEYWORD_ANNOTATION, val.trim());
					}
				}
			}
		}
	}

	private void parseClassification(String line) {
		ArrayList<String> taxa = getTaxa();
		if (taxa == null) {
			taxa = new ArrayList<String>();
		}
		// get rid of the lines start and end (if necessary)
		line = line.substring(KEY_OC.length(),
				((line.endsWith(".")) ? line.length() - 1 : line.length()));
		String lineArr[] = line.split(SPACER); // split line by ;
		if (lineArr != null) {
			for (int i = 0; i < lineArr.length; ++i) {
				taxa.add(lineArr[i].trim());
			}
		}
		setTaxa(taxa);
	}

	private void parseSpecies(String line) {
		String species = getSpecies();
		if (species == null) {
			species = "";
		} else {
			species += " "; // add space, if there is already a part of the name
		}
		// get rid of the lines start and end (if necessary)
		line = line.substring(KEY_OS.length(),
				((line.endsWith(".")) ? line.length() - 1 : line.length()));
		species += line.trim(); // trim whitespace
		setSpecies(species);
	}

	private String parseSequence(BufferedReader reader, String line)
			throws IOException {
		Formatter formatter = new Formatter();
		String sequence = "";
		line = nextLine(reader); // ignore first line -> no seq there!
		while (line != null && (line.startsWith(" ") || line.startsWith("\t"))) {
			sequence += formatter.trimWhitespace(line);
			line = nextLine(reader);
		}
		if (!sequence.isEmpty()) {
			sequence.replaceAll("\\d", ""); // get rid of any numbers (if there
											// are any!) -- eg. seq.size counts
			setSequence(sequence);
		}
		return line;
	}

	private void parseAccessionAnnotations(String line) {
		HashMap<String, Set<String>> annotations = getAnnotations();
		line = line.replaceAll(SPACER, " "); // get rid of ';'
		String[] lineArr = splitByWhitespaces(line);
		if (lineArr != null) {
			for (int i = 1; i < lineArr.length; ++i) { // ignore line key (first
														// entry)
				addAnnotation(
						ProteinFileTransliterator.PROT_ACCESSION_ANNOTATION,
						lineArr[i]);
			}
		}
		setAnnotations(annotations);
	}

	private void parseId(String line) {
		String[] lineArr = splitByWhitespaces(line);
		if (lineArr != null && lineArr.length >= 2) { // first entry is line
														// key, second is the id
														// we want
			setProteinId(lineArr[1]);
		}
	}
}
