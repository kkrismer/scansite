package edu.mit.scansite.server.dataaccess;

import java.util.List;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Konstantin Krismer
 */
public interface ProteinDao extends Dao {

	public abstract void setUseTempTablesForUpdate(
			boolean useTempTablesForUpdate);

	public abstract List<Protein> getAll(DataSource dataSource)
			throws DataAccessException;

	public abstract List<String> getAllIdentifiers(DataSource dataSource)
			throws DataAccessException;

	/**
	 * @param dataSource
	 *            protein data source
	 * @param taxon
	 *            A taxon with its id set.
	 * @param withProteinInfo
	 *            If TRUE, the protein's additional informations are also
	 *            gathered from the database (may be very time consuming!).
	 * @return A list of all proteins from the given datasource.
	 * @throws DataAccessException
	 */
	public abstract List<Protein> getAll(Taxon taxon, DataSource dataSource,
			boolean withProteinInfo) throws DataAccessException;

	public abstract List<Protein> getProteinInformation(List<Protein> ps,
			DataSource dataSource) throws DataAccessException;

	/**
	 * Returns those proteins that are defined by the given datasource and
	 * taxon.
	 * 
	 * @param databaseShortName
	 *            The protein's datasource's shortName.
	 * @param taxonId
	 *            A valid taxon.
	 * @return A list of proteins.
	 * @throws DataAccessException
	 */
	public abstract List<Protein> getByDatasourceAndTaxon(
			DataSource dataSource, int taxonId) throws DataAccessException;

	/**
	 * Retrieves a protein from the database.
	 * 
	 * @param identifier
	 *            The protein's identifier. The identifier is either the
	 *            protein's actual accession number or any accession number that
	 *            is saved in the database as annotation.
	 * @param dataSource
	 *            The protein's data source.
	 * @return The protein, or NULL, in case no protein was found.
	 * @throws DataAccessException
	 */
	public abstract Protein get(String identifier, DataSource dataSource)
			throws DataAccessException;

	/**
	 * Retrieves a set of proteins from the database.
	 * 
	 * @param identifiers
	 *            The proteins' accession numbers. The accession number is
	 *            either the protein's actual accession number or any accession
	 *            number that is saved in the database as annotation.
	 * @param dataSource
	 *            the protein data source
	 * @return A list of proteins, or NULL, in case no protein was found.
	 * @throws DataAccessException
	 */
	public abstract List<Protein> get(List<String> identifiers,
			DataSource dataSource) throws DataAccessException;

	/**
	 * Adds a protein to the database.
	 * 
	 * @param p
	 *            A protein.
	 * @param doAddAnnotations
	 *            TRUE, if the protein's annotations should also be added to the
	 *            database, otherwise FALSE.
	 * @param dataSource
	 *            The protein's data source.
	 * @throws DataAccessException
	 */
	public abstract void add(Protein p, boolean doAddAnnotations,
			DataSource dataSource) throws DataAccessException;

	public abstract int getProteinCount(DataSource dataSource)
			throws DataAccessException;

	/**
	 * Gets a list of proteins restricted by the given parameters.
	 * 
	 * @param dataSource
	 *            <b>REQUIRED</b> protein data source
	 * @param sequenceRegexs
	 *            <i>OPTIONAL</i> Regular expressions that are searched for in
	 *            the sequence.
	 * @param organismClass
	 *            <i>OPTIONAL</i>. A class of organisms (saved in protein table
	 *            for faster access).
	 * @param speciesRegex
	 *            <i>OPTIONAL</i>. A regular expression that is searched for in
	 *            the proteins' species
	 * @param keywordSearch
	 *            <i>OPTIONAL</i>. A regular expression that is searched for in
	 *            the proteins' annotations.
	 * @param phosphoSites
	 *            Number of phosphorylated positions (if pi- or mw-limits are
	 *            given!).
	 * @param piFrom
	 *            <i>OPTIONAL</i>. Lower limit of pI.
	 * @param piTo
	 *            <i>OPTIONAL</i>. Upper limit of pI.
	 * @param mwFrom
	 *            <i>OPTIONAL</i>. Lower limit of molecular weight.
	 * @param mwTo
	 *            <i>OPTIONAL</i>. Upper limit of molecular weight.
	 * @param getWithSequence
	 *            TRUE, if the proteins in the result list should contains
	 *            sequences, FALSE otherwise.
	 * @return
	 * @throws DataAccessException
	 */
	public abstract List<Protein> get(DataSource dataSource,
			RestrictionProperties restrictionProperties,
			boolean getWithSequence, boolean getWithInformation)
			throws DataAccessException;

	public abstract List<String> getAccessionsStartingWith(
			String accessionContains, DataSource dataSource,
			int maxSuggestionsProteinAccessions) throws DataAccessException;

}