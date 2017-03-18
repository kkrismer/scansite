package edu.mit.scansite.server.dataaccess;

import java.util.List;

import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Konstantin Krismer
 */
public interface HistogramDao extends Dao {

	/**
	 * Adds the given histogram to the database. If the motif has not yet been
	 * added to the database (ie, it does not have an id set), it is also added
	 * to the database.
	 * 
	 * @param histogram
	 *            The histogram.
	 */
	public abstract void add(ServerHistogram histogram)
			throws DataAccessException;

	/**
	 * Deletes all histograms that match the given pair of motif, dataSource,
	 * and taxon. Nothing happens if no valid motif is given (NULL or no id
	 * set)! The parameters taxon and dataSource are optional.
	 * 
	 * @param motif
	 *            A valid motif.
	 * @param datasource
	 *            A valid dataSource (optional).
	 * @param taxon
	 *            A valid taxon (optional).
	 */
	public abstract void deleteHistograms(Motif motif, DataSource dataSource,
			Taxon taxon) throws DataAccessException;

	/**
	 * A more performant way of retrieving histograms.
	 * 
	 * @param motifs
	 *            A set of motifs, or NULL.
	 * @param dataSource
	 *            A data source with id or null. If this is given, a taxonId is
	 *            also needed.
	 * @param taxonId
	 *            A taxon's id. if this is given, a datasourceShortName is also
	 *            needed.
	 * @return A list of histograms that match the given parameters.
	 */
	public abstract List<ServerHistogram> getHistograms(List<Motif> motifs,
			DataSource dataSource, int taxonId) throws DataAccessException;

	/**
	 * Returns the histogram that matches the given parameters
	 * motif/datasource/taxon. All parameters are required (param > 0),
	 * otherwise null is returned.
	 * 
	 * @param motifId
	 *            A motifId.
	 * @param taxonName
	 *            A taxon's name.
	 * @param datasourceShortName
	 *            the taxon's datasource's shortname.
	 * @return The histogram that match the given parameters or NULL if there is
	 *         no histogram that matches the given parameters or in case not all
	 *         parameters are given.
	 * @throws DataAccessException
	 */
	public abstract ServerHistogram getHistogram(int motifId, String taxonName,
			DataSource dataSource) throws DataAccessException;

	/**
	 * Updates the given histogram in the database.
	 * 
	 * @param hist
	 *            The histogram to update.
	 * @param updateData
	 *            TRUE if the histogram's data also needs an update, otherwise
	 *            FALSE.
	 * @throws DataAccessException
	 */
	public abstract void updateHistogram(ServerHistogram hist,
			boolean updateData) throws DataAccessException;

	public abstract Double getThresholdValue(int motifId, int taxonId,
			DataSource dataSource, HistogramStringency stringency)
			throws DataAccessException;

}