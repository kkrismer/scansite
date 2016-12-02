package edu.mit.scansite.server.dataaccess;

import java.util.List;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Protein;

/**
 * @author Konstantin Krismer
 */
public interface OrthologyDao extends Dao {

	public abstract void setUseTempTablesForUpdate(
			boolean useTempTablesForUpdate);

	public abstract void addOrthologyEntry(DataSource orthologyDataSource,
			int orthologsGroupId, String orthologsIdentifier)
			throws DataAccessException;

	public abstract List<Protein> getOrthologs(DataSource orthologyDataSource,
			DataSource proteinDataSource, String identifier)
			throws DatabaseException;

	public abstract int getOrthologsCountByIdentifier(
			DataSource orthologyDataSource, String identifier)
			throws DataAccessException;

}