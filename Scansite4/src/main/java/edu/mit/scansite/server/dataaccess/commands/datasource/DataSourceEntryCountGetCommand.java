package edu.mit.scansite.server.dataaccess.commands.datasource;

import java.sql.ResultSet;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DataSourceEntryCountGetCommand extends DbQueryCommand<Integer> {
	private DataSource dataSource = null;

	public DataSourceEntryCountGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		this.dataSource = dataSource;
	}

	@Override
	protected Integer doProcessResults(ResultSet result)
			throws DataAccessException {
		try {
			if (result.next()) {
				return result.getInt(1);
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return -1;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		// SELECT COUNT(*) FROM `orthologs_swissprotorthology`
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(CommandConstants.count("*"))
				.append(CommandConstants.FROM);
		if (dataSource.getType().getShortName().equals("proteins")) {
			sql.append(c.getProteinsTableName(dataSource));
		} else if (dataSource.getType().getShortName().equals("orthologs")) {
			sql.append(c.getOrthologsTableName(dataSource));
		} else if (dataSource.getType().getShortName().equals("localization")) {
			sql.append(c.getLocalizationTableName(dataSource));
		}

		return sql.toString();
	}
}
