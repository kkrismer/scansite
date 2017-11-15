package edu.mit.scansite.server.dataaccess.commands.motifgroup;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupDeleteCommand extends DbUpdateCommand {

	private int id;

	public MotifGroupDeleteCommand(Properties dbAccessConfig, Properties dbConstantsConfig, int id) {
		super(dbAccessConfig, dbConstantsConfig);
		this.id = id;
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(c.DELETEFROM).append(c.gettMotifGroups()).append(c.WHERE).append(c.getcMotifGroupsId()).append(c.EQ)
				.append(id);
		return sql.toString();
	}

}
