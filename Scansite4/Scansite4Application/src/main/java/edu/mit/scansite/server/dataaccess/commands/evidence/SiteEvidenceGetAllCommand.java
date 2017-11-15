package edu.mit.scansite.server.dataaccess.commands.evidence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.EvidenceResource;

/**
 * Created by Thomas on 3/25/2017.
 */
public class SiteEvidenceGetAllCommand extends
        DbQueryCommand<List<EvidenceResource> > {

    public SiteEvidenceGetAllCommand(Properties dbAccessConfig, Properties dbConstantsConfig) {
        super(dbAccessConfig, dbConstantsConfig);
    }

    @Override
    protected List<EvidenceResource> doProcessResults(ResultSet result) throws DataAccessException {
        try {
            List<EvidenceResource> resources = new LinkedList<EvidenceResource>();
            while (result.next()) {
                resources.add(new EvidenceResource(
                        result.getString(c.getcEvidenceResourcesResource()),
                        result.getString(c.getcProteinsIdentifier()),
                        result.getString(c.getcSiteEvidenceSite())));
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
                .append(c.getcProteinsIdentifier())
                .append(CommandConstants.COMMA)
                .append(c.getcSiteEvidenceSite());
        sql.append(CommandConstants.FROM).append(c.gettSiteEvidence());
        return sql.toString();
    }
}
