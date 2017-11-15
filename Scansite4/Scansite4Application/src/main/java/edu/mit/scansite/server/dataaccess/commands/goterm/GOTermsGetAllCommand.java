package edu.mit.scansite.server.dataaccess.commands.goterm;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.GOTerm;

/**
 * @author Konstantin Krismer
 */
public class GOTermsGetAllCommand extends DbQueryCommand<List<GOTerm>> {

	public GOTermsGetAllCommand(Properties dbAccessConfig, Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	@Override
	protected List<GOTerm> doProcessResults(ResultSet result) throws DataAccessException {
		List<GOTerm> goTerms = new LinkedList<GOTerm>();
		try {
			while (result.next()) {
				GOTerm goTerm = new GOTerm();
				goTerm.setId(result.getString(c.getcGOTermsId()));
				goTerm.setName(result.getString(c.getcGOTermsName()));
				goTerms.add(goTerm);
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return goTerms;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcGOTermsId()).append(CommandConstants.COMMA)
				.append(c.getcGOTermsName()).append(CommandConstants.FROM).append(c.gettGOTerms());
		return sql.toString();
	}
}
