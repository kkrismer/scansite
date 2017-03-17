package edu.mit.scansite.webservice.proteinscan;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.features.ProteinScanFeature;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.*;
import edu.mit.scansite.webservice.WebService;
import edu.mit.scansite.webservice.exception.ScansiteWebServiceException;
import edu.mit.scansite.webservice.transferobjects.MotifSite;
import edu.mit.scansite.webservice.transferobjects.ProteinScanResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tobieh
 * @author Thomas Bernwinkler
 */

public class ProteinScanWebService extends WebService {
    private static final String MOTIF_SEPARATOR = "~";

    // package private
    static ProteinScanResult doProteinScan(LightWeightProtein protein, String referenceProteome,
             String dataSourceShortName, DataSource dataSource, String inputMotifShortNames, String motifClass, String stringencyValue) {

        // check motif input
        MotifClass mc;
        try {
            mc = MotifClass.getDbValue(motifClass.toUpperCase());
        } catch (Exception e) {
            throw new ScansiteWebServiceException("Given motif class is invalid!");
        }

        if (dataSourceShortName == null || dataSourceShortName.isEmpty()) {
            dataSourceShortName = "swissprot";
        }

        //If explicitly motifs picked: use them -- else motif class
        String[] motifShortNames;
        Set<String> motifShortNamesSet = null;
        if (inputMotifShortNames != null) {
            motifShortNames = inputMotifShortNames.split(MOTIF_SEPARATOR);
            if (motifShortNames.length > 0) {
                motifShortNamesSet = new HashSet<>();
                Collections.addAll(motifShortNamesSet, motifShortNames);
            }
        }

        // check stringency input
        HistogramStringency stringency = getStringency(stringencyValue);
        if (stringency == null) {
            throw new ScansiteWebServiceException("Invalid stringency value given.");
        }

//        LightWeightProtein protein = new LightWeightProtein(proteinName, proteinSequence);
        MotifSelection motifSelection = new MotifSelection();
        motifSelection.setMotifShortNames(motifShortNamesSet);
        motifSelection.setMotifClass(mc);
        final boolean showDomains = false;
        final boolean doCreateFiles = false;
        final boolean publicOnly = true;
        final String realPath = null; // only necessary if doCreateFiles or if showDomains

        try {
            DbConnector.getInstance().setWebServiceProperties(ServiceLocator.getSvcDbAccessProperties());
            ProteinScanFeature feature = new ProteinScanFeature();

            edu.mit.scansite.shared.dispatch.features.ProteinScanResult res = feature.doProteinScan(protein, motifSelection, stringency, showDomains,
                    dataSourceShortName, referenceProteome, dataSource, doCreateFiles, publicOnly, realPath);

            if (res.isSuccess()) {
                ProteinScanResult result = new ProteinScanResult();
                result.setProteinName(res.getResults().getProtein().getIdentifier());
                result.setProteinSequence(res.getResults().getProtein().getSequence());
                if (res.getResults().getHits() == null || res.getResults().getHits().isEmpty()) {
                    result.setPredictedSite(new MotifSite[]{});
                } else {
                    MotifSite[] sites = new MotifSite[res.getResults().getHits().size()];
                    for (int i = 0; i < sites.length; ++i) {
                        ScanResultSite site = res.getResults().getHits().get(i);
                        sites[i] = new MotifSite(site.getScore(), site.getPercentile(), site.getMotif().getDisplayName(), site.getMotif().getShortName(),
                                site.getSite(), site.getSiteSequence());
                    }
                    result.setPredictedSite(sites);
                }

                return result;
            } else {
                throw new ScansiteWebServiceException(res.getFailureMessage());
            }
        } catch (DataAccessException e) {
            throw new ScansiteWebServiceException("Running ProteinScan service failed.");
        }
    }

    public static HistogramStringency getStringency(String stringencyValue) {
        if (stringencyValue == null) {
            return null;
        }

        for (HistogramStringency str : HistogramStringency.values()) {
            if (str.getName().equalsIgnoreCase(stringencyValue)) {
                return str;
            }
        }
        return null;
    }

}
