package edu.mit.scansite.server.dataaccess.commands.annotation;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class CreateAnnotationsTableCommand extends DbUpdateCommand {
	private DataSource dataSource;

	public CreateAnnotationsTableCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig,
			DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(true);
		this.dataSource = dataSource;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ c.gettAnnotations(dataSource) + " (\n" + "  "
				+ c.getcAnnotationsId() + "         INT AUTO_INCREMENT,\n"
				+ "  " + c.getcProteinsIdentifier() + "  VARCHAR(30),\n" + "  "
				+ c.getcAnnotationTypesId() + "     INT NOT NULL,\n" + "  "
				+ c.getcAnnotationsAnnotation() + " TEXT,\n"
				+ "  FOREIGN KEY (" + c.getcProteinsIdentifier()
				+ ") REFERENCES " + c.gettProteins(dataSource) + "("
				+ c.getcProteinsIdentifier() + "),\n" + "  FOREIGN KEY ("
				+ c.getcAnnotationTypesId() + ") REFERENCES "
				+ c.gettAnnotationTypes() + "(" + c.getcAnnotationTypesId()
				+ "),\n" + "  PRIMARY KEY(" + c.getcAnnotationsId() + ", "
				+ c.getcProteinsIdentifier() + ")\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
		return sql;
	}
}
