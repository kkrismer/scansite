package edu.mit.scansite.server.dataaccess.commands.goterm;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.GOTerm;

/**
 * @author Konstantin Krismer
 */
public class GOTermAddCommand extends DbInsertCommand {
	private GOTerm goTerm;

	public GOTermAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, GOTerm goTerm) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.goTerm = goTerm;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettGOTerms();
	}

	@Override
	protected String getIdColumnName() {
		return null; // id is not set by database
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();

		sql.append(CommandConstants.INSERTINTO).append(c.gettGOTerms())
				.append('(').append(c.getcGOTermsId())
				.append(CommandConstants.COMMA).append(c.getcGOTermsName())
				.append(')').append(CommandConstants.VALUES).append('(')
				.append(CommandConstants.enquote(goTerm.getId()))
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(goTerm.getName())).append(')');
		return sql.toString();
	}
}
