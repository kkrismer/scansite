package edu.mit.scansite.server.dataaccess.commands.localizationtype;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LocalizationType;

/**
 * @author Konstantin Krismer
 */
public class LocalizationTypeAddCommand extends DbInsertCommand {
	private DataSource localizationTypeDataSource;
	private LocalizationType localizationType;

	public LocalizationTypeAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig,
            DataSource localizationTypeDataSource,
			LocalizationType localizationType) {
		super(dbAccessConfig, dbConstantsConfig);
		this.localizationTypeDataSource = localizationTypeDataSource;
		this.localizationType = localizationType;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettLocalizationTypes();
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.INSERTINTO)
				.append(c.gettLocalizationTypes()).append('(')
				.append(c.getcLocalizationTypesName())
				.append(CommandConstants.COMMA).append(c.getcDataSourcesId())
				.append(')').append(CommandConstants.VALUES).append('(')
				.append(CommandConstants.enquote(localizationType.getName()))
				.append(CommandConstants.COMMA)
				.append(localizationTypeDataSource.getId()).append(')');
		return sql.toString();
	}

	@Override
	protected String getIdColumnName() {
		return c.getcLocalizationTypesId();
	}
}
