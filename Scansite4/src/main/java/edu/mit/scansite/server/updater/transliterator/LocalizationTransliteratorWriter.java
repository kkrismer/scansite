package edu.mit.scansite.server.updater.transliterator;

import java.util.List;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Konstantin Krismer
 */
public interface LocalizationTransliteratorWriter {

	public abstract void saveInvalidEntry(String line)
			throws ScansiteUpdaterException;

	public abstract void saveEntry(String proteinIdentifier,
			int score, String localization, List<String> goTerms) throws ScansiteUpdaterException;

	public abstract void close() throws ScansiteUpdaterException;
}
