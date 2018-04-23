package edu.mit.scansite.rest;

import edu.mit.scansite.model.DataSource;
import edu.mit.scansite.sql.constants.AvailableStatements;
import edu.mit.scansite.sql.processing.DataSourceProcessor;
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
import java.util.Arrays;
import java.util.List;

@Path("/data-source-display-names")
public class ListDataSourcesResource {
    private Logger log = LoggerFactory.getLogger(ListDataSourcesResource.class);
    private List<String> dataSourceNames = null;
    private static final int DATA_SOURCE_TYPE_PROTEIN = 1;

    @Context
    ServletContext context;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> provideDataSourceDisplayNames() {
        if (dataSourceNames == null) {
            parseDataSources(DATA_SOURCE_TYPE_PROTEIN);
        }
        return dataSourceNames;
    }

    private void parseDataSources(int dataSourceType) {
        dataSourceNames = new ArrayList<>();
        ConnectionProvider provider = new ConnectionProvider();
        Connection con = null;
        try {
            con = provider.provideConnection();
            List<List<String>> queryParameters = new ArrayList<>();
            if (dataSourceType > 0 && dataSourceType < 4) {
                queryParameters.add(Arrays.asList("int", String.valueOf(dataSourceType)));
            }
            PreparedStatement ps = new PreparedStatements().getStatement(con, AvailableStatements.DATA_SOURCES_DISPLAY_NAME_STATEMENT, queryParameters);
            List<DataSource> ds = new DataSourceProcessor().process(ps);
            ds.forEach(entry -> dataSourceNames.add(entry.getDataSourceDisplayName()));
        } catch (ClassNotFoundException | SQLException e) {
            log.error("", e);
        } finally {
            provider.closeConnection(con);
        }
    }
}
