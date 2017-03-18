package edu.mit.scansite.server.dataaccess.commands.datasourcetype;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSourceType;

/**
 * @author Konstantin Krismer
 */
public class DataSourceTypeAddCommand extends DbInsertCommand {
	private DataSourceType dataSourceType = null;

	public DataSourceTypeAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DataSourceType dataSourceType) {
		super(dbAccessConfig, dbConstantsConfig);
		this.dataSourceType = dataSourceType;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettDataSourceTypes();
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.INSERTINTO)
				.append(c.gettDataSourceTypes())
				.append('(')
				.append(c.getcDataSourceTypesId())
				.append(CommandConstants.COMMA)
				.append(c.getcDataSourceTypesShortName())
				.append(CommandConstants.COMMA)
				.append(c.getcDataSourceTypesDisplayName())
				.append(')')
				.append(CommandConstants.VALUES)
				.append('(')
				.append(dataSourceType.getId())
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(dataSourceType.getShortName()))
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(dataSourceType
						.getDisplayName())).append(')');
		return sql.toString();
	}

	@Override
	protected String getIdColumnName() {
		return null; // id is not set by database
	}
}
