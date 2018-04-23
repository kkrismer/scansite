package edu.mit.scansite.rest;

import edu.mit.scansite.model.MotifGroup;
import edu.mit.scansite.sql.constants.AvailableStatements;
import edu.mit.scansite.sql.processing.MotifGroupProcessor;
import edu.mit.scansite.sql.provider.ConnectionProvider;
import edu.mit.scansite.sql.statements.PreparedStatements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/motif-group-display-names")
public class ListMotifGroupDisplayNamesResource {
    private Logger log = LoggerFactory.getLogger(ListMotifGroupDisplayNamesResource.class);
    private List<String> motifDisplayNames = null;

    @Context
    ServletContext context;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> provideDataSourceDisplayNames() {
        if (motifDisplayNames == null) {
            parseMotifDisplayNames();
        }
        return motifDisplayNames;
    }

    private void parseMotifDisplayNames() {
        motifDisplayNames = new ArrayList<>();
        ConnectionProvider provider = new ConnectionProvider();
        Connection con = null;
        try {
            con = provider.provideConnection();
            PreparedStatement ps = new PreparedStatements().getStatement(con, AvailableStatements.MOTIF_GROUP_DISPLAY_NAMES_STATEMENT, null);
            List<MotifGroup> group = new MotifGroupProcessor().process(ps);
            group.forEach(entry -> motifDisplayNames.add(entry.getMotifGroupDisplayName()));
        } catch (ClassNotFoundException | SQLException e) {
            log.error("", e);
        } finally {
            provider.closeConnection(con);
        }
    }
}
