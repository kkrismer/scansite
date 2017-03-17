package edu.mit.scansite.server.dataaccess.commands.protein;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

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
public class ProteinsGetCommand extends DbQueryCommand<ArrayList<Protein>> {

	protected DataSource dataSource;
	protected List<String> identifiers = null;
	private HashMap<String, OrganismClass> classMap = new HashMap<String, OrganismClass>();

	public ProteinsGetCommand(Properties dbAccessConfig, Properties dbConstantsConfig,
		  	List<String> identifiers, DataSource dataSource, boolean useTempTablesForUpdate) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(useTempTablesForUpdate);
		initClassMap();
		this.dataSource = dataSource;
		this.identifiers = identifiers;
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
				String accNr = result.getString(c.getcProteinsIdentifier());
				double mw = result.getDouble(c.getcProteinsMolWeight());
				double pI = result.getDouble(c.getcProteinsPI());
				int taxonId = result.getInt(c.getcTaxaId());
				String seq = result.getString(c.getcProteinsSequence());

				Protein p = new Protein(accNr, dataSource, seq, new Taxon(
						taxonId), mw, pI);
				p.setOrganismClass(classMap.get(result.getString(c
						.getcProteinsClass())));
				proteins.add(p);
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return proteins;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcProteinsIdentifier())
				.append(CommandConstants.COMMA)
				.append(c.getcProteinsMolWeight())
				.append(CommandConstants.COMMA).append(c.getcProteinsClass())
				.append(CommandConstants.COMMA).append(c.getcProteinsPI())
				.append(CommandConstants.COMMA)
				.append(c.getcProteinsSequence())
				.append(CommandConstants.COMMA).append(c.getcTaxaId());
		sql.append(CommandConstants.FROM).append(
				c.gettProteins(dataSource));
		sql.append(CommandConstants.WHERE).append(c.getcProteinsIdentifier())
				.append(CommandConstants.IN).append(CommandConstants.LPAR);
		for (int i = 0; i < identifiers.size(); ++i) {
			sql.append(CommandConstants.enquote(identifiers.get(i)));
			if (i + 1 < identifiers.size()) {
				sql.append(CommandConstants.COMMA);
			}
		}
		sql.append(CommandConstants.RPAR);
		return sql.toString();
	}
}
