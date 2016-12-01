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
public class ProteinGetAccessionsLikeCommand extends
		DbQueryCommand<List<String>> {

	private String accessionContains;
	private DataSource dataSource;
	private int maxSuggestionsProteinAccessions = -1;

	public ProteinGetAccessionsLikeCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			String accessionContains, boolean beginningOnly,
			DataSource dataSource, int maxSuggestionsProteinAccessions) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.accessionContains = (beginningOnly ? "" : "%") + accessionContains
				+ "%";
		this.dataSource = dataSource;
		this.maxSuggestionsProteinAccessions = maxSuggestionsProteinAccessions;
	}

	@Override
	protected List<String> doProcessResults(ResultSet result)
			throws DataAccessException {
		List<String> accs = new LinkedList<String>();
		try {
			while (result.next()) {
				accs.add(result.getString(c.getcProteinsIdentifier()));
			}
		} catch (SQLException e) {
			throw new DataAccessException(
					"Error fetching protein accessions from database", e);
		}
		return accs;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcProteinsIdentifier());
		sql.append(CommandConstants.FROM).append(c.gettProteins(dataSource));
		if (accessionContains != null && !accessionContains.isEmpty()) {
			sql.append(CommandConstants.WHERE)
					.append(c.getcProteinsIdentifier())
					.append(CommandConstants.LIKE)
					.append(CommandConstants.enquote(accessionContains));
		}
		if (maxSuggestionsProteinAccessions > 0) {
			sql.append(CommandConstants.LIMIT).append(
					maxSuggestionsProteinAccessions);
		}
		return sql.toString();
	}
}
