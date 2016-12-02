package edu.mit.scansite.server.dataaccess.commands.datasourcetype;

import java.sql.ResultSet;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSourceType;

/**
 * @author Konstantin Krismer
 */
public class DataSourceTypeGetCommand extends DbQueryCommand<DataSourceType> {

	private int id = -1;
	private String shortName = null;

	public DataSourceTypeGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, int id) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.id = id;
	}

	public DataSourceTypeGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			String shortName) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.shortName = shortName;
	}

	@Override
	protected DataSourceType doProcessResults(ResultSet result)
			throws DataAccessException {
		DataSourceType dataSourceType = null;
		try {
			if (result.next()) {
				dataSourceType = new DataSourceType(result.getInt(c
						.getcDataSourceTypesId()), result.getString(c
						.getcDataSourceTypesShortName()), result.getString(c
						.getcDataSourceTypesDisplayName()));
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return dataSourceType;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append('*')
				.append(CommandConstants.FROM).append(c.gettDataSourceTypes())
				.append(CommandConstants.WHERE);
		if (shortName != null) {
			sql.append(c.getcDataSourceTypesShortName())
					.append(CommandConstants.EQ)
					.append(CommandConstants.enquote(shortName));
		} else {
			sql.append(c.getcDataSourceTypesId()).append(CommandConstants.EQ)
					.append(id);
		}
		return sql.toString();
	}
}
