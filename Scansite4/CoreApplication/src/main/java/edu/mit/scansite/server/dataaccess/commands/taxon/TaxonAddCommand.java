package edu.mit.scansite.server.dataaccess.commands.taxon;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class TaxonAddCommand extends DbInsertCommand {
	private Taxon taxon;
	private DataSource dataSource;

	public TaxonAddCommand(Properties dbAccessConfig, Properties dbConstantsConfig, Taxon t,
			boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(useTempTablesForUpdate);
		this.dataSource = dataSource;
		this.taxon = t;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettTaxa(dataSource);
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		String parentList = taxon.getParentTaxonList();
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.INSERTINTO).append(getTableName()).append('(').append(c.getcTaxaName())
				.append(CommandConstants.COMMA).append(c.getcTaxaIsSpecies());
		if (parentList != null && !parentList.isEmpty()) {
			sql.append(CommandConstants.COMMA).append(c.getcTaxaParentTaxa());
		}
		sql.append(')').append(CommandConstants.VALUES).append('(').append(CommandConstants.enquote(taxon.getName()))
				.append(CommandConstants.COMMA).append((taxon.isSpecies()) ? "1" : "0");
		if (parentList != null && !parentList.isEmpty()) {
			sql.append(CommandConstants.COMMA).append('\"').append(parentList).append('\"');
		}
		sql.append(')');
		return sql.toString();
	}

	@Override
	protected String getIdColumnName() {
		return c.getcTaxaId();
	}
}
