package edu.mit.scansite.server.dataaccess.commands.evidence;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.EvidenceResource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class EvidenceResourceGetAllCommand extends
		DbQueryCommand<List<EvidenceResource>> {

	public EvidenceResourceGetAllCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	@Override
	protected List<EvidenceResource> doProcessResults(ResultSet result)
			throws DataAccessException {
		List<EvidenceResource> rs = new LinkedList<EvidenceResource>();
		try {
			while (result.next()) {
				rs.add(new EvidenceResource(result.getString(c
						.getcEvidenceResourcesResource()), result.getString(c
						.getcEvidenceResourcesLink())));
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return rs;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT)
				.append(c.getcEvidenceResourcesResource())
				.append(CommandConstants.COMMA)
				.append(c.getcEvidenceResourcesLink())
				.append(CommandConstants.FROM)
				.append(c.gettEvidenceResources());
		return sql.toString();
	}
}
