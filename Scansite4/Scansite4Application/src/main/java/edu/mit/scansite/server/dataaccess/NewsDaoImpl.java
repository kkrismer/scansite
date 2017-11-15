package edu.mit.scansite.server.dataaccess;

import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.news.NewsAddCommand;
import edu.mit.scansite.server.dataaccess.commands.news.NewsDeleteCommand;
import edu.mit.scansite.server.dataaccess.commands.news.NewsGetAllCommand;
import edu.mit.scansite.server.dataaccess.commands.news.NewsGetCommand;
import edu.mit.scansite.server.dataaccess.commands.news.NewsUpdateCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.NewsEntry;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsDaoImpl extends DaoImpl implements NewsDao {

	public NewsDaoImpl(Properties dbAccessConfig, Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.NewsDao#add(edu.mit.scansite.shared
	 * .transferobjects.NewsEntry)
	 */
	@Override
	public void add(NewsEntry entry) throws DataAccessException {
		NewsAddCommand command = new NewsAddCommand(dbAccessConfig, dbConstantsConfig, entry);
		try {
			command.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Adding entry to DB failed.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.NewsDao#update(edu.mit.scansite.shared
	 * .transferobjects.NewsEntry)
	 */
	@Override
	public boolean update(NewsEntry entry) throws DataAccessException {
		NewsUpdateCommand command = new NewsUpdateCommand(dbAccessConfig, dbConstantsConfig, entry);
		try {
			return command.execute() > 0;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Updating entry in DB failed.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.NewsDao#delete(int)
	 */
	@Override
	public void delete(int id) throws DataAccessException {
		NewsDeleteCommand command = new NewsDeleteCommand(dbAccessConfig, dbConstantsConfig, id);
		try {
			command.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Deleting entry from DB failed.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.NewsDao#get(int)
	 */
	@Override
	public NewsEntry get(int id) throws DataAccessException {
		NewsGetCommand command = new NewsGetCommand(dbAccessConfig, dbConstantsConfig, id);
		NewsEntry entry;
		try {
			entry = command.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Retrieving entry from DB failed.", e);
		}
		return entry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.NewsDao#getAll(int)
	 */
	@Override
	public List<NewsEntry> getAll(int nrOfNewestEntries) throws DataAccessException {
		NewsGetAllCommand command = new NewsGetAllCommand(dbAccessConfig, dbConstantsConfig, nrOfNewestEntries);
		List<NewsEntry> news;
		try {
			news = command.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Retrieving entries from DB failed: " + e.getMessage(), e);
		}
		return news;
	}
}
