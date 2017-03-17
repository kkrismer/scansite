package edu.mit.scansite.server.dataaccess.commands.motifidentifier;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Konstantin Krismer
 */
public class MotifIdentifierDeleteCommand extends DbUpdateCommand {

	private int motifId;

	public MotifIdentifierDeleteCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, int motifId) {
		super(dbAccessConfig, dbConstantsConfig);
		this.motifId = motifId;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();

		sql.append(CommandConstants.DELETEFROM)
				.append(c.gettMotifIdentifierMapping())
				.append(CommandConstants.WHERE).append(c.getcMotifsId())
				.append(CommandConstants.EQ).append(motifId);
		return sql.toString();
	}
}
