package edu.mit.scansite.webservice;

import java.util.ArrayList;

import edu.mit.scansite.server.features.ProteinScanFeature;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import edu.mit.scansite.shared.util.Formatter;
import edu.mit.scansite.shared.util.Validator;
import edu.mit.scansite.webservice.transferobjects.MotifSite;
import edu.mit.scansite.webservice.transferobjects.ProteinScanResult;

public class ProteinScanWebService {
  protected static final String MOTIF_SEPARATOR = "~";

  protected static ProteinScanResult doProteinScan(boolean isDatabaseProtein, String proteinName, String proteinSequence, String datasourceNickName,
      String motifNickNames, String motifClass, String stringencyValue) {

    // check motif input
    MotifClass mc =  MotifClass.MAMMALIAN;
    try {
      mc = MotifClass.getDbValue(motifClass.toUpperCase());
    } catch (Exception e) {
      throw new ScansiteWebServiceException("Given motif class is invalid!");
    }

    if (motifNickNames == null) {
      motifNickNames = "";
    }
    String[] motifShortNames = motifNickNames.split(MOTIF_SEPARATOR);
    ArrayList<String> motifNickNamesList = new ArrayList<String>();
    if (!motifNickNames.isEmpty() && motifShortNames != null && motifShortNames.length > 0) {
      for (String nick : motifShortNames) {
        motifNickNamesList.add(nick);
      }
    }

    // check protein input
    Formatter formatter = new Formatter();
    Validator validator = new Validator();
    if (isDatabaseProtein && (proteinName == null || proteinName.isEmpty())) {
      throw new ScansiteWebServiceException("No protein accession is given!");
    } else if (!isDatabaseProtein && (proteinSequence == null || proteinSequence.isEmpty())) {
      throw new ScansiteWebServiceException("No protein sequence is given!");
    } else if (!isDatabaseProtein) {
      proteinSequence = formatter.formatSequence(proteinSequence);
      if (!validator.validateProteinSequence(proteinSequence)) {
        throw new ScansiteWebServiceException("Given protein sequence is invalid!");
      }
    }

    // check stringency input
    HistogramStringency stringency = HistogramStringency.STRINGENCY_HIGH;
    boolean stringencyFound = false;
    for (HistogramStringency str : HistogramStringency.values()) {
      if (str.getName().equalsIgnoreCase(stringencyValue)) {
        stringency = str;
        stringencyFound = true;
      }
    }
    if (!stringencyFound) {
      throw new ScansiteWebServiceException("Invalid stringency value given.");
    }

    try {
      edu.mit.scansite.shared.dispatch.features.ProteinScanResult res = ProteinScanFeature.doProteinScan(isDatabaseProtein, 
          proteinName, proteinSequence, datasourceNickName, motifNickNamesList, mc, stringency);
      if (res.isSuccess()) {
        ProteinScanResult result = new ProteinScanResult();
        result.setProteinName(res.getResults().getProtein().getAccessionNr());
        result.setProteinSequence(res.getResults().getProtein().getSequence());
        if (res.getResults().getHits() == null || res.getResults().getHits().isEmpty()) {
          result.setPredictedSite(new MotifSite[] {});
        } else {
          MotifSite[] sites = new MotifSite[res.getResults().getHits().size()];
          for (int i = 0; i < sites.length; ++i) {
            ScanResultSite site = res.getResults().getHits().get(i);
            sites[i] = new MotifSite(site.getScore(), site.getPercentile(), site.getMotif().getName(), site.getMotif().getShortName(), 
                site.getSite(), site.getSiteSequence());
          }
          result.setPredictedSite(sites);
        }
        
        return result;
      } else {
        throw new ScansiteWebServiceException(res.getFailureMessage());
      }
    } catch (DataAccessException e) {
      throw new ScansiteWebServiceException("Running protein scan failed.");
    }
  }

}
