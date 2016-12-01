package edu.mit.scansite.server.dataaccess.commands.motif;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifDataDeleteCommand extends DbUpdateCommand {

	private int motifId;

	public MotifDataDeleteCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, int motifId) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.motifId = motifId;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();

		sql.append(CommandConstants.DELETEFROM).append(c.gettMotifMatrixData())
				.append(CommandConstants.WHERE).append(c.getcMotifsId())
				.append(CommandConstants.EQ).append(motifId);
		return sql.toString();
	}
}
