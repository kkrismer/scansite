package edu.mit.scansite.webservice.proteinscan;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.webservice.transferobjects.ProteinScanResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static edu.mit.scansite.webservice.proteinscan.ProteinScanUtils.*;

@Path("/proteinscan/identifier={identifier: \\S+}/dsshortname={dsshortname: [A-Za-z]+}/motifclass={motifclass: [A-Za-z]+}{motifshortnames: (/motifshortnames=[\\S~]*)?}/stringency={stringency: [A-Za-z]+}{referenceproteome:(/referenceproteome=[A-Za-z]+)?}")
public class ProteinIdentifierScanService extends ProteinScanWebService {
    /**
     * @param proteinIdentifier        A otherservices accession.
     * @param dataSourceShortName The nickname of an existing datasource.
     * @param motifClass    A list of existing motif-nicknames. These motifs will be used to scan the given otherservices.
     * @param stringency    A stringency value.
     * @return A otherservices scan result containing the results of the scan.
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public static ProteinScanResult doProteinScan(
            @PathParam("identifier") String proteinIdentifier,
            @PathParam("dsshortname") String dataSourceShortName,
            @DefaultValue("") @PathParam("motifshortnames") String motifShortNames,
            @PathParam("motifclass") String motifClass,
            @PathParam("stringency") String stringency,
            @DefaultValue(REFERENCE_VERTEBRATA) @QueryParam("referenceproteome") String referenceProteome) {

        //By making the value optional the identifier in the URL is also part of the value
        motifShortNames = processOptionalParameter(motifShortNames);
        referenceProteome = processOptionalParameter(referenceProteome);

        DataSource dataSource = null;
        try {
            dataSource = ProteinScanUtils.getLocalizationDataSource(TXT_TRY_LATER);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        LightWeightProtein protein = getLightWeightProtein(proteinIdentifier, dataSourceShortName);

        referenceProteome = ProteinScanUtils.checkReferenceProteome(referenceProteome);
        return doProteinScan(protein, referenceProteome, dataSourceShortName, dataSource, motifShortNames, motifClass, stringency);
    }



}