package edu.mit.scansite.server.dataaccess.commands.localization;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.LightWeightLocalization;
import edu.mit.scansite.shared.transferobjects.LocalizationType;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Konstantin Krismer
 */
public class MotifLocalizationGetAllCommand extends
		DbQueryCommand<Map<Motif, LightWeightLocalization>> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private DataSource localizationDataSource;
	private Map<String, List<Motif>> identifierToMotifsLUT;

	public MotifLocalizationGetAllCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			DataSource localizationDataSource, List<Motif> motifs) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.localizationDataSource = localizationDataSource;
		identifierToMotifsLUT = generateLUT(motifs);
	}

	private Map<String, List<Motif>> generateLUT(List<Motif> motifs) {
		Map<String, List<Motif>> lookUp = new HashMap<String, List<Motif>>();
		for (Motif motif : motifs) {
			Identifier identifier;
			try {
				identifier = extractCompatibleIdentifier(
						localizationDataSource, motif);
				if (!lookUp.containsKey(identifier.getValue())) {
					lookUp.put(identifier.getValue(), new LinkedList<Motif>());
				}
				lookUp.get(identifier.getValue()).add(motif);
			} catch (DataAccessException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return lookUp;
	}

	private Identifier extractCompatibleIdentifier(
			DataSource localizationDataSource, Motif motif)
			throws DataAccessException {
		if (motif.getId() > 0 && motif.getIdentifiers() != null && !motif.getIdentifiers().isEmpty()) {
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

	@Override
	protected Map<Motif, LightWeightLocalization> doProcessResults(
			ResultSet result) throws DataAccessException {
		Map<Motif, LightWeightLocalization> localizations = null;

		try {
			while (result.next()) {
				if (localizations == null) {
					localizations = new HashMap<Motif, LightWeightLocalization>();
				}
				LightWeightLocalization localization = new LightWeightLocalization();
				LocalizationType type = new LocalizationType(result.getInt(c
						.getcLocalizationTypesId()), result.getString(c
						.getcLocalizationTypesName()));
				localization.setId(result.getInt(c.getcLocalizationId()));
				localization.setType(type);
				localization.setScore(result.getInt(c.getcLocalizationScore()));
				List<Motif> motifs = identifierToMotifsLUT.get(result
						.getString(c.getcProteinsIdentifier()));
				for (Motif motif : motifs) {
					localizations.put(motif, localization);
				}
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return localizations;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append('*')
				.append(CommandConstants.FROM)
				.append(c.gettLocalization(localizationDataSource))
				.append(CommandConstants.INNERJOIN)
				.append(c.gettLocalizationTypes())
				.append(CommandConstants.USING).append(CommandConstants.LPAR)
				.append(c.getcLocalizationTypesId())
				.append(CommandConstants.RPAR).append(CommandConstants.WHERE);
		sql.append(c.getcProteinsIdentifier()).append(CommandConstants.IN)
				.append(CommandConstants.LPAR);
		List<String> identifiers = new ArrayList<>(
				identifierToMotifsLUT.keySet());
		for (int i = 0; i < identifiers.size(); ++i) {
			sql.append(CommandConstants.enquote(identifiers.get(i)));
			if (i + 1 < identifiers.size()) {
				sql.append(CommandConstants.COMMA);
			}
		}
		sql.append(CommandConstants.RPAR);
		return sql.toString();
	}
}
