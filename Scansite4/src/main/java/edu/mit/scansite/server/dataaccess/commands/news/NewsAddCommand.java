package edu.mit.scansite.server.dataaccess.commands.news;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.NewsEntry;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsAddCommand extends DbInsertCommand {

	private NewsEntry entry;

	public NewsAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			NewsEntry entry) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.entry = entry;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettNews();
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(c.INSERTINTO).append(c.gettNews()).append(" ( ")
				.append(c.getcNewsTitle()).append(c.COMMA)
				.append(c.getcNewsText()).append(c.COMMA)
				.append(c.getcUsersEmail()).append(" ) ").append(c.VALUES)
				.append('(').append(c.enquote(entry.getTitle()))
				.append(c.COMMA).append(c.enquote(entry.getText()) + c.COMMA)
				.append(c.enquote(entry.getUser().getEmail())).append(" ) ");
		return sql.toString();
	}

	@Override
	protected String getIdColumnName() {
		return c.getcNewsId();
	}
}
