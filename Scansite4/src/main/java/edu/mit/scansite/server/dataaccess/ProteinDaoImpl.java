package edu.mit.scansite.server.dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.commands.datasource.DataSourceEntryCountGetCommand;
import edu.mit.scansite.server.dataaccess.commands.protein.ProteinAddCommand;
import edu.mit.scansite.server.dataaccess.commands.protein.ProteinGetAccessionsLikeCommand;
import edu.mit.scansite.server.dataaccess.commands.protein.ProteinGetAllCommand;
import edu.mit.scansite.server.dataaccess.commands.protein.ProteinGetCommand;
import edu.mit.scansite.server.dataaccess.commands.protein.ProteinGetRestrictedCommand;
import edu.mit.scansite.server.dataaccess.commands.protein.ProteinIdentifierGetAllCommand;
import edu.mit.scansite.server.dataaccess.commands.protein.ProteinsGetCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinDaoImpl extends DaoImpl implements ProteinDao {

	public ProteinDaoImpl(Properties dbAccessConfig, Properties dbConstantsConfig,
			DbConnector dbConnector) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.ProteinDao#setUseTempTablesForUpdate(boolean)
	 */
	@Override
	public void setUseTempTablesForUpdate(boolean useTempTablesForUpdate) {
		this.useTempTablesForUpdate = useTempTablesForUpdate;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.ProteinDao#getAll(edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public List<Protein> getAll(DataSource dataSource)
			throws DataAccessException {
		ProteinGetAllCommand cmd = new ProteinGetAllCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, useTempTablesForUpdate,
				dataSource);
		List<Protein> proteins = new LinkedList<Protein>();
		try {
			proteins = cmd.execute();
			proteins = getProteinInformation(proteins, dataSource);
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return proteins;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.ProteinDao#getAllIdentifiers(edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public List<String> getAllIdentifiers(DataSource dataSource)
			throws DataAccessException {
		ProteinIdentifierGetAllCommand cmd = new ProteinIdentifierGetAllCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector,
				useTempTablesForUpdate, dataSource);
		List<String> proteinIdentifiers;
		try {
			proteinIdentifiers = cmd.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return proteinIdentifiers;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.ProteinDao#getAll(edu.mit.scansite.shared.transferobjects.Taxon, edu.mit.scansite.shared.transferobjects.DataSource, boolean)
	 */
	@Override
	public List<Protein> getAll(Taxon taxon, DataSource dataSource,
			boolean withProteinInfo) throws DataAccessException {
		try {
			Set<Integer> taxIds = ServiceLocator.getInstance()
					.getDaoFactory(dbConnector).getTaxonDao()
					.getSubTaxaIds(taxon, dataSource);
			ProteinGetAllCommand cmd = new ProteinGetAllCommand(dbAccessConfig,
					dbConstantsConfig, dbConnector, taxIds,
					useTempTablesForUpdate, dataSource);
			List<Protein> proteins = new ArrayList<Protein>();
			proteins = cmd.execute();
			if (withProteinInfo) {
				proteins = getProteinInformation(proteins, dataSource);
			}
			return proteins;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.ProteinDao#getProteinInformation(java.util.List, edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public List<Protein> getProteinInformation(List<Protein> ps,
			DataSource dataSource) throws DataAccessException {
		return getProteinInformation(ps, dataSource, null);
	}

	private List<Protein> getProteinInformation(List<Protein> ps,
			DataSource dataSource, String regex) throws DataAccessException {
		Map<Integer, Taxon> taxa = new HashMap<Integer, Taxon>();
		try {
			DaoFactory fac = ServiceLocator.getInstance().getDaoFactory(
					dbConnector);
			TaxonDao taxonDao = fac.getTaxonDao();
			taxonDao.setUseTempTablesForUpdate(useTempTablesForUpdate);

			AnnotationDao annDao = fac.getAnnotationDao();
			annDao.setUseTempTablesForUpdate(useTempTablesForUpdate);

			taxa = taxonDao.getSpeciesMap(dataSource, true);

			ArrayList<Protein> protPart = new ArrayList<Protein>();
			ArrayList<Protein> filtered = new ArrayList<Protein>();
			for (int i = 0; i < ps.size(); ++i) { // get in chunks of 500
													// proteins
				protPart.add(ps.get(i));
				if (protPart.size() == 2000 || i == ps.size() - 1) {
					List<Protein> temp = annDao.getForAllProteins(protPart,
							dataSource, regex);
					if (temp != null) {
						filtered.addAll(temp);
					}
					protPart.clear();
				}
			}
			ps = filtered;
			for (Protein p : ps) {
				Taxon t = p.getSpecies();
				// set datasource
				p.setDataSource(dataSource);

				// get and set Taxon/Species
				if (taxa.containsKey(t.getId())) {
					t = taxa.get(t.getId());
				} else {
					t = taxonDao.getById(t.getId(), dataSource);
					p.setSpecies(t);
					taxa.put(t.getId(), t);
				}
			}
			return ps;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.ProteinDao#getByDatasourceAndTaxon(edu.mit.scansite.shared.transferobjects.DataSource, int)
	 */
	@Override
	public List<Protein> getByDatasourceAndTaxon(DataSource dataSource,
			int taxonId) throws DataAccessException {
		ProteinGetCommand cmd = new ProteinGetCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, taxonId, dataSource,
				useTempTablesForUpdate);
		List<Protein> ps = null;
		try {
			ps = cmd.execute();
			ps = getProteinInformation(ps, dataSource);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return ps;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.ProteinDao#get(java.lang.String, edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public Protein get(String identifier, DataSource dataSource)
			throws DataAccessException {
		ProteinGetCommand cmd = new ProteinGetCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, identifier, dataSource,
				useTempTablesForUpdate);
		List<Protein> ps = null;
		try {
			if (identifier != null) {
				ps = cmd.execute();

				// NOTE: several queries are faster than a single multi-table
				// query!!!!
				if (ps.isEmpty()) {// no protein with the given primary
									// accessionNumber
					identifier = ServiceLocator.getInstance()
							.getDaoFactory(dbConnector).getAnnotationDao()
							.getProteinAccessionNr(identifier, dataSource);
					if (identifier == null) {
						return null;
					}
					cmd = new ProteinGetCommand(dbAccessConfig,
							dbConstantsConfig, dbConnector, identifier,
							dataSource, useTempTablesForUpdate);
					ps = cmd.execute();
				}

				ps = getProteinInformation(ps, dataSource);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return (ps.size() > 0) ? ps.get(0) : null;
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.ProteinDao#get(java.util.List, edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public List<Protein> get(List<String> identifiers, DataSource dataSource)
			throws DataAccessException {
		ProteinsGetCommand cmd = new ProteinsGetCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, identifiers, dataSource,
				useTempTablesForUpdate);
		try {
			if (identifiers != null && identifiers.size() > 0) {
				return getProteinInformation(cmd.execute(), dataSource);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.ProteinDao#add(edu.mit.scansite.shared.transferobjects.Protein, boolean, edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public void add(Protein p, boolean doAddAnnotations, DataSource dataSource)
			throws DataAccessException {
		DaoFactory fac = ServiceLocator.getInstance()
				.getDaoFactory(dbConnector);

		// add taxon if necessary
		TaxonDao taxonDao = fac.getTaxonDao();
		taxonDao.setUseTempTablesForUpdate(useTempTablesForUpdate);
		Taxon taxon = p.getSpecies();
		int taxonId = -1;
		if (taxon != null) {
			taxonId = taxon.getId();
			if (taxonId < 0) {
				taxon = taxonDao.getByName(taxon.getName(), dataSource);
				if (taxon == null) {
					taxon = p.getSpecies();
					taxonId = taxonDao.add(taxon, dataSource);
					taxon.setId(taxonId);
				}
				p.setSpecies(taxon);
			}
		} else {
			throw new DataAccessException(
					"Can not add taxon to database (protein: "
							+ p.getIdentifier() + ").");
		}

		// add datasource if necessary
		DataSourceDao dsDao = fac.getDataSourceDao();
		DataSource datasource = p.getDataSource();
		int datasourceId = -1;
		if (datasource != null) {
			datasourceId = datasource.getId();
			if (datasourceId < 0) {
				datasource = dsDao.get(datasource.getShortName());
				if (datasource == null) {
					datasource = p.getDataSource();
					datasourceId = dsDao.addOrUpdate(datasource);
					datasource.setId(datasourceId);
				}
				p.setDataSource(datasource);
			}
		} else {
			throw new DataAccessException(
					"Can not add datasource to database (protein: "
							+ p.getIdentifier() + ").");
		}

		ProteinAddCommand cmd = new ProteinAddCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, p, taxonId,
				useTempTablesForUpdate, dataSource);
		try {
			cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		if (doAddAnnotations) {
			AnnotationDao annoDao = fac.getAnnotationDao();
			HashMap<String, Set<String>> annotations = p.getAnnotations();
			if (annotations != null) {
				annoDao.addAnnotations(annotations, p.getIdentifier(),
						dataSource);
			} else {
				throw new DataAccessException(
						"Can not add annotations to database (protein: "
								+ p.getIdentifier() + ").");
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.ProteinDao#getProteinCount(edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public int getProteinCount(DataSource dataSource)
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
	 * @see edu.mit.scansite.server.dataaccess.ProteinDao#get(edu.mit.scansite.shared.transferobjects.DataSource, edu.mit.scansite.shared.transferobjects.RestrictionProperties, boolean, boolean)
	 */
	@Override
	public List<Protein> get(DataSource dataSource,
			RestrictionProperties restrictionProperties,
			boolean getWithSequence, boolean getWithInformation)
			throws DataAccessException {
		Set<Integer> taxaIds = ServiceLocator
				.getInstance()
				.getDaoFactory(dbConnector)
				.getTaxonDao()
				.getAllTaxonIds(dataSource,
						restrictionProperties.getSpeciesRegEx());
		if (taxaIds == null || taxaIds.isEmpty()) {
			return new LinkedList<Protein>();
		}
		ProteinGetRestrictedCommand cmd = new ProteinGetRestrictedCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector, dataSource,
				restrictionProperties.getSequenceRegEx(),
				restrictionProperties.getOrganismClass(), taxaIds,
				restrictionProperties.getPhosphorylatedSites(),
				restrictionProperties.getIsoelectricPointFrom(),
				restrictionProperties.getIsoelectricPointTo(),
				restrictionProperties.getMolecularWeightFrom(),
				restrictionProperties.getMolecularWeightTo(), getWithSequence);
		try {
			List<Protein> proteins = cmd.execute();
			proteins = getProteinInformation(proteins, dataSource,
					restrictionProperties.getKeywordRegEx());
			if (proteins != null && !proteins.isEmpty()
					&& restrictionProperties.getKeywordRegEx() != null
					&& !restrictionProperties.getKeywordRegEx().isEmpty()) {
				proteins = getProteinInformation(proteins, dataSource);
			}
			return proteins;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.scansite.server.dataaccess.ProteinDao#getAccessionsStartingWith(java.lang.String, edu.mit.scansite.shared.transferobjects.DataSource, int)
	 */
	@Override
	public List<String> getAccessionsStartingWith(String accessionContains,
			DataSource dataSource, int maxSuggestionsProteinAccessions)
			throws DataAccessException {
		String acc = accessionContains.replace("_", "\\_");
		ProteinGetAccessionsLikeCommand cmd = new ProteinGetAccessionsLikeCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector, acc, true,
				dataSource, maxSuggestionsProteinAccessions);
		// TOO expensive
		// AnnotationDao annDao = ServiceLocator.getInstance()
		// .getDaoFactory(dbConnector).getAnnotationDao();
		// ProteinGetAlternativeAccessionsLikeCommand cmd2 = new
		// ProteinGetAlternativeAccessionsLikeCommand(
		// dbAccessConfig,
		// dbConstantsConfig,
		// dbConnector,
		// acc,
		// true,
		// dataSourceShortName,
		// annDao.getAnnotationTypeId(ScansiteConstants.ANNOTATION_ACCESSION),
		// maxSuggestionsProteinAccessions);
		try {
			return cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}
}
