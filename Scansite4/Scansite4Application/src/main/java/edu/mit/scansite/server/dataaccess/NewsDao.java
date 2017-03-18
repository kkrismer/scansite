package edu.mit.scansite.server.dataaccess;

import java.util.List;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.NewsEntry;

/**
 * @author Konstantin Krismer
 */
public interface NewsDao extends Dao {

	public abstract void add(NewsEntry entry) throws DataAccessException;

	public abstract boolean update(NewsEntry entry) throws DataAccessException;

	public abstract void delete(int id) throws DataAccessException;

	public abstract NewsEntry get(int id) throws DataAccessException;

	/**
	 * @param nrOfNewestEntries
	 *            The number of newest news entries that should be returned. If
	 *            0 is given, all entries are returned.
	 * @return The newest nrOfNewestEntries news entries. All entries, if 0 is
	 *         given as parameter.
	 */
	public abstract List<NewsEntry> getAll(int nrOfNewestEntries)
			throws DataAccessException;

}