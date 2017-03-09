package edu.mit.scansite.webservice.proteinscan;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.util.Formatter;
import edu.mit.scansite.shared.util.Validator;
import edu.mit.scansite.webservice.exception.ScansiteWebServiceException;
import edu.mit.scansite.webservice.transferobjects.ProteinScanResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static edu.mit.scansite.webservice.proteinscan.ProteinScanUtils.REFERENCE_VERTEBRATA;
import static edu.mit.scansite.webservice.proteinscan.ProteinScanUtils.processOptionalParameter;

@Path("/proteinscan/identifier={identifier}/sequence={sequence: [A-Za-z]+}/motifclass={motifclass: [A-Za-z]+}{motifshortnames: (/motifshortnames=[\\S~]*)?}/stringency={stringency: [A-Za-z]+}{referenceproteome:(/referenceproteome=[A-Za-z]+)?}")
public class ProteinSequenceScanService extends ProteinScanWebService {
  /**
   * @param proteinIdentifier A  name for the otherservices.
   * @param proteinSequence A otherservices sequence.
   * @param motifShortNames A list of existing motif-nicknames. These motifs will be used to scan the given otherservices.
   * @param stringency A stringency value.
   * @return A otherservices scan result containing the results of the scan.
   */
  @GET
  @Produces( { MediaType.APPLICATION_XML })
  public static ProteinScanResult doProteinScan(
      @DefaultValue("USER_PROTEIN") @PathParam("identifier") String proteinIdentifier,
      @PathParam("sequence") String proteinSequence,
      @DefaultValue("") @PathParam("motifshortnames") String motifShortNames,
      @PathParam("motifclass") String motifClass,
      @PathParam("stringency") String stringency,
      @DefaultValue(REFERENCE_VERTEBRATA) @PathParam("referenceproteome") String referenceProteome) {

      //By making the value optional the identifier in the URL is also part of the value
      motifShortNames = processOptionalParameter(motifShortNames);
      referenceProteome = processOptionalParameter(referenceProteome);

      DataSource dataSource = null;
      try {
          dataSource = ProteinScanUtils.getLocalizationDataSource(TXT_TRY_LATER);
      } catch (DataAccessException e) {
          e.printStackTrace();
      }

      Formatter formatter = new Formatter();
      Validator validator = new Validator();
      if (proteinSequence == null || proteinSequence.isEmpty()) {
          throw new ScansiteWebServiceException("No otherservices sequence is given!");
      } else {
          proteinSequence = formatter.formatSequence(proteinSequence);
          if (!validator.validateProteinSequence(proteinSequence)) {
              throw new ScansiteWebServiceException("Given otherservices sequence is invalid!");
          }
      }

      LightWeightProtein protein = new LightWeightProtein(proteinIdentifier, proteinSequence);
      referenceProteome = ProteinScanUtils.checkReferenceProteome(referenceProteome);
      String dataSourceShortName = "";
      return doProteinScan(protein, referenceProteome, dataSourceShortName, dataSource, motifShortNames, motifClass, stringency);
  }
}
