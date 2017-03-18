package edu.mit.scansite.server.dataaccess.commands.protein;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
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
public class ProteinIdentifierGetAllCommand extends
		DbQueryCommand<List<String>> {
	private DataSource dataSource;

	public ProteinIdentifierGetAllCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(useTempTablesForUpdate);
		this.dataSource = dataSource;
	}

	@Override
	protected List<String> doProcessResults(ResultSet result)
			throws DataAccessException {
		List<String> proteinIdentifiers = new LinkedList<String>();
		try {
			while (result.next()) {
				proteinIdentifiers.add(result.getString(c
						.getcProteinsIdentifier()));
			}
		} catch (SQLException e) {
			throw new DataAccessException(
					"Error fetching protein identifiers from database", e);
		}
		return proteinIdentifiers;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcProteinsIdentifier());
		sql.append(CommandConstants.FROM).append(
				c.gettProteins(dataSource));
		return sql.toString();
	}
}
