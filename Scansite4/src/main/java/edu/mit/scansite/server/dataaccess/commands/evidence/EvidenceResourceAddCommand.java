package edu.mit.scansite.server.dataaccess.commands.evidence;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class EvidenceResourceAddCommand extends DbInsertCommand {

	private String resourceName;
	private String link;

	public EvidenceResourceAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			String resourceName, String link) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.resourceName = resourceName;
		this.link = link;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.INSERTINTO)
				.append(c.gettEvidenceResources()).append("( ");
		sql.append(c.getcEvidenceResourcesResource())
				.append(CommandConstants.COMMA)
				.append(c.getcEvidenceResourcesLink());
		sql.append(" ) ").append(CommandConstants.VALUES).append("( ");
		sql.append(CommandConstants.enquote(resourceName)).append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(link));
		sql.append(" ) ");
		return sql.toString();
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettEvidenceResources();
	}

	@Override
	protected String getIdColumnName() {
		return null;
	}
}
