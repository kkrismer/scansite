package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.util.ScansiteAlgorithms;

/**
 * A class for reading a protein database files and saving the information that
 * is important to Scansite.
 * 
 * @author tobieh
 * @author Konstantin Krismer
 */
public abstract class ProteinFileTransliterator extends FileTransliterator {
	public static final String ENTRY_SPACER = "###-@-@-@-@-@-###";
	public static final String SPACER = "\t";
	public static final String PROT_ACCESSION_ANNOTATION = ScansiteConstants.ANNOTATION_ACCESSION;
	public static final String PROT_DESCRIPTION_ANNOTATION = ScansiteConstants.ANNOTATION_DESCRIPTION;
	public static final String PROT_LOCATION_ANNOTATION = ScansiteConstants.ANNOTATION_LOCATION;
	public static final String PROT_TYPE_ANNOTATION = ScansiteConstants.ANNOTATION_TYPE;
	public static final String PROT_KEYWORD_ANNOTATION = ScansiteConstants.ANNOTATION_KEYWORD;
	public static final String PROT_TRANSCRIPT_ANNOTATION = ScansiteConstants.ANNOTATION_TRANSCRIPT;
	public static final String PROT_GENE_ANNOTATION = ScansiteConstants.ANNOTATION_GENE;
	public static final String PROT_OTHER_ANNOTATION = ScansiteConstants.ANNOTATION_OTHER;
	public static final String KEY_PROT_ID = "[ID]";
	public static final String KEY_PROT_ANNOTATION = "[AN]";
	public static final String KEY_PROT_TAXA = "[TX]";
	public static final String KEY_PROT_MW = "[MW]";
	public static final String KEY_PROT_PI = "[PI]";
	public static final String KEY_PROT_SEQUENCE = "[SQ]";

	private ScansiteAlgorithms algs = new ScansiteAlgorithms();
	protected ProteinTransliteratorWriter writer;

	private String proteinId = null;
	private String sequence = null;
	private HashMap<String, Set<String>> annotations = null;
	private Double mw = null;
	private Double pI = null;
	private ArrayList<String> taxa = new ArrayList<String>();
	private String species = null;

	/**
	 * @param reader
	 *            A reader, initialized with the database-file that is going to
	 *            be read.
	 * @param transWriter
	 *            A writer, initialized with the output destination.
	 */
	public ProteinFileTransliterator(List<BufferedReader> readers,
			ProteinTransliteratorWriter writer) {
		this.readers = readers;
		this.writer = writer;
	}

	/**
	 * Reads the input file (reader) and saves the needed information in a given
	 * format to a file (writer). Invalid entries (entries that cannot be parsed
	 * correctly) are saved to another file (errorWriter).
	 * 
	 * @throws ScansiteUpdaterException
	 *             Is thrown if an error occurs, usually, if the input file
	 *             cannot be read.
	 */
	@Override
	public void transliterate() throws ScansiteUpdaterException {
		for (BufferedReader reader : readers) {
			while (readEntry(reader)) {
				if (!isSet(mw)) {
					calcMw();
				}
				if (!isSet(pI)) {
					calcPI();
				}
				if (allSet()) {
					writer.saveEntry(proteinId, annotations, mw, pI, taxa, species, sequence);
				} else {
					writer.saveInvalidEntry(proteinId);
				}
				writer.writeEntrySpacer();
				unsetAll();
			}
		}
		writer.close();
	}

	private void calcPI() {
		if (isSet(sequence)) {
			double pI = algs.calculateIsoelectricPoint(sequence, 0);
			if (pI > 0) {
				setpI(pI);
			}
		}
	}

	private void calcMw() {
		if (isSet(sequence)) {
			double weight = algs.calculateMolecularWeight(sequence, 0);
			if (weight > 0) {
				setMw(weight);
			}
		}
	}

	private void unsetAll() {
		species = null;
		mw = null;
		pI = null;
		sequence = null;
		proteinId = null;
		annotations = null;
		taxa.clear();
	}

	private boolean allSet() {
		return isSet(species) && isSet(mw) && isSet(pI) && isSet(sequence)
				&& isSet(proteinId); // annotations and taxa optional
	}

	protected boolean isSet(Object obj) {
		return obj != null;
	}

	/**
	 * @return TRUE if a whole entry has been read, otherwise FALSE.
	 * @throws ScansiteUpdaterException
	 *             Is thrown if the file cannot be accessed.
	 */
	protected abstract boolean readEntry(BufferedReader reader)
			throws ScansiteUpdaterException;

	protected String getProteinId() {
		return proteinId;
	}

	protected void setProteinId(String proteinId) {
		this.proteinId = proteinId;
	}

	protected String getSequence() {
		return sequence;
	}

	protected void setSequence(String sequence) {
		sequence = sequence.replaceAll("\\d", ""); // get rid of any numbers (if
													// there are any!) -- eg.
													// seq.size counts
		sequence = sequence.replaceAll("\\s", ""); // get rid of whitespace
		this.sequence = sequence.toUpperCase();
	}

	protected HashMap<String, Set<String>> getAnnotations() {
		if (annotations == null) {
			annotations = new HashMap<String, Set<String>>();
		}
		return annotations;
	}

	protected void addAnnotation(String key, String value) {
		HashMap<String, Set<String>> annotations = getAnnotations();
		Set<String> entries = annotations.get(key);
		if (entries == null) {
			entries = new HashSet<String>();
		}
		entries.add(value);
		annotations.put(key, entries);
		setAnnotations(annotations);
	}

	protected void setAnnotations(HashMap<String, Set<String>> annotations) {
		this.annotations = annotations;
	}

	protected Double getMw() {
		return mw;
	}

	protected void setMw(Double mw) {
		this.mw = mw;
	}

	protected Double getpI() {
		return pI;
	}

	protected String getSpecies() {
		return species;
	}

	protected void setSpecies(String species) {
		this.species = species;
	}

	protected void setpI(Double pI) {
		this.pI = pI;
	}

	protected ArrayList<String> getTaxa() {
		return taxa;
	}

	/**
	 * @param taxa
	 *            The taxa have to be sorted from the most general (0) to the
	 *            most specific (taxa.size() - 1).
	 */
	protected void setTaxa(ArrayList<String> taxa) {
		this.taxa = taxa;
	}

	protected String nextLine(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		return line;
	}

	protected String[] splitByWhitespaces(String str) {
		return str.split("\\s+");
	}
}
