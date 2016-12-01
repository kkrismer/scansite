package edu.mit.scansite.server.dataaccess.commands.goterm;

import java.sql.ResultSet;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.GOTerm;

/**
 * @author Konstantin Krismer
 */
public class GOTermGetCommand extends DbQueryCommand<GOTerm> {
	private String id;

	public GOTermGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, String id) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.id = id;
	}

	@Override
	protected GOTerm doProcessResults(ResultSet result)
			throws DataAccessException {
		try {
			GOTerm goTerm = null;
			if (result.next()) {
				goTerm = new GOTerm(result.getString(c
						.getcGOTermsId()), result.getString(c
						.getcGOTermsName()));
			}
			return goTerm;
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcGOTermsId())
				.append(CommandConstants.COMMA)
				.append(c.getcGOTermsName())
				.append(CommandConstants.FROM).append(c.gettGOTerms())
				.append(CommandConstants.WHERE)
				.append(c.getcGOTermsId()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(id));
		return sql.toString();
	}
}
