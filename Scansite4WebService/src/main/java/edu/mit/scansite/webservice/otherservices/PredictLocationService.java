package edu.mit.scansite.webservice.otherservices;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.features.PredictLocalizationFeature;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.webservice.exception.ScansiteWebServiceException;
import edu.mit.scansite.webservice.proteinscan.PredictLocationResult;
import edu.mit.scansite.webservice.proteinscan.ProteinScanUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

/**
 * Created by Thomas on 3/9/2017.
 */
@Path("/predictlocation/identifier={identifier: \\S+}/dsshortname={dsshortname: [A-Za-z]+}/motifclass={motifclass: [A-Za-z]+}")
public class PredictLocationService {

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public static PredictLocationResult predictLocation (
            @PathParam("identifier") String proteinIdentifier,
            @PathParam("dsshortname") String dsShortName,
            @PathParam("motifclass") String motifClassStr
    ) {
        LightWeightProtein protein = ProteinScanUtils.getLightWeightProtein(proteinIdentifier, dsShortName);

        MotifClass motifClass;
        try {
            motifClass = MotifClass.getDbValue(motifClassStr.toUpperCase());
        } catch (Exception e) {
            throw new ScansiteWebServiceException("Given motif class is invalid!");
        }

        try {
            Properties config = ServiceLocator.getWebServiceInstance().getDbAccessFile();
            DbConnector connector = new DbConnector(config);
            connector.initConnectionPooling();
            DataSource ds = ServiceLocator.getWebServiceInstance().getDaoFactory().getDataSourceDao().get(dsShortName);
            PredictLocalizationFeature feature = new PredictLocalizationFeature(connector);
            edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult result;

            if (protein == null) {
                result = feature.doPredictMotifsLocalization(ds, motifClass);
            } else {
                result = feature.doPredictProteinLocalization(ds, protein);
            }

            //todo process result(s)

        } catch (Exception e) {
            throw new ScansiteWebServiceException("Running localization prediction service failed.");
        }


        return new PredictLocationResult("Hello from localization prediction service :-)");
    }
}
