package edu.mit.scansite.server.dataaccess.commands.datasource;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DataSourceUpdateCommand extends DbUpdateCommand {
	private DataSource dataSource;

	public DataSourceUpdateCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		this.dataSource = dataSource;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.UPDATE).append(c.gettDataSources());
		sql.append(CommandConstants.SET);
		sql.append(c.getcDataSourceTypesId()).append(CommandConstants.EQ)
				.append(dataSource.getType().getId())
				.append(CommandConstants.COMMA);
		sql.append(c.getcIdentifierTypesId()).append(CommandConstants.EQ)
				.append(dataSource.getIdentifierType().getId())
				.append(CommandConstants.COMMA);
		sql.append(c.getcDataSourcesDescription()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(dataSource.getDescription()))
				.append(CommandConstants.COMMA);
		sql.append(c.getcDataSourcesDisplayName()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(dataSource.getDisplayName()))
				.append(CommandConstants.COMMA);
		sql.append(c.getcDataSourcesShortName()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(dataSource.getShortName()))
				.append(CommandConstants.COMMA);
		sql.append(c.getcDataSourcesIsPrimary()).append(CommandConstants.EQ)
				.append(dataSource.isPrimaryDataSource() ? 1 : 0)
				.append(CommandConstants.COMMA);
		sql.append(c.getcDataSourcesVersion()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(dataSource.getVersion()));
		sql.append(CommandConstants.WHERE);
		sql.append(c.getcDataSourcesId()).append(CommandConstants.EQ)
				.append(dataSource.getId());
		return sql.toString();
	}
}
