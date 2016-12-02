package edu.mit.scansite.server.dataaccess.commands.datasource;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.DataSourceType;
import edu.mit.scansite.shared.transferobjects.IdentifierType;

/**
 * @author Konstantin Krismer
 */
public class DataSourcesByIdentifierTypeGetCommand extends
		DbQueryCommand<List<DataSource>> {
	private IdentifierType type;

	public DataSourcesByIdentifierTypeGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			IdentifierType type) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.type = type;
	}

	@Override
	protected List<DataSource> doProcessResults(ResultSet result)
			throws DataAccessException {
		List<DataSource> dataSources = new LinkedList<DataSource>();
		try {
			while (result.next()) {
				DataSourceType type = new DataSourceType(result.getInt(c
						.getcDataSourceTypesId()), result.getString(c
						.getcDataSourceTypesShortName()), result.getString(c
						.getcDataSourceTypesDisplayName()));
				IdentifierType identifierType = new IdentifierType(
						result.getInt(c.getcIdentifierTypesId()),
						result.getString(c.getcIdentifierTypesName()));
				dataSources.add(new DataSource(result.getInt(c
						.getcDataSourcesId()), type, identifierType, result
						.getString(c.getcDataSourcesShortName()), result
						.getString(c.getcDataSourcesDisplayName()), result
						.getString(c.getcDataSourcesDescription()), result
						.getString(c.getcDataSourcesVersion()), result
						.getDate(c.getcDataSourcesLastUpdate()), result
						.getBoolean(c.getcDataSourcesIsPrimary())));
			}
			return dataSources;
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
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
				.append(CommandConstants.RPAR).append(CommandConstants.WHERE)
				.append(c.getcIdentifierTypesId()).append(CommandConstants.EQ)
				.append(type.getId());
		return sql.toString();
	}
}
