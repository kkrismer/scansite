package edu.mit.scansite.server.dataaccess.commands.localization;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Konstantin Krismer
 */
public class CreateLocalizationTableCommand extends DbUpdateCommand {

	private DataSource localizationDataSource;

	public CreateLocalizationTableCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DataSource localizationDataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(true);
		this.localizationDataSource = localizationDataSource;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ c.gettLocalization(localizationDataSource) + " (\n"
				+ c.getcLocalizationId()
				+ " INT(11) NOT NULL AUTO_INCREMENT,\n"
				+ c.getcProteinsIdentifier()
				+ " VARCHAR(30) UNIQUE NOT NULL,\n" + c.getcLocalizationScore()
				+ " INT(11) NOT NULL,\n" + c.getcLocalizationTypesId()
				+ " INT(11) NOT NULL,\n" + "FOREIGN KEY ("
				+ c.getcLocalizationTypesId() + ") REFERENCES `"
				+ c.gettLocalizationTypes() + "` (`"
				+ c.getcLocalizationTypesId() + "`) ON DELETE CASCADE,"
				+ "PRIMARY KEY (" + c.getcLocalizationId() + ")\n"
				+ " ) ENGINE=InnoDB DEFAULT CHARSET=utf8";
		return sql;
	}
}
