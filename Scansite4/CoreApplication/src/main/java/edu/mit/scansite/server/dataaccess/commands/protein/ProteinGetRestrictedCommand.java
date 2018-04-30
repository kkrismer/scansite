package edu.mit.scansite.server.dataaccess.commands.protein;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.OrganismClass;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinGetRestrictedCommand extends DbQueryCommand<ArrayList<Protein>> {

	private DataSource dataSource;
	private List<String> sequenceRegexs;
	private OrganismClass organismClass;
	private Set<Integer> taxaIds;
	private int phosphoSites;
	private Double piFrom;
	private Double piTo;
	private Double mwFrom;
	private Double mwTo;
	private boolean getWithSequence = true;

	private String piCol = c.getcProteinsPI();
	private HashMap<String, OrganismClass> classMap = new HashMap<String, OrganismClass>();

	public ProteinGetRestrictedCommand(Properties dbAccessConfig, Properties dbConstantsConfig, DataSource dataSource,
			List<String> sequenceRegexs, OrganismClass organismClass, Set<Integer> taxaIds, int phosphoSites,
			Double piFrom, Double piTo, Double mwFrom, Double mwTo, boolean getWithSequence) {
		super(dbAccessConfig, dbConstantsConfig);
		this.dataSource = dataSource;
		this.sequenceRegexs = sequenceRegexs;
		this.organismClass = organismClass;
		this.taxaIds = taxaIds;
		this.phosphoSites = phosphoSites;
		this.piFrom = piFrom;
		this.piTo = piTo;
		this.mwFrom = mwFrom;
		this.mwTo = mwTo;
		this.getWithSequence = getWithSequence;
		initPiCol();
		initClassMap();
	}

	private void initPiCol() {
		switch (phosphoSites) {
		case 1:
			piCol = c.getcProteinsPIPhos1();
			break;
		case 2:
			piCol = c.getcProteinsPIPhos2();
			break;
		case 3:
			piCol = c.getcProteinsPIPhos3();
			break;
		default:
			piCol = c.getcProteinsPI();
		}
	}

	private void initClassMap() {
		for (OrganismClass oClass : OrganismClass.values()) {
			classMap.put(oClass.getShortName(), oClass);
		}
	}

	@Override
	protected ArrayList<Protein> doProcessResults(ResultSet result) throws DataAccessException {
		ArrayList<Protein> proteins = new ArrayList<Protein>();
		try {
			while (result.next()) {
				Taxon taxon = new Taxon(result.getInt(c.getcTaxaId()));
				Protein p = new Protein();
				p.setIdentifier(result.getString(c.getcProteinsIdentifier()));
				p.setDataSource(dataSource);
				p.setSpecies(taxon);
				p.setMolecularWeight(result.getDouble(c.getcProteinsMolWeight()));
				p.setpI(result.getDouble(c.getcProteinsPI()));
				if (getWithSequence) {
					p.setSequence(result.getString(c.getcProteinsSequence()));
				}
				p.setOrganismClass(classMap.get(result.getString(c.getcProteinsClass())));
				p.setpIPhos1(result.getDouble(c.getcProteinsPIPhos1()));
				p.setpIPhos2(result.getDouble(c.getcProteinsPIPhos2()));
				p.setpIPhos3(result.getDouble(c.getcProteinsPIPhos3()));
				proteins.add(p);
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error fetching proteins from database!", e);
		}
		return proteins;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcProteinsIdentifier()).append(CommandConstants.COMMA)
				.append(c.getcTaxaId()).append(CommandConstants.COMMA).append(c.getcProteinsClass())
				.append(CommandConstants.COMMA).append(c.getcProteinsMolWeight()).append(CommandConstants.COMMA)
				.append(c.getcProteinsPI()).append(CommandConstants.COMMA).append(c.getcProteinsPIPhos1())
				.append(CommandConstants.COMMA).append(c.getcProteinsPIPhos2()).append(CommandConstants.COMMA)
				.append(c.getcProteinsPIPhos3());
		if (getWithSequence) {
			sql.append(CommandConstants.COMMA).append(c.getcProteinsSequence());
		}
		sql.append(CommandConstants.FROM).append(c.getProteinsTableName(dataSource));
		String connector = CommandConstants.WHERE;

		if (taxaIds != null && !taxaIds.isEmpty()) { // MANY ..OR.. ARE MORE
														// EFFICIENT THAN
														// ..IN(...)
			sql.append(connector).append('(');
			boolean first = true;
			Iterator<Integer> it = taxaIds.iterator();
			while (it.hasNext()) {
				if (first) {
					first = false;
				} else {
					sql.append(CommandConstants.OR);
				}
				sql.append(c.getcTaxaId()).append(CommandConstants.EQ).append(it.next());
			}
			sql.append(" ) ");
			connector = CommandConstants.AND;
		}

		if (mwFrom != null && mwFrom > 0) {
			sql.append(connector).append(c.getcProteinsMolWeight()).append(" >= ")
					.append(mwFrom - phosphoSites * ScansiteConstants.PHOSPHORYLATION_WEIGHT);
			connector = CommandConstants.AND;
		}
		if (mwTo != null && mwTo > 0) {
			sql.append(connector).append(c.getcProteinsMolWeight()).append(" <= ")
					.append(mwTo + phosphoSites * ScansiteConstants.PHOSPHORYLATION_WEIGHT);
			connector = CommandConstants.AND;
		}
		if (piFrom != null && piFrom > 0) {
			sql.append(connector).append(piCol).append(" >= ").append(piFrom);
			connector = CommandConstants.AND;
		}
		if (piTo != null && piTo > 0) {
			sql.append(connector).append(piCol).append(" <= ").append(piTo);
			connector = CommandConstants.AND;
		}

		if (organismClass != null && !organismClass.equals(OrganismClass.ALL)) {
			sql.append(connector).append(" ( ").append(c.getcProteinsClass()).append(CommandConstants.EQ)
					.append(CommandConstants.enquote(organismClass.getShortName()));
			if (organismClass.equals(OrganismClass.VERTEBRATA)) {
				sql.append(CommandConstants.OR).append(c.getcProteinsClass()).append(CommandConstants.EQ)
						.append(CommandConstants.enquote(OrganismClass.MAMMALIA.getShortName()));
			}
			sql.append(" ) ");
			connector = CommandConstants.AND;
		}

		if (sequenceRegexs != null && sequenceRegexs.size() > 0) {
			for (String regex : sequenceRegexs) {
				if (regex != null && !regex.isEmpty()) {
					sql.append(connector).append(c.getcProteinsSequence()).append(CommandConstants.REGEXP)
							.append(CommandConstants.enquote(regex));
					connector = CommandConstants.AND;
				}
			}
			connector = CommandConstants.AND;
		}
		return sql.toString();
	}
}
