package edu.mit.scansite.server.dataaccess.commands.taxon;

import java.sql.ResultSet;
import java.sql.SQLException;
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
public class TaxonGetCommand extends DbQueryCommand<Taxon> {

	private int id = -1;
	private String name = null;
	private DataSource dataSource;
	private boolean getSpeciesOnly = false;

	public TaxonGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, int id,
			boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		setUseOfTempTables(useTempTablesForUpdate);
		this.dataSource = dataSource;
		this.id = id;
	}

	public TaxonGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, String name,
			boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		setUseOfTempTables(useTempTablesForUpdate);
		this.dataSource = dataSource;
		this.name = name;
	}

	public void setSpeciesOnly(boolean getSpeciesOnly) {
		this.getSpeciesOnly = getSpeciesOnly;
	}

	@Override
	protected Taxon doProcessResults(ResultSet result)
			throws DataAccessException {
		Taxon taxon = null;
		try {
			if (result.next()) {
				taxon = new Taxon(result.getInt(c.getcTaxaId()),
						result.getString(c.getcTaxaName()), result.getString(c
								.getcTaxaParentTaxa()), result.getBoolean(c
								.getcTaxaIsSpecies()));
			}
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return taxon;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcTaxaId())
				.append(CommandConstants.COMMA).append(c.getcTaxaName())
				.append(CommandConstants.COMMA).append(c.getcTaxaParentTaxa())
				.append(CommandConstants.COMMA).append(c.getcTaxaIsSpecies());
		sql.append(CommandConstants.FROM).append(c.gettTaxa(dataSource))
				.append(CommandConstants.WHERE);
		if (getSpeciesOnly) {
			sql.append(c.getcTaxaIsSpecies()).append(CommandConstants.EQ)
					.append(1).append(CommandConstants.AND);
		}
		if (name == null) {
			sql.append(c.getcTaxaId()).append(CommandConstants.EQ).append(id);
		} else {
			sql.append(c.getcTaxaName()).append(CommandConstants.LIKE)
					.append(CommandConstants.enquote(name));
		}
		return sql.toString();
	}
}
