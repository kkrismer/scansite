package edu.mit.scansite.webservice.otherservices;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.features.OrthologScanFeature;
import edu.mit.scansite.shared.transferobjects.*;
import edu.mit.scansite.webservice.exception.ScansiteWebServiceException;
import edu.mit.scansite.webservice.proteinscan.ProteinScanUtils;
import edu.mit.scansite.webservice.proteinscan.ProteinScanWebService;
import edu.mit.scansite.webservice.transferobjects.MotifSite;
import edu.mit.scansite.webservice.transferobjects.OrthologScanResult;
import edu.mit.scansite.webservice.transferobjects.OrthologScanResultEntry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Created by Thomas on 3/9/2017.
 */
//@Path("/scanorthologs/identifier={identifier: \\S+}/dsshortname={dsshortname: [A-Za-z]+}/alignmentradius={alignmentradius: [0-9]+}/stringency={stringency: [A-Za-z]+}{sequencepattern: (/sequencepattern=([A-Z]*))?}{motifgroup: (/motifgroup=*)?}{siteposition: (/siteposition=[A-Za-z0-9])?}")
@Path("/scanorthologs")
public class ScanOrthologsService {

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public static OrthologScanResult scanOrthologs(
//            @PathParam("siteposition") String sitePositionStr,
//            @PathParam("motifgroup") String motifGroupsStr,
//            @PathParam("sequencepattern") String sequencePatternStr,
//            @PathParam("identifier") String proteinIdentifier,
//            @PathParam("dsshortname") String dsShortName,
//            @PathParam("alignmentradius") String alignmentRadiusStr,
//            @PathParam("stringency") String stringencyValue
    ) {
        //test
        String sitePositionStr = "306";
        String motifGroupsStr = "Acid_ST_kin";
        String sequencePatternStr = "";
        String proteinIdentifier = "BRCA2_HUMAN";
        String proteinDsShortName = "swissprot";
        String orthologyDsShortName = "swissprotorthology";
        String alignmentRadiusStr = "40";
        String stringencyValue = "High";

        Integer sitePosition;
        if (sitePositionStr != null && !sitePositionStr.isEmpty()) {
            sitePosition = Integer.parseInt(sitePositionStr); //todo: make changes to that the site position is assigned correctly -- only numbers possible?
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

        LightWeightProtein protein = ProteinScanUtils.getLightWeightProtein(proteinIdentifier, proteinDsShortName);
        HistogramStringency stringency = ProteinScanWebService.getStringency(stringencyValue);

        try {
            boolean publicOnly = false;
            DataSource ds = ServiceLocator.getWebServiceInstance().getDaoFactory().getDataSourceDao().get(orthologyDsShortName);
            Properties config = ServiceLocator.getWebServiceInstance().getDbAccessFile();
            DbConnector connector = new DbConnector(config);
            connector.initLongTimeConnection();
            OrthologScanFeature feature = new OrthologScanFeature(connector);
            edu.mit.scansite.shared.dispatch.features.OrthologScanResult result;
            if (motifGroup != null) {
                 result = feature.scanOrthologsByMotifGroup(sitePosition, motifGroup, ds, protein, stringency, alignmentRadius, publicOnly);
                System.out.println("Running ortholog scan based on motif groups");
            } else {
                result = feature.scanOrthologsBySequencePattern(pattern, ds, protein, stringency, alignmentRadius, publicOnly);
                System.out.println("Running ortholog scan based on sequence pattern(s)");
            }
            connector.closeLongTimeConnection();
            if(result.isSuccess()) {
                OrthologScanResult scanResult = new OrthologScanResult();
                List<OrthologScanResultEntry> resultEntries = new ArrayList<>();
                List<Ortholog> orthologs = result.getOrthologs();

                for (Ortholog ortholog : orthologs) {
                    OrthologScanResultEntry entry = new OrthologScanResultEntry();
                    entry.setProteinName(ortholog.getProtein().getIdentifier());
                    HashMap<String, Set<String> > annotationsMap = ortholog.getProtein().getAnnotations();
                    Set<String> annotationKeys = annotationsMap.keySet();
                    String annotations = "";
                    for (String annotationKey : annotationKeys) {
                        Set<String> categoryAnnotations = annotationsMap.get(annotationKey);
                        annotations += annotationKey + ": ";
                        for (String categoryAnnotation : categoryAnnotations) {
                            annotations += categoryAnnotation;
                        }
                        annotations += "; ";
                    }
                    entry.setAnnotation(annotations);
                    entry.setMolWeight(ortholog.getProtein().getMolecularWeight());
                    entry.setpI(ortholog.getProtein().getpI());
                    MotifSite [] motifSites = new MotifSite[ortholog.getPhosphorylationSites().size()];
                    for (int i=0; i < ortholog.getPhosphorylationSites().size(); i++){
                        ScanResultSite site = ortholog.getPhosphorylationSites().get(i);
                        motifSites[i] = new MotifSite(site.getScore(), site.getPercentile(), site.getMotif().getDisplayName(),
                                site.getMotif().getShortName(), site.getSite(), site.getSiteSequence());
                    }
                    entry.setPredictedSite(motifSites);
                    resultEntries.add(entry);
                }
                scanResult.setOrthologousProteins((OrthologScanResultEntry[]) resultEntries.toArray());
                scanResult.setSequenceAlignment(result.getSequenceAlignment().getHTMLFormattedAlignment());

                return scanResult;
            } else {
                throw new ScansiteWebServiceException(result.getFailureMessage());
            }
        } catch (Exception e) {
            throw new ScansiteWebServiceException("Running OrthologScan service failed.");
        }
    }

    private static SequencePattern getSequencePattern(String sequencePatternStr) {
        return null;
    }

    private static LightWeightMotifGroup getMotifGroup(String motifGroupsStr) {
        return null;
    }
}
