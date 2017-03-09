package edu.mit.scansite.webservice.otherservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.webservice.exception.ScansiteWebServiceException;
import edu.mit.scansite.webservice.WebService;
import edu.mit.scansite.webservice.transferobjects.BooleanResult;

import java.util.List;

/**
 * @author Tobieh
 * @author Thomas Bernwinkler
 * */

@Path("/proteinexists/identifier={identifier}/dsshortname={shortname}")
public class ProteinExistsService extends WebService {
    /**
     * @param proteinIdentifier
     * @param dsShortName DataSource short name
     * @return TRUE, if the otherservices is available in our database, otherwise FALSE.
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public static BooleanResult isProteinInDatabase(
            @PathParam("identifier") String proteinIdentifier,
            @PathParam("shortname") String dsShortName
    ) {

        try {
            DaoFactory daoFactory = ServiceLocator.getWebServiceInstance().getDaoFactory();
            List<DataSource> dataSourceList = new DatasourcesService().retrieveDataSources(daoFactory);
            DataSource ds = null;
            for (DataSource dataSource : dataSourceList) {
                if(dataSource.getShortName().equals(dsShortName)) {
                    ds = dataSource;
                }
            }
            if (ds == null) {
                throw new ScansiteWebServiceException("Could not assign data source shortname: " + dsShortName);
            }
            return new BooleanResult(daoFactory.getProteinDao().get(proteinIdentifier, ds) != null);
        } catch (DataAccessException e) {
            throw new ScansiteWebServiceException("Server can not access database. Maybe the given database nickname is invalid...");
        }
    }
}
