package edu.mit.scansite.server.dataaccess;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.commands.orthology.OrthologProteinsGetCommand;
import edu.mit.scansite.server.dataaccess.commands.orthology.OrthologsCountByIdentifierGetCommand;
import edu.mit.scansite.server.dataaccess.commands.orthology.OrthologyAddCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Protein;

/**
 * @author Konstantin Krismer
 */
public class OrthologyDaoImpl extends DaoImpl implements OrthologyDao {

	public OrthologyDaoImpl(Properties dbAccessConfig, Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.OrthologyDao#setUseTempTablesForUpdate(
	 * boolean)
	 */
	@Override
	public void setUseTempTablesForUpdate(boolean useTempTablesForUpdate) {
		this.useTempTablesForUpdate = useTempTablesForUpdate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.OrthologyDao#addOrthologyEntry(edu.mit.
	 * scansite.shared.transferobjects.DataSource, int, java.lang.String)
	 */
	@Override
	public void addOrthologyEntry(DataSource orthologyDataSource, int orthologsGroupId, String orthologsIdentifier)
			throws DataAccessException {
		OrthologyAddCommand command = new OrthologyAddCommand(dbAccessConfig, dbConstantsConfig, useTempTablesForUpdate,
				orthologyDataSource, orthologsGroupId, orthologsIdentifier);
		try {
			command.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	// public List<Protein> getOrthologs(DataSource orthologyDataSource,
	// DataSource proteinDataSource, String identifier,
	// IdentifierType proteinIdentifierType) throws DatabaseException {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.OrthologyDao#getOrthologs(edu.mit.scansite
	 * .shared.transferobjects.DataSource,
	 * edu.mit.scansite.shared.transferobjects.DataSource, java.lang.String)
	 */
	@Override
	public List<Protein> getOrthologs(DataSource orthologyDataSource, DataSource proteinDataSource, String identifier)
			throws DatabaseException {
		// assume orthology data source and protein data source have the same
		// identifier type
		List<Protein> proteins = getOrthologProteins(orthologyDataSource, proteinDataSource, identifier);
		// IdentifierDaoImpl identifierDao = ServiceLocator.getInstance()
		// .getDaoFactory(dbConnector).getIdentifierDao();
		// IdentifierType orthologyIdentifierType = identifierDao
		// .getCompatibleIdentifierTypeForDataSourceShortName(orthologyDataSource
		// .getShortName());
		// if (orthologyIdentifierType.equals(proteinIdentifierType)) {
		// // identifier types of orthology data source and protein data source
		// are
		// // the same
		//
		// proteins = getOrthologProteins(orthologyDataSource,
		// proteinDataSource,
		// identifier);
		// } else {
		// // identifier types of orthology data source and protein data source
		// are
		// // not the same
		//
		// // map identifier to orthology identifier type
		// List<String> mappedOrthologyIdentifiers =
		// identifierDao.mapIdentifier(
		// identifier, proteinIdentifierType, orthologyIdentifierType);
		// if (mappedOrthologyIdentifiers == null
		// || mappedOrthologyIdentifiers.size() == 0) {
		// return null;
		// }
		//
		// // get identifiers of ortholog proteins
		// List<String> orthologProteinIdentifiers =
		// getOrthologProteinIdentifiers(
		// orthologyDataSource, mappedOrthologyIdentifiers);
		// if (orthologProteinIdentifiers == null
		// || orthologProteinIdentifiers.size() == 0) {
		// return null;
		// }
		//
		// // map ortholog protein identifiers back to protein identifiers
		// Map<String, List<String>> mappedOrthologProteinIdentifiers =
		// identifierDao
		// .mapIdentifiers(orthologProteinIdentifiers, orthologyIdentifierType,
		// proteinIdentifierType);
		// if (mappedOrthologProteinIdentifiers == null
		// || mappedOrthologProteinIdentifiers.size() == 0) {
		// return null;
		// }
		//
		// // get full protein information
		// proteins = ServiceLocator
		// .getInstance()
		// .getDaoFactory(dbConnector)
		// .getProteinDao()
		// .get(collectUniqueValues(mappedOrthologProteinIdentifiers.values()),
		// proteinDataSource.getShortName());
		// }
		return (proteins.size() > 0) ? proteins : null;
	}

	// private List<String> collectUniqueValues(Collection<List<String>> values)
	// {
	// Set<String> uniqueValues = new HashSet<String>();
	//
	// for (List<String> valueBatch : values) {
	// uniqueValues.addAll(valueBatch);
	// }
	//
	// return new ArrayList<String>(uniqueValues);
	// }

	private List<Protein> getOrthologProteins(DataSource orthologyDataSource, DataSource proteinDataSource,
			String identifier) throws DataAccessException {
		return getOrthologProteins(orthologyDataSource, proteinDataSource, Arrays.asList(identifier));
	}

	private List<Protein> getOrthologProteins(DataSource orthologyDataSource, DataSource proteinDataSource,
			List<String> identifiers) throws DataAccessException {
		try {
			if (identifiers != null && identifiers.size() > 0) {
				OrthologProteinsGetCommand cmd = new OrthologProteinsGetCommand(dbAccessConfig, dbConstantsConfig,
						useTempTablesForUpdate, orthologyDataSource, identifiers, proteinDataSource);
				List<Protein> proteins = cmd.execute();
				proteins = ServiceLocator.getDaoFactory().getProteinDao().getProteinInformation(proteins,
						proteinDataSource);
				return proteins;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	// private List<String> getOrthologProteinIdentifiers(
	// DataSource orthologyDataSource, List<String> identifiers)
	// throws DataAccessException {
	// try {
	// if (identifiers != null && identifiers.size() > 0) {
	// OrthologProteinIdentifiersGetCommand cmd = new
	// OrthologProteinIdentifiersGetCommand(
	// dbAccessConfig, dbConstantsConfig, dbConnector,
	// orthologyDataSource, identifiers, useTempTablesForUpdate);
	// return cmd.execute();
	// } else {
	// return null;
	// }
	// } catch (Exception e) {
	// throw new DataAccessException(e.getMessage(), e);
	// }
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.OrthologyDao#getOrthologsCountByIdentifier
	 * (edu.mit.scansite.shared.transferobjects.DataSource, java.lang.String)
	 */
	@Override
	public int getOrthologsCountByIdentifier(DataSource orthologyDataSource, String identifier)
			throws DataAccessException {
		OrthologsCountByIdentifierGetCommand cmd = new OrthologsCountByIdentifierGetCommand(dbAccessConfig,
				dbConstantsConfig, useTempTablesForUpdate, orthologyDataSource, identifier);
		try {
			return cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}
}
