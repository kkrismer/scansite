package edu.mit.scansite.server.dataaccess.commands.motif;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifAddCommand extends DbInsertCommand {

	private Motif m;

	public MotifAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, Motif motif) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.m = motif;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettMotifs();
	}

	@Override
	protected String getIdColumnName() {
		return c.getcMotifsId();
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.INSERTINTO)
				.append(c.gettMotifs())
				.append('(')
				.append(c.getcMotifsDisplayName())
				.append(CommandConstants.COMMA)
				.append(c.getcMotifsShortName())
				.append(CommandConstants.COMMA)
				.append(c.getcMotifGroupsId())
				.append(CommandConstants.COMMA)
				.append(c.getcUsersEmail())
				.append(CommandConstants.COMMA)
				.append(c.getcMotifsOptScore())
				.append(CommandConstants.COMMA)
				.append(c.getcMotifsIsPublic())
				.append(CommandConstants.COMMA)
				.append(c.getcMotifsMotifClass())
				.append(')')
				.append(CommandConstants.VALUES)
				.append('(')
				.append(CommandConstants.enquote(m.getDisplayName()))
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(m.getShortName()))
				.append(CommandConstants.COMMA)
				.append(m.getGroup().getId())
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(m.getSubmitter()))
				.append(CommandConstants.COMMA)
				.append(m.getOptimalScore())
				.append(CommandConstants.COMMA)
				.append(m.isPublic() ? "1" : "0")
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(m.getMotifClass()
						.getDatabaseEntry())).append(')');
		return sql.toString();
	}
}
