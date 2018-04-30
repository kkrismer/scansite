package edu.mit.scansite.server.dataaccess.commands.taxon;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class TaxonDeleteCommand extends DbUpdateCommand {

	private String name = null;
	private int id = -1;
	private DataSource dataSource;

	public TaxonDeleteCommand(Properties dbAccessConfig, Properties dbConstantsConfig, String name,
			boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(useTempTablesForUpdate);
		this.dataSource = dataSource;
		this.name = name;
	}

	public TaxonDeleteCommand(Properties dbAccessConfig, Properties dbConstantsConfig, int id,
			boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(useTempTablesForUpdate);
		this.dataSource = dataSource;
		this.id = id;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		CommandConstants c = CommandConstants.instance();
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.DELETEFROM).append(c.gettTaxa(dataSource)).append(CommandConstants.WHERE);
		if (name == null) {
			sql.append(c.getcTaxaId()).append(CommandConstants.EQ).append(id);
		} else {
			sql.append(c.getcTaxaName()).append(CommandConstants.LIKE).append(CommandConstants.enquote(name));
		}
		return sql.toString();
	}
}
