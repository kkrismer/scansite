package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class FileTransliterator {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected List<BufferedReader> readers;

	/**
	 * Reads the input file (reader) and saves the needed information in a given
	 * format to a file (writer). Invalid entries (entries that cannot be parsed
	 * correctly) are saved to another file (errorWriter).
	 * 
	 * @throws ScansiteUpdaterException
	 *             Is thrown if an error occurs, usually, if the input file
	 *             cannot be read.
	 */
	public abstract void transliterate() throws ScansiteUpdaterException;
}
