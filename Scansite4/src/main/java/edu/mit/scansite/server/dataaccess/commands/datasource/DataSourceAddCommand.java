package edu.mit.scansite.server.dataaccess.commands.datasource;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DataSourceAddCommand extends DbInsertCommand {
	private DataSource dataSource = null;

	public DataSourceAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		this.dataSource = dataSource;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettDataSources();
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.INSERTINTO).append(c.gettDataSources())
				.append('(').append(c.getcDataSourceTypesId())
				.append(CommandConstants.COMMA)
				.append(c.getcIdentifierTypesId())
				.append(CommandConstants.COMMA)
				.append(c.getcDataSourcesShortName())
				.append(CommandConstants.COMMA)
				.append(c.getcDataSourcesDisplayName())
				.append(CommandConstants.COMMA)
				.append(c.getcDataSourcesDescription())
				.append(CommandConstants.COMMA)
				.append(c.getcDataSourcesVersion())
				.append(CommandConstants.COMMA)
				.append(c.getcDataSourcesIsPrimary()).append(')')
				.append(CommandConstants.VALUES).append('(')
				.append(dataSource.getType().getId())
				.append(CommandConstants.COMMA)
				.append(dataSource.getIdentifierType().getId())
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(dataSource.getShortName()))
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(dataSource.getDisplayName()))
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(dataSource.getDescription()))
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(dataSource.getVersion()))
				.append(CommandConstants.COMMA)
				.append(dataSource.isPrimaryDataSource() ? "1": "0").append(')');
		return sql.toString();
	}

	@Override
	protected String getIdColumnName() {
		return c.getcDataSourcesId();
	}
}
