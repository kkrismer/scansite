package edu.mit.scansite.server.dataaccess.commands.news;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.NewsEntry;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsUpdateCommand extends DbUpdateCommand {
	private NewsEntry entry;

	public NewsUpdateCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			NewsEntry entry) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.entry = entry;
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(c.UPDATE).append(c.gettNews()).append(c.SET)
				.append(c.getcNewsTitle()).append(c.EQ)
				.append(c.enquote(entry.getTitle())).append(c.COMMA)
				.append(c.getcNewsText()).append(c.EQ)
				.append(c.enquote(entry.getText())).append(c.COMMA)
				.append(c.getcUsersEmail()).append(c.EQ)
				.append(c.enquote(entry.getUser().getEmail())).append(c.WHERE)
				.append(c.getcNewsId()).append(c.EQ).append(entry.getId());
		return sql.toString();
	}
}
