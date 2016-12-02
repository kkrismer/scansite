package edu.mit.scansite.server.dataaccess;

import java.util.List;
import java.util.Map;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.IdentifierType;

/**
 * @author Konstantin Krismer
 */
public interface IdentifierDao extends Dao {

	public abstract List<DataSource> getCompatibleProteinDataSourcesForIdentifierType(
			IdentifierType type) throws DataAccessException;

	public abstract List<DataSource> getCompatibleOrthologyDataSourcesForIdentifierType(
			IdentifierType type) throws DataAccessException;

	public abstract List<DataSource> getCompatibleLocalizationDataSourcesForIdentifierType(
			IdentifierType type) throws DataAccessException;

	public abstract void addOrUpdateIdentifierType(IdentifierType identifierType)
			throws DataAccessException;

	public abstract IdentifierType getIdentifierType(int id)
			throws DataAccessException;

	public abstract List<IdentifierType> getAllIdentifierTypes()
			throws DataAccessException;

	public abstract Map<String, List<String>> mapIdentifiers(
			List<String> identifiers, IdentifierType inputIdentifierType,
			IdentifierType outputIdentifierType) throws NoSuchMethodException;

	public abstract List<String> mapIdentifier(String identifier,
			IdentifierType inputIdentifierType,
			IdentifierType outputIdentifierType) throws DataAccessException,
			NoSuchMethodException;

}