package edu.mit.scansite.webservice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.mit.scansite.webservice.transferobjects.ProteinScanResult;

@Path("/proteinScan/proteinName={proteinName}/sequence={proteinSequence: [A-Za-z]+}/motifClass={motifClass: [A-Za-z]+}/motifNicknames={motifNicks: [\\S~]*}/stringencyValue={stringencyValue: [A-Za-z]+}")
public class ProteinSequenceScanService extends ProteinScanWebService {
  /**
   * @param proteinName A  name for the protein.
   * @param proteinSequence A protein sequence.
   * @param motifNickNames A list of existing motif-nicknames. These motifs will be used to scan the given protein.
   * @param stringencyValue A stringency value.
   * @return A protein scan result containing the results of the scan.
   */
  @GET
  @Produces( { MediaType.APPLICATION_XML })
  public static ProteinScanResult doProteinScan(
      @DefaultValue("USER_PROTEIN") @PathParam("proteinName") String proteinName, 
      @PathParam("proteinSequence") String proteinSequence,
      @DefaultValue("") @PathParam("motifNicks") String motifNickNames,
      @PathParam("motifClass") String motifClass,
      @PathParam("stringencyValue") String stringencyValue
      ) {
    return doProteinScan(false, proteinName, proteinSequence, null, motifNickNames, motifClass, stringencyValue);
  }
}
