package edu.mit.scansite.webservice;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.DatasourceDao;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.webservice.transferobjects.Datasource;

@Path("/datasources")
public class DatasourcesService extends WebService {
  /**
   * @return An object containing all available datasources/databases (each of which has a name and a nickname).
   * @throws ScansiteWebServiceException 
   */
  @GET
  @Produces( { MediaType.APPLICATION_XML })
  public Datasource[] getDatasources() {
    try {
      DaoFactory daoFac = ServiceLocator.getInstance().getDaoFactory();
      DatasourceDao dsDao = daoFac.getDatasourceDao();
      ArrayList<edu.mit.scansite.shared.transferobjects.Datasource> ds = dsDao.getAll();
      if (ds != null && ds.size() > 0) {
        Datasource [] datasources = new Datasource[ds.size()];
        for (int i = 0; i < ds.size(); ++i) {
          datasources[i] = new Datasource(ds.get(i).getDisplayName(), ds.get(i).getShortName(), ds.get(i).getVersion(), ds.get(i).getDescription(), 
              daoFac.getProteinDao().getProteinCount(ds.get(i).getShortName()));
        }
        return datasources;
      }
      return new Datasource[] {};
    } catch (DataAccessException e) {
      throw new ScansiteWebServiceException("Server can not access database. " + TXT_TRY_LATER);
    }
  }
}
