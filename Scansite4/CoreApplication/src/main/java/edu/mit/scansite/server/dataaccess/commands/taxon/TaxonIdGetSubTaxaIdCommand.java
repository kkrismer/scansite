package edu.mit.scansite.server.dataaccess.commands.taxon;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class TaxonIdGetSubTaxaIdCommand extends DbQueryCommand<Set<Integer>> {
	private String parentPath = CommandConstants.PARENT_TAXON_SEPARATOR;
	private DataSource dataSource;
	private boolean getSpeciesOnly = true;

	public TaxonIdGetSubTaxaIdCommand(Properties dbAccessConfig, Properties dbConstantsConfig, String parentPath,
			boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(useTempTablesForUpdate);
		this.dataSource = dataSource;
		this.parentPath = parentPath;
	}

	public void setSpeciesOnly(boolean getSpeciesOnly) {
		this.getSpeciesOnly = getSpeciesOnly;
	}

	@Override
	protected Set<Integer> doProcessResults(ResultSet result) throws DataAccessException {
		Set<Integer> ts = new HashSet<Integer>();
		try {
			while (result.next()) {
				ts.add(result.getInt(c.getcTaxaId()));
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return ts;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcTaxaId());
		sql.append(CommandConstants.FROM).append(c.gettTaxa(dataSource));
		sql.append(CommandConstants.WHERE);
		if (getSpeciesOnly) {
			sql.append(c.getcTaxaIsSpecies()).append(CommandConstants.EQ).append(1).append(CommandConstants.AND);
		}
		sql.append(c.getcTaxaParentTaxa()).append(CommandConstants.LIKE)
				.append(CommandConstants.enquote(parentPath + "%"));
		return sql.toString();
	}
}
