package edu.mit.scansite.server.dataaccess.commands.protein;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.OrganismClass;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinGetAllCommand extends DbQueryCommand<ArrayList<Protein>> {

	private Set<Integer> taxIds;
	private DataSource dataSource;
	private HashMap<String, OrganismClass> classMap = new HashMap<String, OrganismClass>();

	public ProteinGetAllCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			Set<Integer> taxIds, boolean useTempTablesForUpdate,
			DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		setUseOfTempTables(useTempTablesForUpdate);
		initClassMap();
		this.taxIds = taxIds;
		this.dataSource = dataSource;
	}

	public ProteinGetAllCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		setUseOfTempTables(useTempTablesForUpdate);
		initClassMap();
		this.dataSource = dataSource;
	}

	private void initClassMap() {
		for (OrganismClass oClass : OrganismClass.values()) {
			classMap.put(oClass.getShortName(), oClass);
		}
	}

	@Override
	protected ArrayList<Protein> doProcessResults(ResultSet result)
			throws DataAccessException {
		ArrayList<Protein> proteins = new ArrayList<Protein>();
		try {
			while (result.next()) {
				Taxon taxon = new Taxon(result.getInt(c.getcTaxaId()));
				Protein p = new Protein(result.getString(c
						.getcProteinsIdentifier()), dataSource,
						result.getString(c.getcProteinsSequence()), taxon,
						result.getDouble(c.getcProteinsMolWeight()),
						result.getDouble(c.getcProteinsPI()));
				p.setOrganismClass(classMap.get(result.getString(c
						.getcProteinsClass())));
				proteins.add(p);
			}
		} catch (SQLException e) {
			throw new DataAccessException(
					"Error fetching proteins from database!", e);
		}
		return proteins;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcProteinsIdentifier())
				.append(CommandConstants.COMMA).append(c.getcTaxaId())
				.append(CommandConstants.COMMA).append(c.getcProteinsClass())
				.append(CommandConstants.COMMA)
				.append(c.getcProteinsMolWeight())
				.append(CommandConstants.COMMA).append(c.getcProteinsPI())
				.append(CommandConstants.COMMA)
				.append(c.getcProteinsSequence());
		sql.append(CommandConstants.FROM).append(c.gettProteins(dataSource));
		if (taxIds != null && !taxIds.isEmpty()) { // MANY ..OR.. ARE MORE
													// EFFICIENT
													// THAN ..IN(...)
			sql.append(CommandConstants.WHERE).append('(');
			boolean first = true;
			Iterator<Integer> it = taxIds.iterator();
			while (it.hasNext()) {
				if (first) {
					first = false;
				} else {
					sql.append(CommandConstants.OR);
				}
				sql.append(c.getcTaxaId()).append(CommandConstants.EQ)
						.append(it.next());
			}
			sql.append(" ) ");
		}
		return sql.toString();
	}
}
