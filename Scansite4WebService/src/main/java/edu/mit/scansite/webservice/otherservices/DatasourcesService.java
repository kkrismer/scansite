package edu.mit.scansite.webservice.otherservices;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.DataSourceDao;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.webservice.WebService;
import edu.mit.scansite.webservice.exception.ScansiteWebServiceException;
import edu.mit.scansite.webservice.transferobjects.Datasource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Tobieh
 * @author Thomas Bernwinkler
 * */

@Path("/datasources")
public class DatasourcesService extends WebService {
    /**
     * @return An object containing all available datasources/databases (each of which has a name and a nickname).
     * @throws ScansiteWebServiceException
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public Datasource[] getDatasources() {
        try {
            DaoFactory daoFac = ServiceLocator.getWebServiceInstance().getDaoFactory();
            List<DataSource> ds = retrieveDataSources(daoFac);
            if (ds != null && ds.size() > 0) {
                Datasource[] datasources = new Datasource[ds.size()];
                for (int i = 0; i < ds.size(); ++i) {
                    datasources[i] = new Datasource(ds.get(i).getDisplayName(), ds.get(i).getShortName(), ds.get(i).getVersion(), ds.get(i).getDescription(),
                            daoFac.getProteinDao().getProteinCount(ds.get(i)));
                }
                return datasources;
            }
            return new Datasource[]{};
        } catch (DataAccessException e) {
            throw new ScansiteWebServiceException("Server can not access database. " + TXT_TRY_LATER);
        }
    }

    public List<DataSource> retrieveDataSources(DaoFactory daoFac) throws DataAccessException {
            boolean primaryDataSourceOnly = true;
            DataSourceDao dsDao = daoFac.getDataSourceDao();
            return dsDao.getAll(primaryDataSourceOnly);
    }
}
