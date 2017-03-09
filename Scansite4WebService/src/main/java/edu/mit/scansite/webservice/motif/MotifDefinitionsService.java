package edu.mit.scansite.webservice.motif;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.MotifGroup;
import edu.mit.scansite.webservice.exception.ScansiteWebServiceException;
import edu.mit.scansite.webservice.WebService;
import edu.mit.scansite.webservice.transferobjects.MotifDefinition;
import edu.mit.scansite.webservice.transferobjects.MotifDefinitions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Tobieh
 * @author Thomas Bernwinkler
 */

@Path("/motifdefinitions/motifclass={motifclass: [A-Za-z]+}")
public class MotifDefinitionsService extends WebService {

    /**
     * @return An object that contains all available motif definitions. In more detail: Their names, nicknames,
     * and groups.
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public static MotifDefinitions getMotifDefinitions(@PathParam("motifclass") String motifClass) {
        MotifClass mc = MotifClass.MAMMALIAN;
        try {
            mc = MotifClass.getDbValue(motifClass.toUpperCase());
        } catch (Exception e) {
            throw new ScansiteWebServiceException("Given motif class is invalid!");
        }
        try {
            List<Motif> dbMotifs = ServiceLocator.getWebServiceInstance().getDaoFactory().getMotifDao().getAll(mc, true);
            List<MotifGroup> dbGroups = ServiceLocator.getWebServiceInstance().getDaoFactory().getGroupsDao().getAll();
            if (dbMotifs != null && dbGroups != null) {
                MotifDefinition[] ms = new MotifDefinition[dbMotifs.size()];
                for (int i = 0; i < dbMotifs.size(); ++i) {
                    Motif m = dbMotifs.get(i);
                    //todo: check whether getShortName or getDisplayName
                    ms[i] = new MotifDefinition(m.getGroup().getShortName(), m.getShortName(), m.getShortName(), m.getMotifClass().getDatabaseEntry());
                }
                return new MotifDefinitions(ms);
            }
            throw new ScansiteWebServiceException("Server can not access database. " + TXT_TRY_LATER);
        } catch (DataAccessException e) {
            throw new ScansiteWebServiceException("Server can not access database. " + TXT_TRY_LATER);
        }
    }
}
