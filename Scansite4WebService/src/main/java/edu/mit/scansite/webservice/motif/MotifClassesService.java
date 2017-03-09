package edu.mit.scansite.webservice.motif;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.MotifDao;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.webservice.exception.ScansiteWebServiceException;
import edu.mit.scansite.webservice.WebService;
import edu.mit.scansite.webservice.transferobjects.MotifClasses;

@Path("/motifclasses")
public class MotifClassesService extends WebService {
    /**
     * @return An object containing all valid motif classes.
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public static MotifClasses getMotifClasses() {
        MotifClass[] mcs = MotifClass.values();
        ArrayList<String> mClasses = new ArrayList<String>();
        MotifDao dao;
        try {
            dao = ServiceLocator.getWebServiceInstance().getSvcDaoFactory().getMotifDao();
            //dao = ServiceLocator.getInstance().getDaoFactory().getMotifDao();
            boolean publicOnly = true;
            for (MotifClass mc : mcs) {
                if (dao.getNumberOfMotifs(mc, publicOnly) > 0) {
                    mClasses.add(mc.getDatabaseEntry());
                }
            }
            return new MotifClasses(mClasses.toArray(new String[]{}));
        } catch (DataAccessException e) {
            throw new ScansiteWebServiceException("Server can not access database. " + TXT_TRY_LATER);
        }
    }
}
