package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Tobieh
 */
public class EnsemblFastaFileTransliterator extends ProteinFileTransliterator {
	private static final String FASTA_ENTRY_START = ">";
	private static final String LOCATION_CHR = "chromosome:";
	private static final String LOCATION_CONTIG = "contig:";
	private static final String TYPE = "pep:";
	private static final String GENE = "gene:";
	private static final String TRANSCRIPT = "transcript:";

	private String line = null;
	private String organism = "";

	public EnsemblFastaFileTransliterator(List<BufferedReader> readers,
			ProteinTransliteratorWriter writer, String organism) {
		super(readers, writer);
		this.organism = organism;
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
		setSequence(sequence.toUpperCase());
		return line;
	}

	private void parseHeaderLine(String line) {
		setSpecies(organism);

		String[] lineArr = splitByWhitespaces(line.trim());
		if (lineArr != null && lineArr.length >= 1) {
			setProteinId(lineArr[0]);
			for (int i = 1; i < lineArr.length; ++i) {
				String s = lineArr[i].trim();
				String type = PROT_OTHER_ANNOTATION;
				if (!s.isEmpty()) {
					if (s.contains(LOCATION_CONTIG)
							|| s.startsWith(LOCATION_CHR)) {
						type = PROT_LOCATION_ANNOTATION;
					} else if (s.startsWith(TYPE)) {
						type = PROT_TYPE_ANNOTATION;
					} else if (s.startsWith(TRANSCRIPT)) {
						type = PROT_TRANSCRIPT_ANNOTATION;
						s = s.substring(TRANSCRIPT.length());
					} else if (s.startsWith(GENE)) {
						type = PROT_GENE_ANNOTATION;
						s = s.substring(GENE.length());
					}
					if (!type.equals(PROT_OTHER_ANNOTATION)) {
						addAnnotation(type, s);
					}
				}
			}
		}
	}

}
