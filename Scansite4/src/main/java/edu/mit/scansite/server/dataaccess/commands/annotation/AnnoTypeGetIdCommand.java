package edu.mit.scansite.server.dataaccess.commands.annotation;

import java.sql.ResultSet;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class AnnoTypeGetIdCommand extends DbQueryCommand<Integer> {

	private String name;

	public AnnoTypeGetIdCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, String name) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.name = name;
	}

	@Override
	protected Integer doProcessResults(ResultSet result)
			throws DataAccessException {
		int id = -1;
		try {
			if (result.next()) {
				id = result.getInt(c.getcAnnotationTypesId());
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return id;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcAnnotationTypesId())
				.append(CommandConstants.FROM).append(c.gettAnnotationTypes())
				.append(CommandConstants.WHERE)
				.append(c.getcAnnotationTypesTitle())
				.append(CommandConstants.LIKE)
				.append(CommandConstants.enquote(name));
		return sql.toString();
	}

}
