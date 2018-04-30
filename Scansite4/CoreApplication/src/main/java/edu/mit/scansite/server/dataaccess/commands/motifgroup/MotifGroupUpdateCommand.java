package edu.mit.scansite.server.dataaccess.commands.motifgroup;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupUpdateCommand extends DbUpdateCommand {

	private LightWeightMotifGroup group;

	public MotifGroupUpdateCommand(Properties dbAccessConfig, Properties dbConstantsConfig,
			LightWeightMotifGroup group) {
		super(dbAccessConfig, dbConstantsConfig);
		this.group = group;
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(c.UPDATE).append(c.gettMotifGroups()).append(c.SET).append(c.getcMotifGroupsDisplayName())
				.append(c.EQ).append(c.enquote(group.getDisplayName())).append(c.COMMA)
				.append(c.getcMotifGroupsShortName()).append(c.EQ).append(c.enquote(group.getShortName()))
				.append(c.WHERE).append(c.getcMotifGroupsId()).append(c.EQ).append(group.getId());
		return sql.toString();
	}

}
