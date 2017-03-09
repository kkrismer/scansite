package edu.mit.scansite.webservice.otherservices;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.features.OrthologScanFeature;
import edu.mit.scansite.shared.transferobjects.*;
import edu.mit.scansite.webservice.exception.ScansiteWebServiceException;
import edu.mit.scansite.webservice.proteinscan.ProteinScanUtils;
import edu.mit.scansite.webservice.proteinscan.ProteinScanWebService;
import edu.mit.scansite.webservice.transferobjects.OrthologScanResult;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

/**
 * Created by Thomas on 3/9/2017.
 */
@Path("/scanorthologs/siteposition={siteposition: [0-9]+}/motifgroups={motifgroups}/sequencepattern={sequencepattern}/identifier={identifier: \\S+}/dsshortname={dsshortname: [A-Za-z]+}/alignmentradius={alignmentradius}/stringency={stringency: [A-Za-z]+}")
public class ScanOrthologsService {

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public static OrthologScanResult scanOrthologs(
            @PathParam("siteposition") String sitePositionStr,
            @PathParam("motifgroups") String motifGroupsStr,
            @PathParam("sequencepattern") String sequencePatternStr,
            @PathParam("identifier") String proteinIdentifier,
            @PathParam("dsshortname") String dsShortName,
            @PathParam("alignmentradius") String alignmentRadiusStr,
            @PathParam("stringency") String stringencyValue
    ) {
        Integer sitePosition;
        if (sitePositionStr != null && !sitePositionStr.isEmpty()) {
            sitePosition = Integer.parseInt(sitePositionStr);
        } else {
            throw new ScansiteWebServiceException("Running OrthologScan service failed: No site position available");
        }
        LightWeightMotifGroup motifGroup = getMotifGroup(motifGroupsStr);
        SequencePattern pattern = getSequencePattern(sequencePatternStr);

        Integer alignmentRadius;
        if (alignmentRadiusStr != null && !alignmentRadiusStr.isEmpty()) {
            alignmentRadius = Integer.parseInt(alignmentRadiusStr);
        } else {
            throw new ScansiteWebServiceException("Running OrthologScan service failed: No alignment radius available");
        }

        LightWeightProtein protein = ProteinScanUtils.getLightWeightProtein(proteinIdentifier, dsShortName);
        HistogramStringency stringency = ProteinScanWebService.getStringency(stringencyValue);

        try {
            boolean publicOnly = false;
            DataSource ds = ServiceLocator.getWebServiceInstance().getDaoFactory().getDataSourceDao().get(dsShortName);
            Properties config = ServiceLocator.getWebServiceInstance().getDbAccessFile();
            DbConnector connector = new DbConnector(config);
            connector.initConnectionPooling();
            OrthologScanFeature feature = new OrthologScanFeature(connector);
            edu.mit.scansite.shared.dispatch.features.OrthologScanResult result;
            if (motifGroup != null) {
                 result = feature.scanOrthologsByMotifGroup(sitePosition, motifGroup, ds, protein, stringency, alignmentRadius, publicOnly);
                System.out.println("Running ortholog scan based on motif groups");


            } else {
                result = feature.scanOrthologsBySequencePattern(pattern, ds, protein, stringency, alignmentRadius, publicOnly);
                System.out.println("Running ortholog scan based on sequence pattern(s)");
            }

            //todo: process result(s)

        } catch (Exception e) {
            throw new ScansiteWebServiceException("Running OrthologScan service failed.");
        }


        return new OrthologScanResult("Hello from ortholog scan ;)");
    }

    private static SequencePattern getSequencePattern(String sequencePatternStr) {
        return null;
    }

    private static LightWeightMotifGroup getMotifGroup(String motifGroupsStr) {
        return null;
    }
}
