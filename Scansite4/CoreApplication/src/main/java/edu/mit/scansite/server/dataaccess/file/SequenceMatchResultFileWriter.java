package edu.mit.scansite.server.dataaccess.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import edu.mit.scansite.shared.transferobjects.ProteinSequenceMatch;
import edu.mit.scansite.shared.transferobjects.SequencePattern;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SequenceMatchResultFileWriter extends
		ResultFileWriter<ProteinSequenceMatch> {

	private List<SequencePattern> sequencePatterns;

	public SequenceMatchResultFileWriter(List<SequencePattern> sequencePatterns) {
		this.sequencePatterns = sequencePatterns;
	}

	@Override
	public String writeResults(String realPath,
			List<ProteinSequenceMatch> matches)
			throws ResultFileWriterException {
		String currentFilePath = getFilePath(realPath);
		BufferedWriter writer = null;
		try {
			DirectoryManagement.prepareDirectory(currentFilePath, true);
			writer = new BufferedWriter(new FileWriter(currentFilePath));
			writer.write("protein_accession");
			writer.write(SEPARATOR);
			for (SequencePattern pattern : sequencePatterns) {
				writer.write("occurrences_" + pattern.getRegEx());
				writer.write(SEPARATOR);
			}
			writer.newLine();
			for (ProteinSequenceMatch match : matches) {
				writer.write(match.getProtein().getIdentifier());
				writer.write(SEPARATOR);
				for (int i : match.getNrOfSequenceMatches()) {
					writer.write(String.valueOf(i));
					writer.write(SEPARATOR);
				}
				writer.newLine();
			}
			writer.close();
			return currentFilePath;
		} catch (Exception e) {
			throw new ResultFileWriterException(e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
				throw new ResultFileWriterException(e);
			}
		}
	}
}
