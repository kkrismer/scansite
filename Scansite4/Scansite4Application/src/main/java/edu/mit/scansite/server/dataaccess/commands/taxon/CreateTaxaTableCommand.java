package edu.mit.scansite.server.dataaccess.commands.taxon;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class CreateTaxaTableCommand extends DbUpdateCommand {

	private DataSource dataSource;

	public CreateTaxaTableCommand(Properties dbAccessConfig, Properties dbConstantsConfig, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(true);
		this.dataSource = dataSource;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		String sql = "CREATE TABLE IF NOT EXISTS " + c.gettTaxa(dataSource) + " (\n" + "  " + c.getcTaxaId()
				+ "                 INT AUTO_INCREMENT,\n" + "  " + c.getcTaxaName()
				+ "               VARCHAR(200) UNIQUE NOT NULL,\n" + "  " + c.getcTaxaParentTaxa()
				+ "    VARCHAR(250) NOT NULL DEFAULT \".\",\n" + "  " + c.getcTaxaIsSpecies()
				+ "          BOOLEAN NOT NULL DEFAULT FALSE,\n" + "  PRIMARY KEY (" + c.getcTaxaId() + ")\n"
				+ "  ) ENGINE=InnoDB DEFAULT CHARSET=utf8";
		return sql;
	}
}
