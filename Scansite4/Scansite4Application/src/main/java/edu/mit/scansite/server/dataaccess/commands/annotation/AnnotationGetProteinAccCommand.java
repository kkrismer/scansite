package edu.mit.scansite.server.dataaccess.commands.annotation;

import java.sql.ResultSet;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class AnnotationGetProteinAccCommand extends DbQueryCommand<String> {

	private String accessionAnnotation = null;
	private DataSource dataSource;

	public AnnotationGetProteinAccCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, String accessionAnnotation,
		    boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		this.accessionAnnotation = accessionAnnotation;
		this.dataSource = dataSource;
	}

	@Override
	protected String doProcessResults(ResultSet result)
			throws DataAccessException {
		String proteinAcc = null;
		try {
			if (result.next()) {
				proteinAcc = result.getString(c.getcProteinsIdentifier());
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return proteinAcc;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcProteinsIdentifier())
				.append(CommandConstants.FROM)
				.append(c.gettAnnotations(dataSource))
				.append(CommandConstants.WHERE)
				.append(c.getcAnnotationsAnnotation())
				.append(CommandConstants.LIKE)
				.append(CommandConstants.enquote(accessionAnnotation));
		return sql.toString();
	}

}
