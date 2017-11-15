package edu.mit.scansite.server.dataaccess.commands.localization;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightLocalization;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.LocalizationType;

/**
 * @author Konstantin Krismer
 */
public class LocalizationGetAllCommand extends
		DbQueryCommand<Map<LightWeightProtein, LightWeightLocalization>> {
	private DataSource localizationDataSource;
	private List<LightWeightProtein> proteins;
	private Map<String, LightWeightProtein> identifierToProteinLUT;

	public LocalizationGetAllCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig,
			DataSource localizationDataSource, List<LightWeightProtein> proteins) {
		super(dbAccessConfig, dbConstantsConfig);
		this.localizationDataSource = localizationDataSource;
		this.proteins = proteins;
		identifierToProteinLUT = generateLUT(proteins);
	}

	private Map<String, LightWeightProtein> generateLUT(
			List<LightWeightProtein> proteins) {
		Map<String, LightWeightProtein> lookUp = new HashMap<String, LightWeightProtein>();
		for (LightWeightProtein protein : proteins) {
			lookUp.put(protein.getIdentifier(), protein);
		}
		return lookUp;
	}

	@Override
	protected Map<LightWeightProtein, LightWeightLocalization> doProcessResults(
			ResultSet result) throws DataAccessException {
		Map<LightWeightProtein, LightWeightLocalization> localizations = null;

		try {
			while (result.next()) {
				if (localizations == null) {
					localizations = new HashMap<LightWeightProtein, LightWeightLocalization>();
				}
				LightWeightLocalization localization = new LightWeightLocalization();
				LocalizationType type = new LocalizationType(result.getInt(c
						.getcLocalizationTypesId()), result.getString(c
						.getcLocalizationTypesName()));
				localization.setId(result.getInt(c.getcLocalizationId()));
				localization.setType(type);
				localization.setScore(result.getInt(c.getcLocalizationScore()));
				localizations.put(identifierToProteinLUT.get(result.getString(c
						.getcProteinsIdentifier())), localization);
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
		for (int i = 0; i < proteins.size(); ++i) {
			sql.append(CommandConstants
					.enquote(proteins.get(i).getIdentifier()));
			if (i + 1 < proteins.size()) {
				sql.append(CommandConstants.COMMA);
			}
		}
		sql.append(CommandConstants.RPAR);

		return sql.toString();
	}
}
