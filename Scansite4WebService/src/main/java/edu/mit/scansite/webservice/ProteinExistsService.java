package edu.mit.scansite.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.webservice.transferobjects.BooleanResult;

@Path("/proteinExists/accession={accession}/datasourceNickname={datasource}")
public class ProteinExistsService extends WebService {
  /**
   * @param proteinAccession A protein accession from the specified datasource.
   * @param datasourceNickName A datasource nick as returned by getDatasources().
   * @return TRUE, if the protein is available in our database, otherwise FALSE.
   */
  @GET
  @Produces( { MediaType.APPLICATION_XML })
  public static BooleanResult isProteinInDatabase(
      @PathParam("accession") String proteinAccession, 
      @PathParam("datasource") String datasourceNickName
      ) {
    
    try {
      return new BooleanResult(ServiceLocator.getInstance().getDaoFactory().getProteinDao().get(proteinAccession, datasourceNickName) != null);
    } catch (DataAccessException e) {
      throw new ScansiteWebServiceException("Server can not access database. Maybe the given database nickname is invalid...");
    }
  }
}
