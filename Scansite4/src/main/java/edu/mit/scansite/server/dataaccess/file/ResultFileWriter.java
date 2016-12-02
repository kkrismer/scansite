package edu.mit.scansite.server.dataaccess.file;

import java.util.List;
import edu.mit.scansite.shared.FilePaths;

/**
 * @author Tobieh
 */
public abstract class ResultFileWriter<T> {
	protected static final String FILE_PREFIX = "results";
	protected static final String FILE_POSTFIX = ".tsv";
	protected static final String SEPARATOR = "\t";

	public ResultFileWriter() {
	}

	/**
	 * @return A unique filepath.
	 */
	protected String getFilePath(String realPath) {
		DirectoryManagement.prepareResultsDirectory(realPath);
		return FilePaths.getResultFilePath(realPath,
				FILE_PREFIX + String.valueOf(System.nanoTime()) + FILE_POSTFIX);
	}

	/**
	 * Writes the given results to a file and returns the file's path.
	 * 
	 * @param hits
	 *            A list of results of type T.
	 * @return The resultfile's path.
	 * @throws ResultFileWriterException
	 *             Is thrown if an error occurs.
	 */
	public abstract String writeResults(String realPath, List<T> hits)
			throws ResultFileWriterException;

}
