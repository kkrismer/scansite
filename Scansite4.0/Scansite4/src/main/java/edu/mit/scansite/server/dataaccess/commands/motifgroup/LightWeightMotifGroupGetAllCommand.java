package edu.mit.scansite.server.dataaccess.commands.motifgroup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class LightWeightMotifGroupGetAllCommand extends
		DbQueryCommand<List<LightWeightMotifGroup>> {

	public LightWeightMotifGroupGetAllCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	@Override
	protected List<LightWeightMotifGroup> doProcessResults(ResultSet result)
			throws DataAccessException {
		List<LightWeightMotifGroup> groups = new ArrayList<LightWeightMotifGroup>();
		try {
			while (result.next()) {
				LightWeightMotifGroup group = new LightWeightMotifGroup(
						result.getInt(c.getcMotifGroupsId()),
						result.getString(c.getcMotifGroupsDisplayName()),
						result.getString(c.getcMotifGroupsShortName()));
				groups.add(group);
			}
		} catch (SQLException e) {
			throw new DataAccessException(
					"Error fetching motif group from result set!", e);
		}
		return groups;
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(c.SELECT).append(c.getcMotifGroupsId()).append(c.COMMA)
				.append(c.getcMotifGroupsDisplayName()).append(c.COMMA)
				.append(c.getcMotifGroupsShortName()).append(c.FROM)
				.append(c.gettMotifGroups()).append(c.ORDERBY)
				.append(c.getcMotifGroupsShortName());
		return sql.toString();
	}

}
