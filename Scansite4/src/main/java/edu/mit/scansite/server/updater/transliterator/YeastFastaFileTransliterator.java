package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Tobieh
 */
public class YeastFastaFileTransliterator extends ProteinFileTransliterator {
	private static final String FASTA_ENTRY_START = ">";
	private static final String COMMA = ",";
	private static final String QUOTE = "\"";
	private static final String LOCATION_START = "Chr ";
	private static final String LOCATION_ADDITION = "reverse ";
	private static final String TYPE_MATCH = "ORF";
	private static final String SGDID = "SGDID:";
	private String SPECIES = "";

	private String line = null;

	public YeastFastaFileTransliterator(List<BufferedReader> readers,
			ProteinTransliteratorWriter writer, String species) {
		super(readers, writer);
		this.SPECIES = species;
	}

	@Override
	protected boolean readEntry(BufferedReader reader)
			throws ScansiteUpdaterException {
		try {
			if (line == null) {
				line = nextLine(reader);
			}
			boolean sequenceRead = false;
			while (line != null && !sequenceRead) {
				if (line.startsWith(FASTA_ENTRY_START)) {
					line = line.substring(FASTA_ENTRY_START.length()).trim();
					parseHeaderLine(line);
					line = nextLine(reader);
				} else {
					line = parseSequence(reader, line);
					sequenceRead = true;
				}
			}
			return line != null;
		} catch (IOException e) {
			return false;
		}
	}

	private String parseSequence(BufferedReader reader, String line)
			throws IOException {
		StringBuilder seq = new StringBuilder();
		seq.append(line.trim());
		line = nextLine(reader);
		while (line != null && !line.startsWith(FASTA_ENTRY_START)) {
			seq.append(line.trim());
			line = nextLine(reader);
		}

		String sequence = seq.toString();
		if (sequence.endsWith("*")) {
			sequence = sequence.substring(0, sequence.length() - 1);
		}
		setSequence(sequence.toUpperCase());
		return line;
	}

	private void parseHeaderLine(String line) {
		setSpecies(SPECIES);
		// setTaxa() not needed here since no taxonomy is given

		// parse accession numbers
		String accs = line.substring(0, line.indexOf(COMMA)).trim();
		String[] lineArr = splitByWhitespaces(accs);
		if (lineArr != null) {
			setProteinId(lineArr[0]);
			for (int i = 1; i < lineArr.length; ++i) {
				addAnnotation(
						PROT_ACCESSION_ANNOTATION,
						(lineArr[i].startsWith(SGDID)) ? lineArr[i]
								.substring(SGDID.length()) : lineArr[i]);
			}
		}

		// parse other annotations: quoted description
		line = line.substring(line.indexOf(COMMA) + 1).trim();
		if (line.contains(QUOTE)) {
			String desc = line.substring(line.indexOf(QUOTE) + 1,
					line.lastIndexOf(QUOTE)).trim();
			if (desc != null && !desc.isEmpty()) {
				addAnnotation(PROT_DESCRIPTION_ANNOTATION, desc);
			}
			line = line.substring(0, line.indexOf(QUOTE)).trim();
		}

		// parse other annotations: location and type
		lineArr = line.split(COMMA);
		if (lineArr != null) {
			for (int i = 0; i < lineArr.length; ++i) {
				lineArr[i] = lineArr[i].trim();
				if (!lineArr[i].isEmpty()) {
					if (lineArr[i].startsWith(LOCATION_START)) {
						String toAdd = lineArr[i];
						while (lineArr[i + 1].matches("^\\d.*\\d")) {
							toAdd += COMMA + lineArr[++i];
						}
						if (lineArr[i + 1].contains(LOCATION_ADDITION)) {
							toAdd += COMMA + lineArr[++i];
						}
						addAnnotation(PROT_LOCATION_ANNOTATION, toAdd);
					} else if (lineArr[i].contains(TYPE_MATCH)) {
						addAnnotation(PROT_TYPE_ANNOTATION, lineArr[i]);
					}
				}
			}
		}
	}
}
