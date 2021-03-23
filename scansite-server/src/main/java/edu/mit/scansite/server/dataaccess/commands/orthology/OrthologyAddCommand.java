package edu.mit.scansite.server.dataaccess.commands.orthology;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Konstantin Krismer
 */
public class OrthologyAddCommand extends DbInsertCommand {

	private DataSource orthologyDataSource;
	private int orthologsGroupId;
	private String orthologsIdentifier;

	public OrthologyAddCommand(Properties dbAccessConfig, Properties dbConstantsConfig, boolean useTempTablesForUpdate,
			DataSource orthologyDataSource, int orthologsGroupId, String orthologsIdentifier) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(useTempTablesForUpdate);
		this.orthologyDataSource = orthologyDataSource;
		this.orthologsGroupId = orthologsGroupId;
		this.orthologsIdentifier = orthologsIdentifier;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettOrthology(orthologyDataSource);
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.INSERTINTO).append(getTableName()).append('(').append(c.getcOrthologsGroupId())
				.append(CommandConstants.COMMA).append(c.getcOrthologsIdentifier()).append(')')
				.append(CommandConstants.VALUES).append('(').append(orthologsGroupId).append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(orthologsIdentifier)).append(')');
		return sql.toString();
	}

	@Override
	protected String getIdColumnName() {
		return c.getcOrthologsId();
	}
}
