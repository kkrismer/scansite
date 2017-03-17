package edu.mit.scansite.server.dataaccess.commands.taxon;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class TaxonIdGetByDataSourceCommand extends DbQueryCommand<Set<Integer>> {

	private DataSource dataSource;
	private boolean getSpeciesOnly = true;
	private String speciesRegex;

	public TaxonIdGetByDataSourceCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(useTempTablesForUpdate);
		this.dataSource = dataSource;
	}

	public TaxonIdGetByDataSourceCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, boolean useTempTablesForUpdate,
            DataSource dataSource, String speciesRegex) {
		this(dbAccessConfig, dbConstantsConfig, useTempTablesForUpdate, dataSource);
		this.speciesRegex = speciesRegex;
	}

	public void setSpeciesOnly(boolean getSpeciesOnly) {
		this.getSpeciesOnly = getSpeciesOnly;
	}

	@Override
	protected Set<Integer> doProcessResults(ResultSet result)
			throws DataAccessException {
		Set<Integer> ids = new HashSet<Integer>();
		try {
			while (result.next()) {
				ids.add(result.getInt(c.getcTaxaId()));
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return ids;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcTaxaId())
				.append(CommandConstants.FROM).append(c.gettTaxa(dataSource));
		String connect = CommandConstants.WHERE;
		if (getSpeciesOnly) {
			sql.append(CommandConstants.WHERE).append(c.getcTaxaIsSpecies())
					.append(CommandConstants.EQ).append(1);
			connect = CommandConstants.AND;
		}
		if (speciesRegex != null && !speciesRegex.isEmpty()) {
			sql.append(connect).append(c.getcTaxaName())
					.append(CommandConstants.REGEXP)
					.append(CommandConstants.enquote(speciesRegex));
		}
		sql.append(CommandConstants.ORDERBY).append(c.getcTaxaId());
		return sql.toString();
	}
}
