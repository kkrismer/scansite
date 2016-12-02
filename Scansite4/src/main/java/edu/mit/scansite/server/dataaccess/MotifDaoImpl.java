package edu.mit.scansite.server.dataaccess;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.commands.motif.MotifAddCommand;
import edu.mit.scansite.server.dataaccess.commands.motif.MotifCountGetCommand;
import edu.mit.scansite.server.dataaccess.commands.motif.MotifDataAddCommand;
import edu.mit.scansite.server.dataaccess.commands.motif.MotifDataDeleteCommand;
import edu.mit.scansite.server.dataaccess.commands.motif.MotifDataUpdateCommand;
import edu.mit.scansite.server.dataaccess.commands.motif.MotifDeleteCommand;
import edu.mit.scansite.server.dataaccess.commands.motif.MotifGetAllCommand;
import edu.mit.scansite.server.dataaccess.commands.motif.MotifGetCommand;
import edu.mit.scansite.server.dataaccess.commands.motif.MotifUpdateCommand;
import edu.mit.scansite.server.dataaccess.commands.motifidentifier.MotifIdentifierAddCommand;
import edu.mit.scansite.server.dataaccess.commands.motifidentifier.MotifIdentifierDeleteCommand;
import edu.mit.scansite.server.dataaccess.commands.motifidentifier.MotifIdentifierGetAllCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.MotifSelection;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifDaoImpl extends DaoImpl implements MotifDao {

	public MotifDaoImpl(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.MotifDao#addMotif(edu.mit.scansite
	 * .shared.transferobjects.Motif)
	 */
	@Override
	public int addMotif(Motif motif) throws DataAccessException {
		try {
			Motif temp = getByShortName(motif.getShortName());
			int id = -1;
			if (temp == null) {
				MotifAddCommand cmd = new MotifAddCommand(dbAccessConfig,
						dbConstantsConfig, dbConnector, motif);
				id = cmd.execute();
				if (id > 0) {
					motif.setId(id);
					for (int i = 0; i < ScansiteConstants.WINDOW_SIZE; ++i) {
						MotifDataAddCommand dataCmd = new MotifDataAddCommand(
								dbAccessConfig, dbConstantsConfig, dbConnector,
								motif, i);
						dataCmd.execute();
					}
					// adding identifiers
					try {
						for (Identifier identifier : motif.getIdentifiers()) {
							addMotifIdentifier(motif, identifier);
						}
					} catch (Exception e) {
						logger.error(
								"Could not add motif identifiers: "
										+ e.getMessage(), e);
					}
				} else {
					throw new DataAccessException("Adding motif failed.");
				}
			} else {
				motif = temp;
				id = temp.getId();
			}
			return id;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.MotifDao#getAll(edu.mit.scansite.shared
	 * .transferobjects.MotifClass, boolean)
	 */
	@Override
	public List<Motif> getAll(MotifClass motifClass, boolean publicOnly)
			throws DataAccessException {
		return getAll(null, motifClass, publicOnly);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.MotifDao#getAll(java.util.Set,
	 * edu.mit.scansite.shared.transferobjects.MotifClass, boolean)
	 */
	@Override
	public List<Motif> getAll(Set<String> motifNicks, MotifClass motifClass,
			boolean publicOnly) throws DataAccessException {
		if (motifClass == null) {
			motifClass = MotifClass.MAMMALIAN;
		}
		try {
			MotifGetAllCommand cmd = new MotifGetAllCommand(dbAccessConfig,
					dbConstantsConfig, dbConnector, motifNicks, motifClass,
					publicOnly);
			
			List<Motif> motifs = cmd.execute();
			List<LightWeightMotifGroup> groups = ServiceLocator.getInstance()
					.getDaoFactory(dbConnector).getGroupsDao()
					.getAllLightWeight();

			for (Motif m : motifs) {
				int groupId = m.getGroup().getId();
				for (int i = 0; i < groups.size() && groupId > 0; ++i) {
					if (groups.get(i).getId() == groupId) {
						groupId = -1;
						m.setGroup(groups.get(i));
					}
				}
			}

			fetchIdentifiers(motifs);

			return motifs;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.MotifDao#getSelectedMotifs(edu.mit
	 * .scansite.shared.transferobjects.MotifSelection, boolean)
	 */
	@Override
	public List<Motif> getSelectedMotifs(MotifSelection motifSelection,
			boolean publicOnly) throws DataAccessException {
		if (motifSelection.getUserMotif() != null) { // user motif
			List<Motif> motifs = new LinkedList<>();
			motifs.add(motifSelection.getUserMotif());
			return motifs;
		} else {
			return getAll(motifSelection.getMotifShortNames(),
					motifSelection.getMotifClass(), publicOnly);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.MotifDao#getByShortName(java.lang.
	 * String)
	 */
	@Override
	public Motif getByShortName(String shortName) throws DataAccessException {
		MotifGetCommand cmd = new MotifGetCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, shortName);
		try {
			Motif motif = cmd.execute();
			return motif;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.MotifDao#getByGroup(edu.mit.scansite
	 * .shared.transferobjects.LightWeightMotifGroup,
	 * edu.mit.scansite.shared.transferobjects.MotifClass, boolean)
	 */
	@Override
	public List<Motif> getByGroup(LightWeightMotifGroup group,
			MotifClass motifClass, boolean publicOnly)
			throws DataAccessException {
		MotifGetAllCommand cmd = new MotifGetAllCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, group.getId(), motifClass,
				publicOnly);
		try {
			List<Motif> motifs = cmd.execute();

			for (Motif m : motifs) {
				m.setGroup(group);
			}

			fetchIdentifiers(motifs);

			return motifs;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	private void fetchIdentifiers(List<Motif> motifs)
			throws DataAccessException {
		MotifIdentifierGetAllCommand cmd = new MotifIdentifierGetAllCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector);
		try {
			Map<Integer, List<Identifier>> motifIdentifiers = cmd.execute();

			for (Motif motif : motifs) {
				motif.setIdentifiers(motifIdentifiers.get(motif.getId()));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.MotifDao#updateMotif(edu.mit.scansite
	 * .shared.transferobjects.Motif)
	 */
	@Override
	public void updateMotif(Motif motif) throws DataAccessException {
		try {
			MotifIdentifierDeleteCommand cmd = new MotifIdentifierDeleteCommand(
					dbAccessConfig, dbConstantsConfig, dbConnector,
					motif.getId());
			cmd.execute();
		} catch (Exception e) {
			logger.error(
					"Could not delete old motif identifiers: " + e.getMessage(),
					e);
			throw new DataAccessException(e.getMessage(), e);
		}
		try {
			for (Identifier identifier : motif.getIdentifiers()) {
				addMotifIdentifier(motif, identifier);
			}
		} catch (Exception e) {
			logger.error(
					"Could not add new motif identifiers: " + e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		try {
			MotifUpdateCommand cmd = new MotifUpdateCommand(dbAccessConfig,
					dbConstantsConfig, dbConnector, motif);
			cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.MotifDao#deleteMotif(int)
	 */
	@Override
	public void deleteMotif(int id) throws DataAccessException {
		MotifDataDeleteCommand cmd1 = new MotifDataDeleteCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector, id);
		MotifIdentifierDeleteCommand cmd2 = new MotifIdentifierDeleteCommand(
				dbAccessConfig, dbConstantsConfig, dbConnector, id);

		MotifDeleteCommand cmd3 = new MotifDeleteCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, id);

		try {
			cmd1.execute();
			cmd2.execute();
			Motif m = new Motif();
			m.setId(id);
			ServiceLocator.getInstance().getDaoFactory(dbConnector)
					.getHistogramDao().deleteHistograms(m, null, null);
			cmd3.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.MotifDao#getById(int)
	 */
	@Override
	public Motif getById(int motifId) throws DataAccessException {
		MotifGetCommand cmd = new MotifGetCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, motifId);
		try {
			Motif motif = cmd.execute();
			return motif;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.MotifDao#updateMotifData(edu.mit.scansite
	 * .shared.transferobjects.Motif)
	 */
	@Override
	public void updateMotifData(Motif motif) throws DataAccessException {
		MotifUpdateCommand cmdMotif = new MotifUpdateCommand(dbAccessConfig,
				dbConstantsConfig, dbConnector, motif, true);
		try {
			cmdMotif.execute();
			for (int i = 0; i < ScansiteConstants.WINDOW_SIZE; ++i) {
				MotifDataUpdateCommand cmdMotifData = new MotifDataUpdateCommand(
						dbAccessConfig, dbConstantsConfig, dbConnector, motif,
						i);
				cmdMotifData.execute();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.MotifDao#getNumberOfMotifs(edu.mit
	 * .scansite.shared.transferobjects.MotifClass, boolean)
	 */
	@Override
	public Integer getNumberOfMotifs(MotifClass motifClass,
			boolean getOnlyPublic) throws DataAccessException {
		try {
			MotifCountGetCommand cmd = new MotifCountGetCommand(dbAccessConfig,
					dbConstantsConfig, dbConnector, motifClass, getOnlyPublic);
			return cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.MotifDao#addMotifIdentifier(edu.mit
	 * .scansite.shared.transferobjects.Motif,
	 * edu.mit.scansite.shared.transferobjects.Identifier)
	 */
	@Override
	public int addMotifIdentifier(Motif motif, Identifier identifier)
			throws DataAccessException {
		try {
			if (motif.getId() <= 0) {
				throw new DataAccessException(
						"cannot save motif identifier, motif is not in db");
			}
			if (identifier.getType() == null
					|| identifier.getType().getId() <= 0) {
				throw new DataAccessException(
						"cannot save motif identifier, identifier type is not in db");
			}
			MotifIdentifierAddCommand cmd = new MotifIdentifierAddCommand(
					dbAccessConfig, dbConstantsConfig, dbConnector, motif,
					identifier);
			return cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}
}
