package edu.mit.scansite.server.dataaccess.commands.datasource;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DataSourceEntryCountsGetCommand extends
		DbQueryCommand<Map<DataSource, Integer>> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private List<DataSource> dataSources = null;

	public DataSourceEntryCountsGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, List<DataSource> dataSources) {
		super(dbAccessConfig, dbConstantsConfig);
		this.dataSources = dataSources;
	}

	@Override
	protected Map<DataSource, Integer> doProcessResults(ResultSet result)
			throws DataAccessException {
		Map<DataSource, Integer> dataSourcesSizes = new HashMap<>();
		try {
			while (result.next()) {
				String shortName = getShortName(result.getString("table_name"));
				int size = result.getInt("TABLE_ROWS");
				addSizeToDataSourceMetaInfo(dataSourcesSizes, shortName, size);
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return dataSourcesSizes;
	}

	private void addSizeToDataSourceMetaInfo(
			Map<DataSource, Integer> dataSourcesSizes, String shortName,
			int size) {
		for (DataSource dataSource : dataSources) {
			if (dataSource.getShortName().equalsIgnoreCase(shortName)) {
				dataSourcesSizes.put(dataSource, size);
				return;
			}
		}
	}

	private String getShortName(String tableName) {
		return tableName.split("_")[1];
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		// SELECT table_name, TABLE_ROWS FROM INFORMATION_SCHEMA.TABLES WHERE
		// table_name IN ('proteins_genpept', 'orthologs_homologene')
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append("table_name, TABLE_ROWS")
				.append(CommandConstants.FROM)
				.append(" INFORMATION_SCHEMA.TABLES ")
				.append(CommandConstants.WHERE).append(" table_name ")
				.append(CommandConstants.IN).append(CommandConstants.LPAR);
		for (int i = 0; i < dataSources.size(); ++i) {
			if (i == 0) {
				sql.append("'" + getTableName(dataSources.get(i)) + "'");
			} else {
				sql.append(", '" + getTableName(dataSources.get(i)) + "'");
			}
		}
		sql.append(CommandConstants.RPAR);
		return sql.toString();
	}

	private String getTableName(DataSource dataSource) {
		if (dataSource.getType().getShortName().equals("proteins")) {
			return c.getProteinsTableName(dataSource);
		} else if (dataSource.getType().getShortName().equals("orthologs")) {
			return c.getOrthologsTableName(dataSource);
		} else if (dataSource.getType().getShortName().equals("localization")) {
			return c.getLocalizationTableName(dataSource);
		} else {
			logger.warn("Unknown data source type: "
					+ dataSource.getType().getShortName() + " of data source "
					+ dataSource.getShortName());
			return dataSource.getShortName();
		}
	}
}
