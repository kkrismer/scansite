package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Tobieh
 */
public class ScansiteProteinFileTransliterator extends
		ProteinFileTransliterator {

	public ScansiteProteinFileTransliterator(List<BufferedReader> readers,
			ProteinTransliteratorWriter writer) {
		super(readers, writer);
	}

	@Override
	protected boolean readEntry(BufferedReader reader)
			throws ScansiteUpdaterException {
		try {
			String line = nextLine(reader);
			while (line != null && !line.startsWith(ENTRY_SPACER)) {
				if (line.startsWith(ProteinFileTransliterator.KEY_PROT_ID)) {
					parseId(line);
				} else if (line.startsWith(KEY_PROT_ANNOTATION)) {
					parseAccessionAnnotations(line);
				} else if (line.startsWith(KEY_PROT_MW)) {
					parseMolWeight(line);
				} else if (line.startsWith(KEY_PROT_PI)) {
					parsePI(line);
				} else if (line.startsWith(KEY_PROT_TAXA)) {
					parseClassification(line);
				} else if (line.startsWith(KEY_PROT_SEQUENCE)) {
					parseSequence(line);
				}
				line = nextLine(reader);
			}
			return line != null;
		} catch (IOException e) {
			return false;
		}
	}

	private void parseSequence(String line) {
		line = line.substring(ProteinFileTransliterator.KEY_PROT_SEQUENCE
				.length());
		setSequence(line.trim());
	}

	/**
	 * saves the taxa from the most general (0) to the most specific taxon
	 * (taxa.size() - 2). The last taxon, since it is the most specific one, is
	 * saved as species.
	 * 
	 * @param line
	 *            input line from ss-file.
	 */
	private void parseClassification(String line) {
		ArrayList<String> taxa = getTaxa();
		String species = "";
		if (taxa == null) {
			taxa = new ArrayList<String>();
		}
		line = line.substring(ProteinFileTransliterator.KEY_PROT_TAXA.length());
		String lineArr[] = line.trim().split(ProteinFileTransliterator.SPACER);
		if (lineArr != null) {
			for (int i = 0; i < lineArr.length; ++i) {
				taxa.add(lineArr[i]);
			}
			species = taxa.remove(taxa.size() - 1);
		}
		setTaxa(taxa);
		setSpecies(species);
	}

	private void parseAccessionAnnotations(String line) {
		HashMap<String, Set<String>> annotations = getAnnotations();
		if (annotations == null) {
			annotations = new HashMap<String, Set<String>>();
		}
		line = line.substring(ProteinFileTransliterator.KEY_PROT_TAXA.length());
		String lineArr[] = line.trim().split(ProteinFileTransliterator.SPACER);
		if (lineArr.length == 2) {
			Set<String> entries = annotations.get(lineArr[0]);
			if (entries == null) {
				entries = new HashSet<String>();
			}
			entries.add(lineArr[1]);
			annotations.put(lineArr[0], entries);
		}
		setAnnotations(annotations);
	}

	private void parsePI(String line) {
		line = line.substring(ProteinFileTransliterator.KEY_PROT_PI.length())
				.trim();
		setpI(Double.valueOf(line.trim()));
	}

	private void parseMolWeight(String line) {
		line = line.substring(ProteinFileTransliterator.KEY_PROT_MW.length())
				.trim();
		setMw(Double.valueOf(line.trim()));
	}

	private void parseId(String line) {
		line = line.substring(ProteinFileTransliterator.KEY_PROT_ID.length())
				.trim();
		setProteinId(line.trim());
	}
}
