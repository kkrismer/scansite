package edu.mit.scansite.server.dataaccess.commands.evidence;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SiteEvidenceAddCommand extends DbInsertCommand {

	private String accession;
	private String site;
	private String evidenceResource;

	public SiteEvidenceAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, String accession, String site, String evidenceResource) {
		super(dbAccessConfig, dbConstantsConfig);
		this.accession = accession;
		this.site = site;
		this.evidenceResource = evidenceResource;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.INSERTINTO).append(c.gettSiteEvidence())
				.append("( ");
		sql.append(c.getcProteinsIdentifier()).append(CommandConstants.COMMA)
				.append(c.getcSiteEvidenceSite())
				.append(CommandConstants.COMMA)
				.append(c.getcEvidenceResourcesResource());
		sql.append(" ) ").append(CommandConstants.VALUES).append("( ");
		sql.append(CommandConstants.enquote(accession))
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(site))
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(evidenceResource));
		sql.append(" ) ");
		return sql.toString();
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettSiteEvidence();
	}

	@Override
	protected String getIdColumnName() {
		return null;
	}

}
