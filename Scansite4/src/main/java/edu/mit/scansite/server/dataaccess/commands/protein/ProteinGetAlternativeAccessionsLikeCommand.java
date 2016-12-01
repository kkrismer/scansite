package edu.mit.scansite.server.dataaccess.commands.protein;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class ProteinGetAlternativeAccessionsLikeCommand extends
		DbQueryCommand<ArrayList<String>> {

	private String accessionContains;
	private DataSource dataSource;
	private int maxSuggestionsProteinAccessions = -1;
	private int accessionAnnotationId = -1;

	public ProteinGetAlternativeAccessionsLikeCommand(
			Properties dbAccessConfig, Properties dbConstantsConfig,
			DbConnector dbConnector, String accessionContains,
			boolean beginningOnly, DataSource dataSource,
			int accessionAnnotationId, int maxSuggestionsProteinAccessions) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.accessionContains = (beginningOnly ? "" : "%") + accessionContains
				+ "%";
		this.dataSource = dataSource;
		this.accessionAnnotationId = accessionAnnotationId;
		this.maxSuggestionsProteinAccessions = maxSuggestionsProteinAccessions;
	}

	@Override
	protected ArrayList<String> doProcessResults(ResultSet result)
			throws DataAccessException {
		ArrayList<String> accs = new ArrayList<String>();
		try {
			while (result.next()) {
				accs.add(result.getString(c.getcAnnotationsAnnotation()));
			}
		} catch (SQLException e) {
			throw new DataAccessException(
					"Error fetching protein accessions from database!", e);
		}
		return accs;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(
				c.getcAnnotationsAnnotation());
		sql.append(CommandConstants.FROM).append(c.gettAnnotations(dataSource));
		if (accessionContains != null && !accessionContains.isEmpty()) {
			sql.append(CommandConstants.WHERE)
					.append(c.getcAnnotationTypesId())
					.append(CommandConstants.EQ).append(accessionAnnotationId);
			sql.append(CommandConstants.AND)
					.append(c.getcAnnotationsAnnotation())
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
