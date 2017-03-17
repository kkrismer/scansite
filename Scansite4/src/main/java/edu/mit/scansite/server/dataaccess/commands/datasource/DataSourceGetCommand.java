package edu.mit.scansite.server.dataaccess.commands.datasource;

import java.sql.ResultSet;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.DataSourceType;
import edu.mit.scansite.shared.transferobjects.IdentifierType;

/**
 * @author tobieh
 * @author Konstantin Krismer
 */
public class DataSourceGetCommand extends DbQueryCommand<DataSource> {

	private int id = -1;
	private String shortName = null;

	public DataSourceGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, int id) {
		super(dbAccessConfig, dbConstantsConfig);
		this.id = id;
	}

	public DataSourceGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig,
			String shortName) {
		super(dbAccessConfig, dbConstantsConfig);
		this.shortName = shortName;
	}

	@Override
	protected DataSource doProcessResults(ResultSet result)
			throws DataAccessException {
		DataSource dataSource = null;
		try {
			if (result.next()) {
				DataSourceType type = new DataSourceType(result.getInt(c
						.getcDataSourceTypesId()), result.getString(c
						.getcDataSourceTypesShortName()), result.getString(c
						.getcDataSourceTypesDisplayName()));
				IdentifierType identifierType = new IdentifierType(
						result.getInt(c.getcIdentifierTypesId()),
						result.getString(c.getcIdentifierTypesName()));
				dataSource = new DataSource(
						result.getInt(c.getcDataSourcesId()), type,
						identifierType, result.getString(c
								.getcDataSourcesShortName()),
						result.getString(c.getcDataSourcesDisplayName()),
						result.getString(c.getcDataSourcesDescription()),
						result.getString(c.getcDataSourcesVersion()),
						result.getDate(c.getcDataSourcesLastUpdate()),
						result.getBoolean(c.getcDataSourcesIsPrimary()));
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return dataSource;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append('*')
				.append(CommandConstants.FROM).append(c.gettDataSources())
				.append(CommandConstants.INNERJOIN)
				.append(c.gettDataSourceTypes()).append(CommandConstants.USING)
				.append(CommandConstants.LPAR)
				.append(c.getcDataSourceTypesId())
				.append(CommandConstants.RPAR)
				.append(CommandConstants.INNERJOIN)
				.append(c.gettIdentifierTypes()).append(CommandConstants.USING)
				.append(CommandConstants.LPAR)
				.append(c.getcIdentifierTypesId())
				.append(CommandConstants.RPAR).append(CommandConstants.WHERE);
		if (shortName != null) {
			sql.append(c.getcDataSourcesShortName())
					.append(CommandConstants.EQ)
					.append(CommandConstants.enquote(shortName));
		} else {
			sql.append(c.getcDataSourcesId()).append(CommandConstants.EQ)
					.append(id);
		}
		return sql.toString();
	}
}
