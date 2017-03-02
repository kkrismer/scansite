package edu.mit.scansite.webservice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.mit.scansite.webservice.transferobjects.ProteinScanResult;

@Path("/proteinScan/accession={accession: \\S+}/datasourceNickname={datasourceNick: [A-Za-z]+}/motifClass={motifClass: [A-Za-z]+}/motifNicknames={motifNicks: [\\S~]*}/stringencyValue={stringencyValue: [A-Za-z]+}")
public class ProteinAccessionScanService extends ProteinScanWebService {
  /**
   * @param proteinName A protein accession.
   * @param datasourceNickName The nickname of an existing datasource.
   * @param motifNickNames A list of existing motif-nicknames. These motifs will be used to scan the given protein.
   * @param stringencyValue A stringency value.
   * @return A protein scan result containing the results of the scan.
   */
  @GET
  @Produces( { MediaType.APPLICATION_XML })
  public static ProteinScanResult doProteinScan(
      @PathParam("accession") String proteinName, 
      @PathParam("datasourceNick") String datasourceNickName,
      @DefaultValue("") @PathParam("motifNicks") String motifNickNames, 
      @PathParam("motifClass") String motifClass,
      @PathParam("stringencyValue") String stringencyValue
      ) {
    return doProteinScan(true, proteinName, null, datasourceNickName, motifNickNames, motifClass, stringencyValue);
  }

 }
