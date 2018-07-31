package edu.mit.scansite.webservice.proteinscan;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.webservice.transferobjects.ProteinScanResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static edu.mit.scansite.webservice.proteinscan.ProteinScanUtils.*;

/**
 * @author Tobieh
 * @author Thomas Bernwinkler
 * @author Konstantin Krismer
 */
//@Path("/proteinscan/identifier={identifier: \\S+}/dsshortname={dsshortname: [A-Za-z]+}/motifclass={motifclass: [A-Za-z]+}{motifshortnames: (/motifshortnames=[\\S~]*)?}/stringency={stringency: [A-Za-z]+}{referenceproteome:(/referenceproteome=[A-Za-z]+)?}")

//@Path("/proteinscan/identifier={identifier: \\S+}{dsshortname: (/dsshortname=[A-Za-z]+)?}{motifclass: (/motifclass=[A-Za-z]+)?}{motifshortnames: (/motifshortnames=[\\S~]*)?}{stringency: (/stringency=[A-Za-z]+)?}{referenceproteome: (/referenceproteome=[A-Za-z]+)?}")
//@Path("/proteinscan/identifier={identifier: \\S+}/dsshortname={dsshortname: [A-Za-z]+}/motifclass={motifclass: [A-Za-z]+}{motifshortnames: (/motifshortnames=[\\S~]*)?}{stringency:(/stringency=[A-Za-z]+)?}{referenceproteome:(/referenceproteome=[A-Za-z]+)?}")
@Path("/proteinscan/identifier={identifier: [a-zA-Z0-9_.-]+}{dsshortname: (/dsshortname=[A-Za-z]+)?}{motifclass: (/motifclass=[A-Za-z]+)?}{motifshortnames: (/motifshortnames=[a-zA-Z0-9_.,-]*)?}{stringency: (/stringency=[A-Za-z]+)?}{referenceproteome: (/referenceproteome=[A-Za-z]+)?}")
public class ProteinIdentifierScanService extends ProteinScanWebService {
	/**
	 * @param proteinIdentifier
	 *            The protein identifier that is used by the data source
	 * @param dataSourceShortName
	 *            The short name of an existing data source (defaults to
	 *            "swissprot").
	 * @param motifClass
	 *            A list of existing motif-nicknames. These motifs will be used to
	 *            scan the given otherservices.
	 * @param stringency
	 *            A stringency value.
	 * @return The result of the protein scan
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML })
	public static ProteinScanResult doProteinScan(@PathParam("identifier") String proteinIdentifier,
			@DefaultValue("swissprot") @PathParam("dsshortname") String dataSourceShortName,
			@DefaultValue("") @PathParam("motifshortnames") String motifShortNames,
			@DefaultValue("MAMMALIAN") @PathParam("motifclass") String motifClass,
			@DefaultValue("High") @PathParam("stringency") String stringency,
			@DefaultValue(REFERENCE_VERTEBRATA) @QueryParam("referenceproteome") String referenceProteome) {

		// By making the value optional the identifier in the URL is also part of the
		// value
		dataSourceShortName = processOptionalParameter(dataSourceShortName);
		motifShortNames = processOptionalParameter(motifShortNames);
		motifClass = processOptionalParameter(motifClass);
		stringency = processOptionalParameter(stringency);
		referenceProteome = processOptionalParameter(referenceProteome);
		
		// @DefaultValue doesn't seem to work
		if(dataSourceShortName == null || dataSourceShortName.isEmpty()) {
			dataSourceShortName = "swissprot";
		}
		if(motifClass == null || motifClass.isEmpty()) {
			motifClass = "MAMMALIAN";
		}
		if(stringency == null || stringency.isEmpty()) {
			stringency = "High";
		}
		if(referenceProteome == null || referenceProteome.isEmpty()) {
			referenceProteome = REFERENCE_VERTEBRATA;
		}

		DataSource localizationDataSource = null;
		try {
			localizationDataSource = ProteinScanUtils.getLocalizationDataSource(TXT_TRY_LATER);
		} catch (DataAccessException e) {
			e.printStackTrace();
			localizationDataSource = null;
		}

		LightWeightProtein protein = getLightWeightProtein(proteinIdentifier, dataSourceShortName);

		referenceProteome = ProteinScanUtils.checkReferenceProteome(referenceProteome);
		return doProteinScan(protein, referenceProteome, dataSourceShortName, localizationDataSource, motifShortNames,
				motifClass, stringency);
	}
}