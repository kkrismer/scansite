package edu.mit.scansite.server.dataaccess.commands.motifgroup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.LightWeightMotif;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.MotifGroup;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupGetAllCommand extends DbQueryCommand<List<MotifGroup>> {
	private MotifClass motifClass = null;
	private boolean publicOnly = true;

	public MotifGroupGetAllCommand(Properties dbAccessConfig, Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	public MotifGroupGetAllCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, MotifClass motifClass, boolean publicOnly) {
		super(dbAccessConfig, dbConstantsConfig);
		this.motifClass = motifClass;
		this.publicOnly = publicOnly;
	}

	@Override
	protected List<MotifGroup> doProcessResults(ResultSet result)
			throws DataAccessException {
		Map<Integer, MotifGroup> groups = new HashMap<Integer, MotifGroup>();
		try {
			while (result.next()) {
				MotifGroup group;
				if (groups.containsKey(result.getInt(c.getcMotifGroupsId()))) {
					group = groups.get(result.getInt(c.getcMotifGroupsId()));
				} else {
					group = new MotifGroup(
							result.getInt(c.getcMotifGroupsId()),
							result.getString(c.getcMotifGroupsDisplayName()),
							result.getString(c.getcMotifGroupsShortName()));
					groups.put(group.getId(), group);
				}
				group.getMotifs().add(
						new LightWeightMotif(result.getInt(c.getcMotifsId()),
								result.getString(c.getcMotifsDisplayName()),
								result.getString(c.getcMotifsShortName())));
			}
		} catch (SQLException e) {
			throw new DataAccessException(
					"Error fetching motif group from result set!", e);
		}
		return new LinkedList<MotifGroup>(groups.values());
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append('*')
				.append(CommandConstants.FROM).append(c.gettMotifs())
				.append(CommandConstants.INNERJOIN).append(c.gettMotifGroups())
				.append(CommandConstants.USING).append(CommandConstants.LPAR)
				.append(c.getcMotifGroupsId()).append(CommandConstants.RPAR);
		if (motifClass != null) {
			sql.append(CommandConstants.WHERE)
					.append(c.getcMotifsMotifClass())
					.append(CommandConstants.EQ)
					.append(CommandConstants.enquote(motifClass
							.getDatabaseEntry()));
			if (publicOnly) {
				sql.append(CommandConstants.AND).append(c.getcMotifsIsPublic())
						.append(CommandConstants.EQ).append('1');
			}
		} else {
			if (publicOnly) {
				sql.append(CommandConstants.WHERE)
						.append(c.getcMotifsIsPublic())
						.append(CommandConstants.EQ).append('1');
			}
		}
		return sql.toString();
	}
}
