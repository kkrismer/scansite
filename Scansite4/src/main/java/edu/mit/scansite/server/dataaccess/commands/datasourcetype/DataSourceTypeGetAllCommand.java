package edu.mit.scansite.server.dataaccess.commands.datasourcetype;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSourceType;

/**
 * @author Konstantin Krismer
 */
public class DataSourceTypeGetAllCommand extends
		DbQueryCommand<List<DataSourceType>> {
	public DataSourceTypeGetAllCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	@Override
	protected List<DataSourceType> doProcessResults(ResultSet result)
			throws DataAccessException {
		List<DataSourceType> dataSourceTypes = new LinkedList<DataSourceType>();
		try {
			while (result.next()) {
				dataSourceTypes.add(new DataSourceType(result.getInt(c
						.getcDataSourceTypesId()), result.getString(c
						.getcDataSourceTypesShortName()), result.getString(c
						.getcDataSourceTypesDisplayName())));
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return dataSourceTypes;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		// SELECT * FROM `datasources` INNER JOIN datasourcetypes USING
		// (dataSourceTypesId) INNER JOIN identifiertypes USING
		// (identifierTypesId)
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append('*')
				.append(CommandConstants.FROM).append(c.gettDataSourceTypes())
				.append(CommandConstants.ORDERBY)
				.append(c.getcDataSourceTypesId());
		return sql.toString();
	}
}
