package edu.mit.scansite.server.dataaccess.commands.annotation;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class AnnoTypeAddCommand extends DbInsertCommand {
	private String name = null;

	public AnnoTypeAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, String name) {
		super(dbAccessConfig, dbConstantsConfig);
		this.name = name;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettAnnotationTypes();
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.INSERTINTO).append(c.gettAnnotationTypes())
				.append('(').append(c.getcAnnotationTypesTitle()).append(')')
				.append(CommandConstants.VALUES).append('(')
				.append(CommandConstants.enquote(name)).append(')');
		return sql.toString();
	}

	@Override
	protected String getIdColumnName() {
		return c.getcAnnotationTypesId();
	}

}
