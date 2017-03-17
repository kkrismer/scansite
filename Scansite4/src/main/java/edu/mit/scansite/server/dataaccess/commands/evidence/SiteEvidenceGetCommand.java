package edu.mit.scansite.server.dataaccess.commands.evidence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.EvidenceResource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SiteEvidenceGetCommand extends
		DbQueryCommand<List<EvidenceResource>> {

	private Set<String> accessions;
	private String site;

	public SiteEvidenceGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, Set<String> accessions, String site) {
		super(dbAccessConfig, dbConstantsConfig);
		this.site = site;
		this.accessions = accessions;
	}

	@Override
	protected List<EvidenceResource> doProcessResults(ResultSet result)
			throws DataAccessException {
		try {
			List<EvidenceResource> resources = new LinkedList<EvidenceResource>();
			while (result.next()) {
				resources.add(new EvidenceResource(result.getString(c
						.getcEvidenceResourcesResource()), result.getString(c
						.getcProteinsIdentifier()), site));
			}
			return resources;
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT)
				.append(c.getcEvidenceResourcesResource())
				.append(CommandConstants.COMMA)
				.append(c.getcProteinsIdentifier());
		sql.append(CommandConstants.FROM).append(c.gettSiteEvidence());
		sql.append(CommandConstants.WHERE).append(c.getcSiteEvidenceSite())
				.append(CommandConstants.LIKE)
				.append(CommandConstants.enquote(site));
		sql.append(CommandConstants.AND).append(" ( ");
		boolean first = true;
		for (String acc : accessions) {
			if (first) {
				first = false;
			} else {
				sql.append(CommandConstants.OR);
			}
			sql.append(c.getcProteinsIdentifier())
					.append(CommandConstants.LIKE)
					.append(CommandConstants.enquote(acc));
		}
		sql.append(" ) ");
		return sql.toString();
	}

}
