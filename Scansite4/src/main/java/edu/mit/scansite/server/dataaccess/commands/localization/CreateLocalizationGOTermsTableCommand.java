package edu.mit.scansite.server.dataaccess.commands.localization;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Konstantin Krismer
 */
public class CreateLocalizationGOTermsTableCommand extends DbUpdateCommand {

	private DataSource localizationDataSource;

	public CreateLocalizationGOTermsTableCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DataSource localizationDataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(true);
		this.localizationDataSource = localizationDataSource;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ c.gettLocalizationGOTerms(localizationDataSource) + " (\n"
				+ c.getcLocalizationGOTermsId()
				+ " INT(11) NOT NULL AUTO_INCREMENT,\n"
				+ c.getcLocalizationId() + " INT(11) NOT NULL,\n"
				+ c.getcGOTermsId() + " VARCHAR(10) NOT NULL,\n"
				+ c.getcEvidenceCodesId() + " INT(11) NULL,\n"
				+ "FOREIGN KEY (" + c.getcLocalizationId() + ") REFERENCES `"
				+ c.gettLocalization(localizationDataSource) + "` (`"
				+ c.getcLocalizationId() + "`) ON DELETE CASCADE,"
				+ "FOREIGN KEY (" + c.getcGOTermsId() + ") REFERENCES `"
				+ c.gettGOTerms() + "` (`" + c.getcGOTermsId()
				+ "`) ON DELETE CASCADE," + "FOREIGN KEY ("
				+ c.getcEvidenceCodesId() + ") REFERENCES `"
				+ c.gettEvidenceCodes() + "` (`" + c.getcEvidenceCodesId()
				+ "`) ON DELETE CASCADE," + "PRIMARY KEY ("
				+ c.getcLocalizationGOTermsId() + ")\n"
				+ " ) ENGINE=InnoDB DEFAULT CHARSET=utf8";
		return sql;
	}
}
