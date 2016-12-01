package edu.mit.scansite.server.dataaccess;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.datasource.DataSourceAddCommand;
import edu.mit.scansite.server.dataaccess.commands.datasource.DataSourceDeleteCommand;
import edu.mit.scansite.server.dataaccess.commands.datasource.DataSourceEntryCountGetCommand;
import edu.mit.scansite.server.dataaccess.commands.datasource.DataSourceEntryCountsGetCommand;
import edu.mit.scansite.server.dataaccess.commands.datasource.DataSourceGetAllCommand;
import edu.mit.scansite.server.dataaccess.commands.datasource.DataSourceGetCommand;
import edu.mit.scansite.server.dataaccess.commands.datasource.DataSourceUpdateCommand;
import edu.mit.scansite.server.dataaccess.commands.datasourcetype.DataSourceTypeAddCommand;
import edu.mit.scansite.server.dataaccess.commands.datasourcetype.DataSourceTypeGetAllCommand;
import edu.mit.scansite.server.dataaccess.commands.datasourcetype.DataSourceTypeGetCommand;
import edu.mit.scansite.server.dataaccess.commands.datasourcetype.DataSourceTypeUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.DataSourceType;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DataSourceDaoImpl extends DaoImpl implements DataSourceDao {

	public DataSourceDaoImpl(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.DataSourceDao#addOrUpdate(edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public int addOrUpdate(DataSource dataSource) throws DataAccessException {
		int id = dataSource.getId();
		try {
			if (id < 0) {
				DataSource temp = get(dataSource.getShortName());
				if (temp == null) {
					DataSourceAddCommand cmd = new DataSourceAddCommand(
							dbAccessConfig, dbConstantsConfig, dbConnector,
							dataSource);
					id = cmd.execute();
				} else {
					id = temp.getId();
					dataSource.setId(id);
					DataSourceUpdateCommand cmd = new DataSourceUpdateCommand(
							dbAccessConfig, dbConstantsConfig, dbConnector,
							dataSource);
					cmd.execute(); // an updatecommand returns number of changed
									// rows. we
									// are not really interested in this number.
					return id;
				}
			} else {
				DataSourceUpdateCommand cmd = new DataSourceUpdateCommand(
						dbAccessConfig, dbConstantsConfig, dbConnector,
						dataSource);
				cmd.execute();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return id;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.DataSourceDao#addOrUpdateDataSourceType(edu.mit.scansite.shared.transferobjects.DataSourceType)
	 */
	@Override
	public void addOrUpdateDataSourceType(DataSourceType dataSourceType)
			throws DataAccessException {
		try {
			DataSourceType temp = getDataSourceType(dataSourceType.getId());
			if (temp == null) {
				DataSourceTypeAddCommand cmd = new DataSourceTypeAddCommand(
						dbAccessConfig, dbConstantsConfig, dbConnector,
						dataSourceType);
				cmd.execute();
			} else {
				DataSourceTypeUpdateCommand cmd = new DataSourceTypeUpdateCommand(
						dbAccessConfig, dbConstantsConfig, dbConnector,
						dataSourceType);
				cmd.execute();
			}
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.DataSourceDao#get(int)
	 */
	@Override
	public DataSource get(int id) throws DataAccessException {
		DataSourceGetCommand cmd = new DataSourceGetCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, id);
		DataSource dataSource = null;
		try {
			dataSource = cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return dataSource;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.DataSourceDao#get(java.lang.String)
	 */
	@Override
	public DataSource get(String shortName) throws DataAccessException {
		DataSourceGetCommand cmd = new DataSourceGetCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, shortName);
		DataSource dataSource = null;
		try {
			dataSource = cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return dataSource;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.DataSourceDao#getDataSourceType(int)
	 */
	@Override
	public DataSourceType getDataSourceType(int id) throws DataAccessException {
		DataSourceTypeGetCommand cmd = new DataSourceTypeGetCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector, id);
		DataSourceType dataSourceType = null;
		try {
			dataSourceType = cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return dataSourceType;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.DataSourceDao#getDataSourceType(java.lang.String)
	 */
	@Override
	public DataSourceType getDataSourceType(String shortName)
			throws DataAccessException {
		DataSourceTypeGetCommand cmd = new DataSourceTypeGetCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector, shortName);
		DataSourceType dataSourceType = null;
		try {
			dataSourceType = cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return dataSourceType;
	}

	// private boolean checkAvailabilityOfAuxiliaryDataSource(
	// DataSource majorDataSource, DataSourceType auxiliaryDataSourceType)
	// throws DataAccessException, DatabaseException {
	// IdentifierTypeCompatibilityByDataSourceShortNameGetCommand
	// identifierTypeCommand = new
	// IdentifierTypeCompatibilityByDataSourceShortNameGetCommand(
	// dbAccessConfig, dbConstantsConfig, dbConnector,
	// majorDataSource.getShortName());
	// IdentifierType dataSourceIdentifierType =
	// identifierTypeCommand.execute();
	// DataSourcesByIdentifierTypeGetCommand compatibleDataSources = new
	// DataSourcesByIdentifierTypeGetCommand(
	// dbAccessConfig, dbConstantsConfig, dbConnector,
	// dataSourceIdentifierType);
	// List<DataSource> dataSources = compatibleDataSources.execute();
	// if (dataSources != null) {
	// for (DataSource dataSource : dataSources) {
	// if (dataSource.getType().equals(auxiliaryDataSourceType)) {
	// return true;
	// }
	// }
	// }
	// return false;
	// }

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.DataSourceDao#getAll(edu.mit.scansite.shared.transferobjects.DataSourceType)
	 */
	@Override
	public List<DataSource> getAll(DataSourceType type)
			throws DataAccessException {
		return getAll(type, false);
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.DataSourceDao#getAll(boolean)
	 */
	@Override
	public List<DataSource> getAll(boolean primaryDataSourcesOnly)
			throws DataAccessException {
		return getAll(null, primaryDataSourcesOnly);
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.DataSourceDao#getAll(edu.mit.scansite.shared.transferobjects.DataSourceType, boolean)
	 */
	@Override
	public List<DataSource> getAll(DataSourceType type,
			boolean primaryDataSourcesOnly) throws DataAccessException {
		DataSourceGetAllCommand cmd = new DataSourceGetAllCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector, type);
		List<DataSource> dataSources = null;
		try {
			dataSources = cmd.execute();
			if (primaryDataSourcesOnly) {
				List<DataSource> filteredList = new LinkedList<DataSource>();
				for (DataSource dataSource : dataSources) {
					if (dataSource.isPrimaryDataSource())
						filteredList.add(dataSource);
				}
				dataSources.clear();
				dataSources.addAll(filteredList);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return dataSources;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.DataSourceDao#getDataSourceSizes()
	 */
	@Override
	public Map<DataSource, Integer> getDataSourceSizes()
			throws DataAccessException {

		try {
			List<DataSource> dataSources = getAll(true);
			DataSourceEntryCountsGetCommand sizesCommand = new DataSourceEntryCountsGetCommand(
					dbAccessConfig, dbConstantsConfig, dbConnector, dataSources);
			return sizesCommand.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.DataSourceDao#delete(int)
	 */
	@Override
	public void delete(int id) throws DataAccessException {
		DataSourceDeleteCommand cmd = new DataSourceDeleteCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector, id);
		try {
			cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.DataSourceDao#getEntryCountByDataSource(edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public int getEntryCountByDataSource(DataSource dataSource)
			throws DataAccessException {
		DataSourceEntryCountGetCommand cmd = new DataSourceEntryCountGetCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector, dataSource);
		try {
			return cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.DataSourceDao#getDataSourceTypes()
	 */
	@Override
	public List<DataSourceType> getDataSourceTypes() throws DataAccessException {
		DataSourceTypeGetAllCommand cmd = new DataSourceTypeGetAllCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector);
		List<DataSourceType> dataSourceTypes = null;
		try {
			dataSourceTypes = cmd.execute();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return dataSourceTypes;
	}
}
