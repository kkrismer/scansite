package edu.mit.scansite.server.updater.transliterator;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Konstantin Krismer
 */
public interface OrthologyTransliteratorWriter {

	public abstract void saveInvalidEntry(String line)
			throws ScansiteUpdaterException;

	public abstract void saveEntry(int orthologsGroupId,
			String orthologsIdentifier)
			throws ScansiteUpdaterException;

	public abstract void close() throws ScansiteUpdaterException;
}
