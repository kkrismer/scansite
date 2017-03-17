package edu.mit.scansite.server.dataaccess.commands.localization;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Localization;

/**
 * @author Konstantin Krismer
 */
public class LocalizationAddCommand extends DbInsertCommand {
	private DataSource localizationDataSource;
	private LightWeightProtein protein;
	private Localization localization;

	public LocalizationAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig,
			DataSource localizationDataSource, LightWeightProtein protein,
			Localization localization) {
		super(dbAccessConfig, dbConstantsConfig);
		this.localizationDataSource = localizationDataSource;
		this.protein = protein;
		this.localization = localization;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettLocalization(localizationDataSource);
	}

	@Override
	protected String getIdColumnName() {
		return c.getcLocalizationId();
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		
		sql.append(CommandConstants.INSERTINTO)
				.append(c.gettLocalization(localizationDataSource)).append('(')
				.append(c.getcProteinsIdentifier())
				.append(CommandConstants.COMMA)
				.append(c.getcLocalizationScore())
				.append(CommandConstants.COMMA)
				.append(c.getcLocalizationTypesId()).append(')')
				.append(CommandConstants.VALUES).append('(')
				.append(CommandConstants.enquote(protein.getIdentifier()))
				.append(CommandConstants.COMMA).append(localization.getScore())
				.append(CommandConstants.COMMA)
				.append(localization.getType().getId()).append(')');
		return sql.toString();
	}
}
