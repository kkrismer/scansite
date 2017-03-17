package edu.mit.scansite.server.dataaccess.commands.orthology;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
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
 * @author Konstantin Krismer
 */
public class OrthologProteinsGetCommand extends DbQueryCommand<List<Protein>> {

	protected DataSource orthologySource;
	protected List<String> identifiers;
	protected DataSource proteinSource;
	private HashMap<String, OrganismClass> classMap = new HashMap<String, OrganismClass>();

	public OrthologProteinsGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, boolean useTempTablesForUpdate, DataSource orthologySource,
			List<String> identifiers, DataSource proteinSource) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(useTempTablesForUpdate);
		initClassMap();
		this.orthologySource = orthologySource;
		this.identifiers = identifiers;
		this.proteinSource = proteinSource;
	}

	public OrthologProteinsGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig,
			DataSource orthologySource, String identifier,
			DataSource proteinSource, boolean useTempTablesForUpdate) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(useTempTablesForUpdate);
		initClassMap();
		this.orthologySource = orthologySource;
		this.identifiers = Arrays.asList(identifier);
		this.proteinSource = proteinSource;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		// SELECT p.*
		// FROM `orthologs_homologene` o, `proteins_genpept` p
		// WHERE o.groupId = (
		// SELECT oo.groupId
		// FROM `orthologs_homologene` oo
		// WHERE oo.identifier = "NP_001120800" )
		// AND o.identifier = p.accessionNumber

		sql.append(CommandConstants.SELECT).append(c.getcProteinsIdentifier())
				.append(CommandConstants.COMMA)
				.append(c.getcProteinsMolWeight())
				.append(CommandConstants.COMMA).append(c.getcProteinsClass())
				.append(CommandConstants.COMMA).append(c.getcProteinsPI())
				.append(CommandConstants.COMMA)
				.append(c.getcProteinsSequence())
				.append(CommandConstants.COMMA).append(c.getcTaxaId());
		sql.append(CommandConstants.FROM)
				.append(c.getOrthologsTableName(orthologySource))
				.append(CommandConstants.COMMA)
				.append(c.getProteinsTableName(proteinSource));
		sql.append(CommandConstants.WHERE);
		sql.append(c.getcOrthologsGroupId()).append(CommandConstants.EQ)
				.append(CommandConstants.LPAR);
		sql.append(CommandConstants.SELECT).append(c.getcOrthologsGroupId())
				.append(CommandConstants.FROM)
				.append(c.getOrthologsTableName(orthologySource))
				.append(CommandConstants.WHERE)
				.append(c.getcOrthologsIdentifier())
				.append(CommandConstants.IN).append(CommandConstants.LPAR);
		for (int i = 0; i < identifiers.size(); ++i) {
			sql.append(CommandConstants.enquote(identifiers.get(i)));
			if (i + 1 < identifiers.size()) {
				sql.append(CommandConstants.COMMA);
			}
		}
		sql.append(CommandConstants.RPAR).append(CommandConstants.RPAR);
		sql.append(CommandConstants.AND).append(c.getcOrthologsIdentifier())
				.append(CommandConstants.EQ).append(c.getcProteinsIdentifier());

		return sql.toString();
	}

	@Override
	protected List<Protein> doProcessResults(ResultSet result)
			throws DataAccessException {
		List<Protein> ps = new ArrayList<Protein>();

		try {
			while (result.next()) {
				String accNr = result.getString(c.getcProteinsIdentifier());
				double mw = result.getDouble(c.getcProteinsMolWeight());
				double pI = result.getDouble(c.getcProteinsPI());
				int taxonId = result.getInt(c.getcTaxaId());
				String seq = result.getString(c.getcProteinsSequence());

				Protein p = new Protein(accNr, proteinSource, seq, new Taxon(
						taxonId), mw, pI);
				p.setOrganismClass(classMap.get(result.getString(c
						.getcProteinsClass())));
				ps.add(p);
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return ps;
	}

	private void initClassMap() {
		for (OrganismClass oClass : OrganismClass.values()) {
			classMap.put(oClass.getShortName(), oClass);
		}
	}
}
