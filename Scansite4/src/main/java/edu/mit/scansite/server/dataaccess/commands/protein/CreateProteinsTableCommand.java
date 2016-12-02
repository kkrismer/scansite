package edu.mit.scansite.server.dataaccess.commands.protein;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class CreateProteinsTableCommand extends DbUpdateCommand {

	private DataSource dataSource;

	public CreateProteinsTableCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		setUseOfTempTables(true);
		this.dataSource = dataSource;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		String sql = "CREATE TABLE IF NOT EXISTS " + c.gettProteins(dataSource)
				+ " (\n" + " " + c.getcProteinsIdentifier()
				+ " VARCHAR(30) UNIQUE NOT NULL,\n" + " " + c.getcTaxaId()
				+ "       INT NOT NULL,\n" + " " + c.getcProteinsClass()
				+ "           VARCHAR(3) DEFAULT \"OTR\",\n" + " "
				+ c.getcProteinsMolWeight() + " DOUBLE DEFAULT 0,\n" + " "
				+ c.getcProteinsPI() + "              DOUBLE DEFAULT 0,\n"
				+ " " + c.getcProteinsPIPhos1()
				+ "         DOUBLE DEFAULT 0,\n" + " "
				+ c.getcProteinsPIPhos2() + "         DOUBLE DEFAULT 0,\n"
				+ " " + c.getcProteinsPIPhos3()
				+ "         DOUBLE DEFAULT 0,\n" + " "
				+ c.getcProteinsSequence() + "        TEXT NOT NULL,\n"
				+ " FOREIGN KEY (" + c.getcTaxaId() + ") REFERENCES "
				+ c.gettTaxa(dataSource) + "(" + c.getcTaxaId() + "),\n"
				+ " PRIMARY KEY (" + c.getcProteinsIdentifier() + ")\n"
				+ " ) ENGINE=InnoDB DEFAULT CHARSET=utf8";
		return sql;
	}
}
