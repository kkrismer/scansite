package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;
import edu.mit.scansite.shared.util.Formatter;

/**
 * @author Tobieh
 */
public class ProteinTransliteratorFileWriter implements ProteinTransliteratorWriter {

	private static final String ERROR_ACCESSING_FILE = "Error accessing file!";

	private Formatter formatter = new Formatter();
	private BufferedWriter writer;
	private BufferedWriter errorWriter;

	public ProteinTransliteratorFileWriter(BufferedWriter writer,
			BufferedWriter errorWriter) {
		this.writer = writer;
		this.errorWriter = errorWriter;
	}

	@Override
	public void writeEntrySpacer() throws ScansiteUpdaterException {
		write(ProteinFileTransliterator.ENTRY_SPACER);
		writeNewline();
	}

	@Override
	public void saveInvalidEntry(String proteinId)
			throws ScansiteUpdaterException {
		writeError(proteinId + "\n");
	}

	@Override
	public void saveEntry(String proteinId,
			HashMap<String, Set<String>> annotations, Double mw, Double pI,
			ArrayList<String> taxa, String species, String sequence)
			throws ScansiteUpdaterException {
		writeId(proteinId);
		writeTaxa(taxa, species);
		writeMw(mw);
		writePI(pI);
		writeAnnotations(annotations);
		writeSequence(sequence);
	}

	private void writeSequence(String sequence) throws ScansiteUpdaterException {
		writeKey(ProteinFileTransliterator.KEY_PROT_SEQUENCE);
		write(formatter.formatSequence(sequence));
		writeNewline();
	}

	private void writeKey(String key) throws ScansiteUpdaterException {
		write(key + ProteinFileTransliterator.SPACER);
	}

	private void writeTaxa(ArrayList<String> taxa, String species)
			throws ScansiteUpdaterException {
		writeKey(ProteinFileTransliterator.KEY_PROT_TAXA);
		if (taxa != null) {
			for (int i = 0; i < taxa.size(); ++i) { // write them in order
													// general -> special!
				write(taxa.get(i) + ProteinFileTransliterator.SPACER);
			}
		}
		write(species);
		writeNewline();
	}

	private void writeAnnotations(HashMap<String, Set<String>> annotations)
			throws ScansiteUpdaterException {
		for (String annType : annotations.keySet()) {
			for (String entryValue : annotations.get(annType)) {
				writeKey(ProteinFileTransliterator.KEY_PROT_ANNOTATION);
				write(annType + ProteinFileTransliterator.SPACER + entryValue);
				writeNewline();
			}
		}
	}

	private void writePI(Double pI) throws ScansiteUpdaterException {
		writeKey(ProteinFileTransliterator.KEY_PROT_PI);
		write(pI.toString());
		writeNewline();
	}

	private void writeMw(Double mw) throws ScansiteUpdaterException {
		writeKey(ProteinFileTransliterator.KEY_PROT_MW);
		write(mw.toString());
		writeNewline();
	}

	private void writeId(String proteinId) throws ScansiteUpdaterException {
		writeKey(ProteinFileTransliterator.KEY_PROT_ID);
		write(proteinId);
		writeNewline();
	}

	private void writeNewline() throws ScansiteUpdaterException {
		write("\n");
	}

	private void write(String s) throws ScansiteUpdaterException {
		try {
			writer.write(s);
		} catch (IOException e) {
			throw new ScansiteUpdaterException(ERROR_ACCESSING_FILE, e);
		}
	}

	private void writeError(String s) throws ScansiteUpdaterException {
		try {
			errorWriter.write(s);
		} catch (IOException e) {
			throw new ScansiteUpdaterException(ERROR_ACCESSING_FILE, e);
		}
	}

	@Override
	public void close() throws ScansiteUpdaterException {
		try {
			writer.close();
			errorWriter.close();
		} catch (IOException e) {
			throw new ScansiteUpdaterException(
					"Error closing temporary files!", e);
		}
	}
}
