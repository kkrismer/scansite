package edu.mit.scansite.server.dataaccess.commands.motifgroup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupGetCommand extends DbQueryCommand<LightWeightMotifGroup> {

	private int id;

	public MotifGroupGetCommand(Properties dbAccessConfig, Properties dbConstantsConfig, int id) {
		super(dbAccessConfig, dbConstantsConfig);
		this.id = id;
	}

	@Override
	protected LightWeightMotifGroup doProcessResults(ResultSet result) throws DataAccessException {
		LightWeightMotifGroup group = null;
		try {
			if (result.next()) {
				group = new LightWeightMotifGroup(id, result.getString(c.getcMotifGroupsDisplayName()),
						result.getString(c.getcMotifGroupsShortName()));
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error fetching motif group from result set", e);
		}
		return group;
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(c.SELECT).append(c.getcMotifGroupsDisplayName()).append(c.COMMA).append(c.getcMotifGroupsShortName())
				.append(c.FROM).append(c.gettMotifGroups()).append(c.WHERE).append(c.getcMotifGroupsId()).append(c.EQ)
				.append(id);
		return sql.toString();
	}
}
