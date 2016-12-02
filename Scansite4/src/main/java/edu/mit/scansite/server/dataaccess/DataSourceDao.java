package edu.mit.scansite.server.dataaccess;

import java.util.List;
import java.util.Map;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.DataSourceType;

/**
 * @author Konstantin Krismer
 */
public interface DataSourceDao extends Dao {

	public abstract int addOrUpdate(DataSource dataSource)
			throws DataAccessException;

	public abstract void addOrUpdateDataSourceType(DataSourceType dataSourceType)
			throws DataAccessException;

	public abstract DataSource get(int id) throws DataAccessException;

	public abstract DataSource get(String shortName) throws DataAccessException;

	public abstract DataSourceType getDataSourceType(int id)
			throws DataAccessException;

	public abstract DataSourceType getDataSourceType(String shortName)
			throws DataAccessException;

	public abstract List<DataSource> getAll(DataSourceType type)
			throws DataAccessException;

	public abstract List<DataSource> getAll(boolean primaryDataSourcesOnly)
			throws DataAccessException;

	public abstract List<DataSource> getAll(DataSourceType type,
			boolean primaryDataSourcesOnly) throws DataAccessException;

	public abstract Map<DataSource, Integer> getDataSourceSizes()
			throws DataAccessException;

	public abstract void delete(int id) throws DataAccessException;

	public abstract int getEntryCountByDataSource(DataSource dataSource)
			throws DataAccessException;

	public abstract List<DataSourceType> getDataSourceTypes()
			throws DataAccessException;

}