package edu.mit.scansite.server.dataaccess.commands.annotation;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class AnnotationTypeGetAllCommand extends
		DbQueryCommand<Map<Integer, String>> {

	public AnnotationTypeGetAllCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	@Override
	protected Map<Integer, String> doProcessResults(ResultSet result)
			throws DataAccessException {
		Map<Integer, String> types = new HashMap<Integer, String>();
		try {
			while (result.next()) {
				Integer id = result.getInt(c.getcAnnotationTypesId());
				String type = result.getString(c.getcAnnotationTypesTitle());
				types.put(id, type);
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return types;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcAnnotationTypesId())
				.append(CommandConstants.COMMA)
				.append(c.getcAnnotationTypesTitle())
				.append(CommandConstants.FROM).append(c.gettAnnotationTypes());
		return sql.toString();
	}
}
