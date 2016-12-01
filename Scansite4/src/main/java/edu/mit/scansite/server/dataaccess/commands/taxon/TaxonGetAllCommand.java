package edu.mit.scansite.server.dataaccess.commands.taxon;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class TaxonGetAllCommand extends DbQueryCommand<ArrayList<Taxon>> {
	private DataSource dataSource;
	private boolean getSpeciesOnly = true;

	public TaxonGetAllCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		setUseOfTempTables(useTempTablesForUpdate);
		this.dataSource = dataSource;
	}

	public void setSpeciesOnly(boolean getSpeciesOnly) {
		this.getSpeciesOnly = getSpeciesOnly;
	}

	@Override
	protected ArrayList<Taxon> doProcessResults(ResultSet result)
			throws DataAccessException {
		ArrayList<Taxon> ts = new ArrayList<Taxon>();
		try {
			while (result.next()) {
				ts.add(new Taxon(result.getInt(c.getcTaxaId()), result
						.getString((c.getcTaxaName())), result.getString(c
						.getcTaxaParentTaxa()), result.getBoolean(c
						.getcTaxaIsSpecies())));
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return ts;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcTaxaId())
				.append(CommandConstants.COMMA).append(c.getcTaxaName())
				.append(CommandConstants.COMMA).append(c.getcTaxaParentTaxa())
				.append(CommandConstants.COMMA).append(c.getcTaxaIsSpecies())
				.append(CommandConstants.FROM)
				.append(c.gettTaxa(dataSource));
		if (getSpeciesOnly) {
			sql.append(CommandConstants.WHERE).append(c.getcTaxaIsSpecies())
					.append(CommandConstants.EQ).append(1);
		}
		sql.append(CommandConstants.ORDERBY).append(c.getcTaxaId());
		return sql.toString();
	}
}
