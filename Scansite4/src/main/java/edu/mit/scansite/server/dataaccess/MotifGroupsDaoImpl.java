package edu.mit.scansite.server.dataaccess;

import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.motifgroup.MotifGroupAddCommand;
import edu.mit.scansite.server.dataaccess.commands.motifgroup.MotifGroupDeleteCommand;
import edu.mit.scansite.server.dataaccess.commands.motifgroup.LightWeightMotifGroupGetAllCommand;
import edu.mit.scansite.server.dataaccess.commands.motifgroup.MotifGroupGetAllCommand;
import edu.mit.scansite.server.dataaccess.commands.motifgroup.MotifGroupGetCommand;
import edu.mit.scansite.server.dataaccess.commands.motifgroup.MotifGroupUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.MotifGroup;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupsDaoImpl extends DaoImpl implements MotifGroupsDao {

	public MotifGroupsDaoImpl(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.MotifGroupsDao#add(edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup)
	 */
	@Override
	public void add(LightWeightMotifGroup group) throws DataAccessException {
		MotifGroupAddCommand command = new MotifGroupAddCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, group);
		try {
			group.setId(command.execute());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.MotifGroupsDao#update(edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup)
	 */
	@Override
	public boolean update(LightWeightMotifGroup group)
			throws DataAccessException {
		MotifGroupUpdateCommand command = new MotifGroupUpdateCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector, group);
		try {
			return command.execute() > 0;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Updating group in DB failed.", e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.MotifGroupsDao#delete(int)
	 */
	@Override
	public void delete(int id) throws DataAccessException {
		MotifGroupDeleteCommand command = new MotifGroupDeleteCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector, id);
		try {
			command.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Deleting group from DB failed.", e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.MotifGroupsDao#get(int)
	 */
	@Override
	public LightWeightMotifGroup get(int id) throws DataAccessException {
		MotifGroupGetCommand command = new MotifGroupGetCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, id);
		LightWeightMotifGroup group;
		try {
			group = command.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Retrieving group from DB failed.",
					e);
		}
		return group;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.MotifGroupsDao#getAllLightWeight()
	 */
	@Override
	public List<LightWeightMotifGroup> getAllLightWeight()
			throws DataAccessException {
		LightWeightMotifGroupGetAllCommand command = new LightWeightMotifGroupGetAllCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector);
		List<LightWeightMotifGroup> groups;
		try {
			groups = command.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Retrieving groups from DB failed.",
					e);
		}
		return groups;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.MotifGroupsDao#getAll()
	 */
	@Override
	public List<MotifGroup> getAll() throws DataAccessException {
		return getAll(null, true);
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.MotifGroupsDao#getAll(boolean)
	 */
	@Override
	public List<MotifGroup> getAll(boolean publicOnly) throws DataAccessException {
		return getAll(null, publicOnly);
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.MotifGroupsDao#getAll(edu.mit.scansite.shared.transferobjects.MotifClass, boolean)
	 */
	@Override
	public List<MotifGroup> getAll(MotifClass motifClass, boolean publicOnly)
			throws DataAccessException {
		MotifGroupGetAllCommand command = new MotifGroupGetAllCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector, motifClass, publicOnly);
		List<MotifGroup> groups;
		try {
			groups = command.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Retrieving groups from DB failed.",
					e);
		}
		return groups;
	}
}
