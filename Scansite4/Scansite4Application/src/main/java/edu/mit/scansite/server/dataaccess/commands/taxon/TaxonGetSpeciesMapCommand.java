package edu.mit.scansite.server.dataaccess.commands.taxon;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class TaxonGetSpeciesMapCommand extends DbQueryCommand<Map<Integer, Taxon>> {
	private DataSource dataSource;

	public TaxonGetSpeciesMapCommand(Properties dbAccessConfig, Properties dbConstantsConfig,
			boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(useTempTablesForUpdate);
		this.dataSource = dataSource;
	}

	@Override
	protected Map<Integer, Taxon> doProcessResults(ResultSet result) throws DataAccessException {
		try {
			Map<Integer, Taxon> map = new HashMap<Integer, Taxon>();
			while (result.next()) {
				int id = result.getInt(c.getcTaxaId());
				map.put(id, new Taxon(id, result.getString((c.getcTaxaName())),
						result.getString(c.getcTaxaParentTaxa()), result.getBoolean(c.getcTaxaIsSpecies())));
			}
			return map;
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcTaxaId()).append(CommandConstants.COMMA)
				.append(c.getcTaxaName()).append(CommandConstants.COMMA).append(c.getcTaxaParentTaxa())
				.append(CommandConstants.COMMA).append(c.getcTaxaIsSpecies()).append(CommandConstants.FROM)
				.append(c.gettTaxa(dataSource));
		sql.append(CommandConstants.WHERE).append(c.getcTaxaIsSpecies()).append(CommandConstants.EQ).append(1);
		sql.append(CommandConstants.ORDERBY).append(c.getcTaxaId());
		return sql.toString();
	}
}
