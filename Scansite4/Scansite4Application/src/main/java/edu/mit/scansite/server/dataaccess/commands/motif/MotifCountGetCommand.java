package edu.mit.scansite.server.dataaccess.commands.motif;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.MotifClass;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifCountGetCommand extends DbQueryCommand<Integer> {

	private MotifClass motifClass = MotifClass.MAMMALIAN;
	private boolean getPublicOnly = true;

	public MotifCountGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, MotifClass motifClass, boolean getPublicOnly) {
		super(dbAccessConfig, dbConstantsConfig);
		this.getPublicOnly = getPublicOnly;
		this.motifClass = motifClass;
	}

	@Override
	protected Integer doProcessResults(ResultSet result)
			throws DataAccessException {
		try {
			if (result.next()) {
				return result.getInt(1);
			} else {
				return -1;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		// SELECT COUNT(*) FROM `motifs` WHERE motifClass="MAMMALIAN"
		sql.append(CommandConstants.SELECT).append(" COUNT(*) ");
		sql.append(CommandConstants.FROM).append(c.gettMotifs());
		sql.append(CommandConstants.WHERE)
				.append(c.getcMotifsMotifClass())
				.append(CommandConstants.EQ)
				.append(CommandConstants.enquote(motifClass.getDatabaseEntry()));
		if (getPublicOnly) {
			sql.append(CommandConstants.AND).append(c.getcMotifsIsPublic())
					.append(CommandConstants.EQ).append(1);
		}
		return sql.toString();
	}
}
