package edu.mit.scansite.webservice;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.MotifGroup;
import edu.mit.scansite.webservice.transferobjects.MotifDefinition;
import edu.mit.scansite.webservice.transferobjects.MotifDefinitions;

@Path("/motifDefinitions/motifClass={motifClass: [A-Za-z]+}")
public class MotifDefinitionsService extends WebService {

  /**
   * @return An object that contains all available motif definitions. In more detail: Their names, nicknames,
   * and groups.
   */
  @GET
  @Produces( { MediaType.APPLICATION_XML })
  public static MotifDefinitions getMotifDefinitions(@PathParam("motifClass") String motifClass) {
    MotifClass mc =  MotifClass.MAMMALIAN;
    try {
      mc = MotifClass.getDbValue(motifClass.toUpperCase());
    } catch (Exception e) {
      throw new ScansiteWebServiceException("Given motif class is invalid!");
    }
    try {
      ArrayList<Motif> dbMotifs = ServiceLocator.getInstance().getDaoFactory().getMotifDao().getAll(mc, true);
      ArrayList<MotifGroup> dbGroups = ServiceLocator.getInstance().getDaoFactory().getGroupsDao().getAll();
      if (dbMotifs != null && dbGroups != null) {
        MotifDefinition[] ms = new MotifDefinition[dbMotifs.size()];
        for (int i = 0; i < dbMotifs.size(); ++i) {
          Motif m = dbMotifs.get(i);
          ms[i] = new MotifDefinition(m.getGroup().getName(), m.getName(), m.getShortName(), m.getMotifClass().getDatabaseEntry());
        }
        return new MotifDefinitions(ms);
      }
      throw new ScansiteWebServiceException("Server can not access database. " + TXT_TRY_LATER);
    } catch (DataAccessException e) {
      throw new ScansiteWebServiceException("Server can not access database. " + TXT_TRY_LATER);
    }
  }
}
