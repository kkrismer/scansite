package edu.mit.scansite.server.dataaccess;

import java.util.List;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.MotifGroup;

/**
 * @author Konstantin Krismer
 */
public interface MotifGroupsDao extends Dao {

	public abstract void add(LightWeightMotifGroup group)
			throws DataAccessException;

	public abstract boolean update(LightWeightMotifGroup group)
			throws DataAccessException;

	public abstract void delete(int id) throws DataAccessException;

	public abstract LightWeightMotifGroup get(int id)
			throws DataAccessException;

	public abstract List<LightWeightMotifGroup> getAllLightWeight()
			throws DataAccessException;

	public abstract List<MotifGroup> getAll() throws DataAccessException;

	public abstract List<MotifGroup> getAll(boolean publicOnly)
			throws DataAccessException;

	public abstract List<MotifGroup> getAll(MotifClass motifClass,
			boolean publicOnly) throws DataAccessException;

}