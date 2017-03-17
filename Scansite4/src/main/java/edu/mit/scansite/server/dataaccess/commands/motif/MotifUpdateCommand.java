package edu.mit.scansite.server.dataaccess.commands.motif;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifUpdateCommand extends DbUpdateCommand {

	private Motif motif;
	private boolean withOptScore = false;

	public MotifUpdateCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, Motif motif, boolean withOptScore) {
		super(dbAccessConfig, dbConstantsConfig);
		this.motif = motif;
		this.withOptScore = withOptScore;
	}

	public MotifUpdateCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, Motif motif) {
		super(dbAccessConfig, dbConstantsConfig);
		this.motif = motif;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.UPDATE).append(c.gettMotifs());
		sql.append(CommandConstants.SET);
		sql.append(c.getcMotifsDisplayName()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(motif.getDisplayName()))
				.append(CommandConstants.COMMA);
		sql.append(c.getcMotifsShortName()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(motif.getShortName()))
				.append(CommandConstants.COMMA);
		sql.append(c.getcMotifGroupsId()).append(CommandConstants.EQ)
				.append(motif.getGroup().getId())
				.append(CommandConstants.COMMA);
		sql.append(c.getcMotifsIsPublic()).append(CommandConstants.EQ)
				.append(motif.isPublic() ? 1 : 0)
				.append(CommandConstants.COMMA);
		sql.append(c.getcUsersEmail()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(motif.getSubmitter()))
				.append(CommandConstants.COMMA);
		sql.append(c.getcMotifsMotifClass())
				.append(CommandConstants.EQ)
				.append(CommandConstants.enquote(motif.getMotifClass()
						.getDatabaseEntry()));
		if (withOptScore) {
			sql.append(CommandConstants.COMMA).append(c.getcMotifsOptScore())
					.append(CommandConstants.EQ)
					.append(motif.getOptimalScore());
		}
		sql.append(CommandConstants.WHERE);
		sql.append(c.getcMotifsId()).append(CommandConstants.EQ)
				.append(motif.getId());
		return sql.toString();
	}

}
