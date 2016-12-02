package edu.mit.scansite.server.dataaccess.commands.datasourcetype;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSourceType;

/**
 * @author Konstantin Krismer
 */
public class DataSourceTypeUpdateCommand extends DbUpdateCommand {
	private DataSourceType dataSourceType;

	public DataSourceTypeUpdateCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			DataSourceType dataSourceType) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.dataSourceType = dataSourceType;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.UPDATE).append(c.gettDataSourceTypes());
		sql.append(CommandConstants.SET);
		sql.append(c.getcDataSourceTypesShortName())
				.append(CommandConstants.EQ)
				.append(CommandConstants.enquote(dataSourceType.getShortName()))
				.append(CommandConstants.COMMA);
		sql.append(c.getcDataSourceTypesDisplayName())
				.append(CommandConstants.EQ)
				.append(CommandConstants.enquote(dataSourceType
						.getDisplayName()));
		sql.append(CommandConstants.WHERE);
		sql.append(c.getcDataSourceTypesId()).append(CommandConstants.EQ)
				.append(dataSourceType.getId());
		return sql.toString();
	}
}
