package edu.mit.scansite.server.dataaccess.commands.annotation;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class AnnotationAddCommand extends DbInsertCommand {

	private int typeId = -1;
	private DataSource dataSource;
	private String annotation = null;
	private String proteinId = null;

	public AnnotationAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, Integer typeId, String annotation,
			String proteinId, boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		this.typeId = typeId;
		this.annotation = annotation;
		this.proteinId = proteinId;
		this.dataSource = dataSource;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettAnnotations(dataSource);
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.INSERTINTO).append(getTableName())
				.append('(').append(c.getcProteinsIdentifier())
				.append(CommandConstants.COMMA)
				.append(c.getcAnnotationTypesId())
				.append(CommandConstants.COMMA)
				.append(c.getcAnnotationsAnnotation()).append(')')
				.append(CommandConstants.VALUES).append('(')
				.append(CommandConstants.enquote(proteinId))
				.append(CommandConstants.COMMA).append(typeId)
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(annotation)).append(')');
		return sql.toString();
	}

	@Override
	protected String getIdColumnName() {
		return c.getcAnnotationsId();
	}
}
