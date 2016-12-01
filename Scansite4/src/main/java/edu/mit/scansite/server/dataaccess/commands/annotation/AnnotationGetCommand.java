package edu.mit.scansite.server.dataaccess.commands.annotation;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class AnnotationGetCommand extends
		DbQueryCommand<HashMap<String, Set<String>>> {

	private String proteinId = null;
	private DataSource dataSource;
	private String regex = null;

	public AnnotationGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			String proteinId, boolean useTempTablesForUpdate,
			DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.proteinId = proteinId;
		this.dataSource = dataSource;
	}

	public AnnotationGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			String proteinId, boolean useTempTablesForUpdate,
			DataSource dataSource, String regex) {
		this(dbAccessConfig, dbConstantsConfig, dbConnector, proteinId,
				useTempTablesForUpdate, dataSource);
		this.regex = regex;
	}

	@Override
	protected HashMap<String, Set<String>> doProcessResults(ResultSet result)
			throws DataAccessException {
		HashMap<String, Set<String>> as = new HashMap<String, Set<String>>();
		try {
			while (result.next()) {
				String key = result.getString(result.findColumn(c
						.gettAnnotationTypes()
						+ CommandConstants.DOT
						+ c.getcAnnotationTypesTitle()));
				String value = result.getString(c.gettAnnotations(dataSource)
						+ CommandConstants.DOT + c.getcAnnotationsAnnotation());
				Set<String> vals = as.get(key);
				if (vals == null) {
					vals = new HashSet<String>();
				}
				vals.add(value);
				as.put(key, vals);
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return as;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT)
				.append(c.getcAnnotationTypesTitle())
				.append(CommandConstants.COMMA)
				.append(c.getcAnnotationsAnnotation())
				.append(CommandConstants.FROM)
				.append(c.gettAnnotations(dataSource))
				.append(CommandConstants.INNERJOIN)
				.append(c.gettAnnotationTypes()).append(CommandConstants.USING)
				.append(CommandConstants.LPAR)
				.append(c.getcAnnotationTypesId())
				.append(CommandConstants.RPAR).append(CommandConstants.WHERE)
				.append(c.getcProteinsIdentifier())
				.append(CommandConstants.LIKE)
				.append(CommandConstants.enquote(proteinId));
		if (regex != null && !regex.isEmpty()) {
			sql.append(CommandConstants.AND)
					.append(c.getcAnnotationsAnnotation())
					.append(CommandConstants.REGEXP)
					.append(CommandConstants.enquote(regex));
		}
		return sql.toString();
	}

}
