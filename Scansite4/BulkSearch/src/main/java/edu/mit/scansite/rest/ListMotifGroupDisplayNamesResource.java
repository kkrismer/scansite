package edu.mit.scansite.rest;

import edu.mit.scansite.model.MotifGroup;
import edu.mit.scansite.sql.statements.GetMotifGroupsStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/motif-group-display-names")
public class ListMotifGroupDisplayNamesResource {
    private Logger log = LoggerFactory.getLogger(ListMotifGroupDisplayNamesResource.class);
    private List<String> motifGroupDisplayNames = null;

    @Context
    ServletContext context;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> provideDataSourceDisplayNames() {
        if (motifGroupDisplayNames == null || motifGroupDisplayNames.isEmpty()) {
            parseMotifDisplayNames();
        }
        return motifGroupDisplayNames;
    }

    private void parseMotifDisplayNames() {
        motifGroupDisplayNames = new ArrayList<>();
        GetMotifGroupsStatement statement = new GetMotifGroupsStatement(false, true, false);
        List<MotifGroup> motifGroups = statement.process();
        if (motifGroups != null) {
            motifGroups.forEach(entry -> motifGroupDisplayNames.add(entry.getMotifGroupDisplayName()));
        }
    }
}
