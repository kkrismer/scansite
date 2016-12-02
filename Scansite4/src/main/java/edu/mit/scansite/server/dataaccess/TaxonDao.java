package edu.mit.scansite.server.dataaccess;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Konstantin Krismer
 */
public interface TaxonDao extends Dao {

	public abstract void setUseTempTablesForUpdate(
			boolean useTempTablesForUpdate);

	/**
	 * @param t
	 *            The taxon to add.
	 * @param databaseShortName
	 *            the database the taxon is from.
	 * @return The newly added taxon's primary key / id, or -1, if adding was
	 *         not successful.
	 * @throws DataAccessException
	 */
	public abstract int add(Taxon t, DataSource dataSource)
			throws DataAccessException;

	/**
	 * @param name
	 *            The taxon's name.
	 * @param databaseShortName
	 *            the database the taxon is from.
	 * @return The taxon.
	 * @throws DataAccessException
	 */
	public abstract Taxon getByName(String name, DataSource dataSource)
			throws DataAccessException;

	/**
	 * @param id
	 *            The taxon's id.
	 * @param databaseShortName
	 *            the database the taxon is from.
	 * @return The taxon.
	 * @throws DataAccessException
	 */
	public abstract Taxon getById(int id, DataSource dataSource)
			throws DataAccessException;

	/**
	 * Removes a taxon from the database.
	 * 
	 * @param name
	 *            The taxon's name.
	 * @param databaseShortName
	 *            the database the taxon is from.
	 * @throws DataAccessException
	 */
	public abstract void delete(String name, DataSource dataSource)
			throws DataAccessException;

	/**
	 * Removes a taxon from the database.
	 * 
	 * @param id
	 *            The taxon's id.
	 * @param databaseShortName
	 *            the database the taxon is from.
	 * @throws DataAccessException
	 */
	public abstract void delete(int id, DataSource dataSource)
			throws DataAccessException;

	/**
	 * @param databaseShortName
	 *            the database the taxon is from.
	 * @return A list that contains all the taxon's that are available in the
	 *         database. The parentTaxon fields are not fully set, ie. just the
	 *         ids of the parent taxa are given.
	 * @throws DataAccessException
	 */
	public abstract ArrayList<Taxon> getAllTaxa(DataSource dataSource)
			throws DataAccessException;

	/**
	 * @param taxon
	 *            The taxon one is interested in (all fields set!).
	 * @param databaseShortName
	 *            the database the taxon is from.
	 * @return A list of all taxon ids that describe this taxon or subtaxa of
	 *         this taxon.
	 * @throws DataAccessException
	 */
	public abstract Set<Integer> getSubTaxaIds(Taxon taxon,
			DataSource dataSource) throws DataAccessException;

	public abstract Set<Integer> getAllTaxonIds(DataSource dataSource)
			throws DataAccessException;

	public abstract Set<Integer> getAllTaxonIds(DataSource dataSource,
			String speciesRegex) throws DataAccessException;

	public abstract Map<Integer, Taxon> getSpeciesMap(DataSource dataSource,
			boolean b) throws DataAccessException;

}