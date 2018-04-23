package edu.mit.scansite.rest;

import edu.mit.scansite.model.DataSource;
import edu.mit.scansite.sql.constants.AvailableStatements;
import edu.mit.scansite.sql.processing.DataSourceProcessor;
import edu.mit.scansite.sql.processing.SingleValueProcessor;
import edu.mit.scansite.sql.provider.ConnectionProvider;
import edu.mit.scansite.sql.statements.PreparedStatements;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.mit.scansite.sql.constants.GeneralConstants.*;

@Path("/protein-identifiers")
public class ProteinIdentifierResource {
    private Logger log = LoggerFactory.getLogger(ProteinIdentifierResource.class);

    @Context
    ServletContext context;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> provideDataSourceDisplayNames(@QueryParam("dataSource") String dataSourceName,
                                                      @QueryParam("searchPattern") String searchPattern)
    {
        List<String> identifiers = new ArrayList<>();
        if (!StringUtils.isBlank(dataSourceName) && !StringUtils.isBlank(searchPattern)) {

            ConnectionProvider provider = new ConnectionProvider();
            Connection con = null;
            try {
                con = provider.provideConnection();

                List<List<String>> queryParameters = new ArrayList<>();
                queryParameters.add(Arrays.asList("int", "1"));
                PreparedStatement ps = new PreparedStatements().getStatement(con, AvailableStatements.DATA_SOURCE_NAMES_STATEMENT, queryParameters);
                List<DataSource> dataSources = new DataSourceProcessor().process(ps);
                ps.close();

                dataSourceName = mapDataSourceShortName(dataSources, dataSourceName);
                if (StringUtils.isBlank(dataSourceName)) {
                    return identifiers;
                }
                AvailableStatements statementType = mapDataSourceName(dataSourceName);
                if (statementType == null) {
                    return identifiers;
                }

                queryParameters.clear();
                queryParameters.add(Arrays.asList("String", "%" + searchPattern + "%"));

                ps = new PreparedStatements().getStatement(con, statementType, queryParameters);
                identifiers = new SingleValueProcessor().process(ps);
            } catch (ClassNotFoundException | SQLException e) {
                log.error("", e);
            } finally {
                provider.closeConnection(con);
            }

        }

         return identifiers;
    }

    private String mapDataSourceShortName(List<DataSource> dataSources, String displayName) {
        for (DataSource dataSource : dataSources) {
            if (dataSource.getDataSourceDisplayName().equals(displayName)) {
                return dataSource.getDataSourceShortName();
            }
        }
        return null;
    }

    private AvailableStatements mapDataSourceName(String dataSourceName) {
        final String tablePrefix = "proteins_";
        if (dataSourceName.equalsIgnoreCase(TABLE_PROTEINS_ENSEMBL_HUMAN.substring(tablePrefix.length()))) {
            return AvailableStatements.PROTEIN_IDENTIFIER_STATEMENT_ENSEMBL_HUMAN;
        }
        if (dataSourceName.equalsIgnoreCase(TABLE_PROTEINS_ENSEMBL_MOUSE.substring(tablePrefix.length()))) {
            return AvailableStatements.PROTEIN_IDENTIFIER_STATEMENT_ENSEMBL_MOUSE;
        }
        if (dataSourceName.equalsIgnoreCase(TABLE_PROTEINS_SWISSPROT.substring(tablePrefix.length()))) {
            return AvailableStatements.PROTEIN_IDENTIFIER_STATEMENT_SWISSPROT;
        }
        if (dataSourceName.equalsIgnoreCase(TABLE_PROTEINS_YEAST.substring(tablePrefix.length()))) {
            return AvailableStatements.PROTEIN_IDENTIFIER_STATEMENT_YEAST;
        }
        return null;
    }

}
