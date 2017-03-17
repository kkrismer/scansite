package edu.mit.scansite.server.dataaccess.commands.motifgroup;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupAddCommand extends DbInsertCommand {

	private LightWeightMotifGroup group;

	public MotifGroupAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, LightWeightMotifGroup group) {
		super(dbAccessConfig, dbConstantsConfig);
		this.group = group;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettMotifGroups();
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(c.INSERTINTO).append(c.gettMotifGroups()).append(" ( ")
				.append(c.getcMotifGroupsDisplayName() + c.COMMA)
				.append(c.getcMotifGroupsShortName()).append(" ) ")
				.append(c.VALUES).append("(\"").append(group.getDisplayName())
				.append('\"').append(c.COMMA).append(" \"")
				.append(group.getShortName()).append("\")");
		return sql.toString();
	}

	@Override
	protected String getIdColumnName() {
		return c.getcMotifGroupsId();
	}
}
