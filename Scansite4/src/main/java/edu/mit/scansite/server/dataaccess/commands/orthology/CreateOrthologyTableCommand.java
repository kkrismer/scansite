package edu.mit.scansite.server.dataaccess.commands.orthology;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Konstantin Krismer
 */
public class CreateOrthologyTableCommand extends DbUpdateCommand {

	private DataSource dataSource;

	public CreateOrthologyTableCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		setUseOfTempTables(true);
		this.dataSource = dataSource;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ c.gettOrthology(dataSource) + " (\n"
				+ c.getcOrthologsId() + " INT(11) NOT NULL AUTO_INCREMENT,\n"
				+ " " + c.getcOrthologsGroupId() + " INT(11) NOT NULL,\n"
				+ c.getcOrthologsIdentifier()
				+ "           VARCHAR(30) NOT NULL,\n" + " PRIMARY KEY ("
				+ c.getcOrthologsId() + "),\n"
				+ " KEY `orthologsGroupIdIndex` (" + c.getcOrthologsGroupId()
				+ ")\n" + " ) ENGINE=InnoDB DEFAULT CHARSET=utf8";
		return sql;
	}
}
