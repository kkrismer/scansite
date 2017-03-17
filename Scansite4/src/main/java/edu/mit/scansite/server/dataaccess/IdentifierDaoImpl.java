package edu.mit.scansite.server.dataaccess;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.datasource.DataSourcesByIdentifierTypeGetCommand;
import edu.mit.scansite.server.dataaccess.commands.identifiertype.IdentifierTypeAddCommand;
import edu.mit.scansite.server.dataaccess.commands.identifiertype.IdentifierTypeByIdGetCommand;
import edu.mit.scansite.server.dataaccess.commands.identifiertype.IdentifierTypeUpdateCommand;
import edu.mit.scansite.server.dataaccess.commands.identifiertype.IdentifierTypesGetAllCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.IdentifierType;

/**
 * @author Konstantin Krismer
 */
public class IdentifierDaoImpl extends DaoImpl implements IdentifierDao {

	public IdentifierDaoImpl(Properties dbAccessConfig,
			Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.IdentifierDao#getCompatibleProteinDataSourcesForIdentifierType(edu.mit.scansite.shared.transferobjects.IdentifierType)
	 */
	@Override
	public List<DataSource> getCompatibleProteinDataSourcesForIdentifierType(
			IdentifierType type) throws DataAccessException {
		DataSourcesByIdentifierTypeGetCommand cmd = new DataSourcesByIdentifierTypeGetCommand(
				dbAccessConfig, dbConstantsConfig, type);
		try {
			List<DataSource> dataSources = cmd.execute();
			List<DataSource> result = new LinkedList<DataSource>();
			for (DataSource dataSource : dataSources) {
				if (dataSource.getType().getShortName().equals("proteins")) {
					result.add(dataSource);
				}
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.IdentifierDao#getCompatibleOrthologyDataSourcesForIdentifierType(edu.mit.scansite.shared.transferobjects.IdentifierType)
	 */
	@Override
	public List<DataSource> getCompatibleOrthologyDataSourcesForIdentifierType(
			IdentifierType type) throws DataAccessException {
		DataSourcesByIdentifierTypeGetCommand cmd = new DataSourcesByIdentifierTypeGetCommand(
				dbAccessConfig, dbConstantsConfig, type);
		try {
			List<DataSource> dataSources = cmd.execute();
			List<DataSource> result = new LinkedList<DataSource>();
			for (DataSource dataSource : dataSources) {
				if (dataSource.getType().getShortName().equals("orthologs")) {
					result.add(dataSource);
				}
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.IdentifierDao#getCompatibleLocalizationDataSourcesForIdentifierType(edu.mit.scansite.shared.transferobjects.IdentifierType)
	 */
	@Override
	public List<DataSource> getCompatibleLocalizationDataSourcesForIdentifierType(
			IdentifierType type) throws DataAccessException {
		DataSourcesByIdentifierTypeGetCommand cmd = new DataSourcesByIdentifierTypeGetCommand(
				dbAccessConfig, dbConstantsConfig, type);
		try {
			List<DataSource> dataSources = cmd.execute();
			List<DataSource> result = new LinkedList<DataSource>();
			for (DataSource dataSource : dataSources) {
				if (dataSource.getType().getShortName().equals("localization")) {
					result.add(dataSource);
				}
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	//
	// public Map<DataSource, IdentifierType>
	// getAllIdentifierTypeCompatibilities()
	// throws DataAccessException {
	// IdentifierTypeCompatibilityGetAllCommand cmd = new
	// IdentifierTypeCompatibilityGetAllCommand(
	// dbAccessConfig, dbConstantsConfig, dbConnector);
	// try {
	// return cmd.execute();
	// } catch (Exception e) {
	// throw new DataAccessException(e.getMessage());
	// }
	// }
	//
	// public Map<IdentifierType, List<DataSource>>
	// getAllDataSourceCompatibilities()
	// throws DataAccessException {
	// IdentifierTypeCompatibilityGetAllCommand cmd = new
	// IdentifierTypeCompatibilityGetAllCommand(
	// dbAccessConfig, dbConstantsConfig, dbConnector);
	// try {
	// Map<DataSource, IdentifierType> reverseMapping = cmd.execute();
	// Map<IdentifierType, List<DataSource>> mapping = new
	// HashMap<IdentifierType, List<DataSource>>();
	//
	// for (Map.Entry<DataSource, IdentifierType> entry : reverseMapping
	// .entrySet()) {
	// if (mapping.containsKey(entry.getValue())) {
	// mapping.get(entry.getValue()).add(entry.getKey());
	// } else {
	// List<DataSource> dataSources = new LinkedList<DataSource>();
	// dataSources.add(entry.getKey());
	// mapping.put(entry.getValue(), dataSources);
	// }
	// }
	// return mapping;
	// } catch (DatabaseException e) {
	// throw new DataAccessException(e.getMessage(), e);
	// }
	// }

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.IdentifierDao#addOrUpdateIdentifierType(edu.mit.scansite.shared.transferobjects.IdentifierType)
	 */
	@Override
	public void addOrUpdateIdentifierType(IdentifierType identifierType)
			throws DataAccessException {
		try {
			IdentifierType temp = getIdentifierType(identifierType.getId());
			if (temp == null) {
				IdentifierTypeAddCommand cmd = new IdentifierTypeAddCommand(
						dbAccessConfig, dbConstantsConfig, identifierType);
				cmd.execute();
			} else {
				IdentifierTypeUpdateCommand cmd = new IdentifierTypeUpdateCommand(
						dbAccessConfig, dbConstantsConfig, identifierType);
				cmd.execute();
			}
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.IdentifierDao#getIdentifierType(int)
	 */
	@Override
	public IdentifierType getIdentifierType(int id) throws DataAccessException {
		IdentifierTypeByIdGetCommand cmd = new IdentifierTypeByIdGetCommand(
				dbAccessConfig, dbConstantsConfig, id);
		try {
			return cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.IdentifierDao#getAllIdentifierTypes()
	 */
	@Override
	public List<IdentifierType> getAllIdentifierTypes()
			throws DataAccessException {
		IdentifierTypesGetAllCommand cmd = new IdentifierTypesGetAllCommand(
				dbAccessConfig, dbConstantsConfig);
		try {
			return cmd.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.IdentifierDao#mapIdentifiers(java.util.List, edu.mit.scansite.shared.transferobjects.IdentifierType, edu.mit.scansite.shared.transferobjects.IdentifierType)
	 */
	@Override
	public Map<String, List<String>> mapIdentifiers(List<String> identifiers,
			IdentifierType inputIdentifierType,
			IdentifierType outputIdentifierType) throws NoSuchMethodException {
		// BioDBnetImpl client;
		// try {
		// client = new BioDBnetImpl();
		// return parseBioDBnetOutput(client.db2db(
		// inputIdentifierType.getName(), collapseList(identifiers),
		// outputIdentifierType.getName(), ""));
		// } catch (AxisFault e) {
		// throw new DataAccessException(e.getMessage(), e);
		// } catch (RemoteException e) {
		// throw new DataAccessException(e.getMessage(), e);
		// }
		throw new NoSuchMethodException();
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.IdentifierDao#mapIdentifier(java.lang.String, edu.mit.scansite.shared.transferobjects.IdentifierType, edu.mit.scansite.shared.transferobjects.IdentifierType)
	 */
	@Override
	public List<String> mapIdentifier(String identifier,
			IdentifierType inputIdentifierType,
			IdentifierType outputIdentifierType) throws DataAccessException,
			NoSuchMethodException {
		List<String> identifiers = new LinkedList<String>();
		identifiers.add(identifier);
		Map<String, List<String>> mappedIdentifiers = mapIdentifiers(
				identifiers, inputIdentifierType, outputIdentifierType);
		if (mappedIdentifiers.containsKey(identifier)) {
			return mappedIdentifiers.get(identifier);
		} else {
			return null;
		}
	}

	// private String collapseList(List<String> elements) {
	// StringBuilder collapsed = new StringBuilder();
	//
	// for (int i = 0; i < elements.size(); ++i) {
	// if (i != 0) {
	// collapsed.append(", ");
	// }
	// collapsed.append(elements.get(i));
	// }
	//
	// return collapsed.toString();
	// }

	// private Map<String, List<String>> parseBioDBnetOutput(String
	// bioDBnetOutput) {
	// // format:
	// // [INPUT_IDENTIFIER_TYPE]\t[OUTPUT_IDENTIFIER_TYPE]
	// //
	// [INPUT_IDENTIFIER_1]\t[OUTPUT_IDENTIFIER_1_1];[OUTPUT_IDENTIFIER_1_2];...
	// //
	// [INPUT_IDENTIFIER_2]\t[OUTPUT_IDENTIFIER_2_1];[OUTPUT_IDENTIFIER_2_2];...
	// // ...
	// Map<String, List<String>> identifiers = new HashMap<String,
	// List<String>>();
	//
	// String[] lines = bioDBnetOutput.split("\\n");
	// for (int i = 1; i < lines.length; ++i) {
	// String[] cells = lines[i].split("\\t");
	// if (cells.length == 2) {
	// String[] outputIdentifiers = cells[1].split(";");
	// List<String> mappedIdentifiers = new LinkedList<String>(
	// Arrays.asList(outputIdentifiers));
	//
	// identifiers.put(cells[0], cleanIdentifiers(mappedIdentifiers));
	// }
	// }
	//
	// return identifiers;
	// }

	// private List<String> cleanIdentifiers(List<String> mappedIdentifiers) {
	// List<String> cleanedIdentifiers = new LinkedList<String>();
	// for (String identifier : mappedIdentifiers) {
	// if (identifier.length() > 0 && !identifier.equals("-")) {
	// cleanedIdentifiers.add(identifier);
	// }
	// }
	// return cleanedIdentifiers;
	// }
}