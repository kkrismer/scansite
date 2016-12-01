package edu.mit.scansite.server.dataaccess;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.commands.datasource.DataSourceEntryCountGetCommand;
import edu.mit.scansite.server.dataaccess.commands.localization.GOTermEvidenceAddCommand;
import edu.mit.scansite.server.dataaccess.commands.localization.LocalizationAddCommand;
import edu.mit.scansite.server.dataaccess.commands.localization.LocalizationGetAllCommand;
import edu.mit.scansite.server.dataaccess.commands.localization.LocalizationGetCommand;
import edu.mit.scansite.server.dataaccess.commands.localization.MotifLocalizationGetAllCommand;
import edu.mit.scansite.server.dataaccess.commands.localizationtype.LocalizationTypeAddCommand;
import edu.mit.scansite.server.dataaccess.commands.localizationtype.LocalizationTypesGetAllCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.GOTermEvidence;
import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.LightWeightLocalization;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Localization;
import edu.mit.scansite.shared.transferobjects.LocalizationType;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.Protein;

/**
 * @author Konstantin Krismer
 */
public class LocalizationDaoImpl extends DaoImpl implements LocalizationDao {

	public LocalizationDaoImpl(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.LocalizationDao#setUseTempTablesForUpdate(boolean)
	 */
	@Override
	public void setUseTempTablesForUpdate(boolean useTempTablesForUpdate) {
		this.useTempTablesForUpdate = useTempTablesForUpdate;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.LocalizationDao#addLocalizationType(edu.mit.scansite.shared.transferobjects.DataSource, edu.mit.scansite.shared.transferobjects.LocalizationType)
	 */
	@Override
	public void addLocalizationType(DataSource localizationDataSource,
			LocalizationType type) throws DataAccessException {
		if (localizationDataSource != null
				&& localizationDataSource.getType().getShortName()
						.equals("localization")) {
			try {
				int id = type.getId();
				if (id < 0) {
					id = retrieveLocalizationTypeId(localizationDataSource,
							type.getName());
					if (id < 0) { // add to id
						LocalizationTypeAddCommand cmd = new LocalizationTypeAddCommand(
								dbAccessConfig, dbConstantsConfig, dbConnector,
								localizationDataSource, type);
						type.setId(cmd.execute());
					} else { // already in db
						type.setId(id);
					}
				} // otherwise do nothing
			} catch (DatabaseException e) {
				logger.error(e.getMessage(), e);
				throw new DataAccessException(e.getMessage(), e);
			}
		} else {
			logger.error("localization data source either null or invalid");
			throw new DataAccessException(
					"localization data source either null or invalid");
		}
	}

	private int retrieveLocalizationTypeId(DataSource localizationDataSource,
			String localizationTypeName) throws DataAccessException {
		List<LocalizationType> types = retrieveLocalizationTypes(localizationDataSource);
		if (types != null) {
			for (LocalizationType type : types) {
				if (type.getName().equalsIgnoreCase(localizationTypeName)) {
					return type.getId();
				}
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.LocalizationDao#addLocalization(edu.mit.scansite.shared.transferobjects.DataSource, edu.mit.scansite.shared.transferobjects.LightWeightProtein, edu.mit.scansite.shared.transferobjects.Localization)
	 */
	@Override
	public void addLocalization(DataSource localizationDataSource,
			LightWeightProtein protein, Localization localization)
			throws DataAccessException {
		if (localizationDataSource != null
				&& localizationDataSource.getType().getShortName()
						.equals("localization")) {
			if (localization != null && localization.getType() != null) {
				if (localization.getType().getId() <= 0) {
					addLocalizationType(localizationDataSource,
							localization.getType());
				}
				try {
					addLocalizationGOTerms(localization.getGoTerms());
					LocalizationAddCommand cmd = new LocalizationAddCommand(
							dbAccessConfig, dbConstantsConfig, dbConnector,
							localizationDataSource, protein, localization);
					localization.setId(cmd.execute());

					if (localization.getGoTerms() != null
							&& !localization.getGoTerms().isEmpty()) {
						for (GOTermEvidence goTerm : localization.getGoTerms()) {
							GOTermEvidenceAddCommand goTermCommand = new GOTermEvidenceAddCommand(
									dbAccessConfig, dbConstantsConfig,
									dbConnector, localizationDataSource,
									localization, goTerm);
							goTermCommand.execute();
						}
					}

				} catch (DatabaseException e) {
					logger.error(e.getMessage(), e);
					throw new DataAccessException(e.getMessage(), e);
				}
			} else {
				logger.error("localization type either null or invalid");
				throw new DataAccessException(
						"localization type either null or invalid");
			}
		} else {
			logger.error("localization data source either null or invalid");
			throw new DataAccessException(
					"localization data source either null or invalid");
		}
	}

	private void addLocalizationGOTerms(List<GOTermEvidence> goTerms)
			throws DataAccessException {
		if (goTerms != null && !goTerms.isEmpty()) {
			GOTermDao dao = ServiceLocator.getInstance()
					.getDaoFactory(dbConnector).getGOTermDao();
			for (GOTermEvidence goTerm : goTerms) {
				goTerm.setGoTerm(dao.addGOTerm(goTerm.getGoTerm()));
				if (goTerm.getEvidenceCode() != null) {
					goTerm.setEvidenceCode(dao.addEvidenceCode(goTerm
							.getEvidenceCode()));
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.LocalizationDao#retrieveLocalization(edu.mit.scansite.shared.transferobjects.DataSource, edu.mit.scansite.shared.transferobjects.LightWeightProtein)
	 */
	@Override
	public Localization retrieveLocalization(DataSource localizationDataSource,
			LightWeightProtein protein) throws DataAccessException {
		if (localizationDataSource != null
				&& localizationDataSource.getType().getShortName()
						.equals("localization")) {
			try {
				LocalizationGetCommand cmd = new LocalizationGetCommand(
						dbAccessConfig, dbConstantsConfig, dbConnector,
						localizationDataSource, protein);
				return cmd.execute();
			} catch (DatabaseException e) {
				logger.error(e.getMessage(), e);
				throw new DataAccessException(e.getMessage(), e);
			}
		} else {
			logger.error("localization data source either null or invalid");
			throw new DataAccessException(
					"localization data source either null or invalid");
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.LocalizationDao#retrieveLocalizations(edu.mit.scansite.shared.transferobjects.DataSource, java.util.List)
	 */
	@Override
	public Map<LightWeightProtein, LightWeightLocalization> retrieveLocalizations(
			DataSource localizationDataSource, List<LightWeightProtein> proteins)
			throws DataAccessException {
		if (localizationDataSource != null
				&& localizationDataSource.getType().getShortName()
						.equals("localization")) {
			try {
				LocalizationGetAllCommand cmd = new LocalizationGetAllCommand(
						dbAccessConfig, dbConstantsConfig, dbConnector,
						localizationDataSource, proteins);
				return cmd.execute();
			} catch (DatabaseException e) {
				logger.error(e.getMessage(), e);
				throw new DataAccessException(e.getMessage(), e);
			}
		} else {
			logger.error("localization data source either null or invalid");
			throw new DataAccessException(
					"localization data source either null or invalid");
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.LocalizationDao#retrieveLocalizationsForMotifs(edu.mit.scansite.shared.transferobjects.DataSource, java.util.List)
	 */
	@Override
	public Map<Motif, LightWeightLocalization> retrieveLocalizationsForMotifs(
			DataSource localizationDataSource, List<Motif> motifs)
			throws DataAccessException {
		if (localizationDataSource != null
				&& localizationDataSource.getType().getShortName()
						.equals("localization")) {
			try {
				MotifLocalizationGetAllCommand cmd = new MotifLocalizationGetAllCommand(
						dbAccessConfig, dbConstantsConfig, dbConnector,
						localizationDataSource, motifs);
				return cmd.execute();
			} catch (DatabaseException e) {
				logger.error(e.getMessage(), e);
				throw new DataAccessException(e.getMessage(), e);
			}
		} else {
			logger.error("localization data source either null or invalid");
			throw new DataAccessException(
					"localization data source either null or invalid");
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.LocalizationDao#retrieveLocalizationForMotif(edu.mit.scansite.shared.transferobjects.DataSource, edu.mit.scansite.shared.transferobjects.Motif)
	 */
	@Override
	public Localization retrieveLocalizationForMotif(
			DataSource localizationDataSource, Motif motif)
			throws DataAccessException {
		if (localizationDataSource != null
				&& localizationDataSource.getType().getShortName()
						.equals("localization")) {
			try {
				Identifier motifIdentifier = extractCompatibleIdentifier(
						localizationDataSource, motif);
				LightWeightProtein protein = new Protein();
				protein.setIdentifier(motifIdentifier.getValue());
				LocalizationGetCommand cmd = new LocalizationGetCommand(
						dbAccessConfig, dbConstantsConfig, dbConnector,
						localizationDataSource, protein);
				return cmd.execute();
			} catch (DatabaseException e) {
				logger.error(e.getMessage(), e);
				throw new DataAccessException(e.getMessage(), e);
			}
		} else {
			logger.error("localization data source either null or invalid");
			throw new DataAccessException(
					"localization data source either null or invalid");
		}
	}

	private Identifier extractCompatibleIdentifier(
			DataSource localizationDataSource, Motif motif)
			throws DataAccessException {
		if (motif.getId() > 0 && motif.getIdentifiers() != null
				&& !motif.getIdentifiers().isEmpty()) {
			for (Identifier identifier : motif.getIdentifiers()) {
				if (identifier.getType().getId() == localizationDataSource
						.getIdentifierType().getId()) {
					return identifier;
				}
			}
		}
		throw new DataAccessException(
				"no localization information: incompatible motif identifiers");
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.LocalizationDao#retrieveLocalizationTypes(edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public List<LocalizationType> retrieveLocalizationTypes(
			DataSource localizationDataSource) throws DataAccessException {
		if (localizationDataSource != null
				&& localizationDataSource.getType().getShortName()
						.equals("localization")) {
			try {
				LocalizationTypesGetAllCommand cmd = new LocalizationTypesGetAllCommand(
						dbAccessConfig, dbConstantsConfig, dbConnector,
						localizationDataSource);
				return cmd.execute();
			} catch (DatabaseException e) {
				logger.error(e.getMessage(), e);
				throw new DataAccessException(e.getMessage(), e);
			}
		} else {
			logger.error("localization data source either null or invalid");
			throw new DataAccessException(
					"localization data source either null or invalid");
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.LocalizationDao#getLocalizationCount(edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public int getLocalizationCount(DataSource dataSource)
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
}
