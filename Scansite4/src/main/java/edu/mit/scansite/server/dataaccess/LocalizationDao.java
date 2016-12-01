package edu.mit.scansite.server.dataaccess;

import java.util.List;
import java.util.Map;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightLocalization;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Localization;
import edu.mit.scansite.shared.transferobjects.LocalizationType;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Konstantin Krismer
 */
public interface LocalizationDao extends Dao {

	public abstract void setUseTempTablesForUpdate(
			boolean useTempTablesForUpdate);

	public abstract void addLocalizationType(DataSource localizationDataSource,
			LocalizationType type) throws DataAccessException;

	public abstract void addLocalization(DataSource localizationDataSource,
			LightWeightProtein protein, Localization localization)
			throws DataAccessException;

	public abstract Localization retrieveLocalization(
			DataSource localizationDataSource, LightWeightProtein protein)
			throws DataAccessException;

	public abstract Map<LightWeightProtein, LightWeightLocalization> retrieveLocalizations(
			DataSource localizationDataSource, List<LightWeightProtein> proteins)
			throws DataAccessException;

	public abstract Map<Motif, LightWeightLocalization> retrieveLocalizationsForMotifs(
			DataSource localizationDataSource, List<Motif> motifs)
			throws DataAccessException;

	public abstract Localization retrieveLocalizationForMotif(
			DataSource localizationDataSource, Motif motif)
			throws DataAccessException;

	public abstract List<LocalizationType> retrieveLocalizationTypes(
			DataSource localizationDataSource) throws DataAccessException;

	public abstract int getLocalizationCount(DataSource dataSource)
			throws DataAccessException;

}