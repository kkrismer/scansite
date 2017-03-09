package edu.mit.scansite.webservice.otherservices;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.features.SequenceMatchFeature;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.*;
import edu.mit.scansite.webservice.exception.ScansiteWebServiceException;
import edu.mit.scansite.webservice.transferobjects.SequenceMatch;
import edu.mit.scansite.webservice.transferobjects.SequenceMatchResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Path("/sequenceMatch/sequenceMatchRegex={sequenceMatchRegex}/datasourceNickname={datasourceNick: [A-Za-z]+}/organismClass={organismClass: [A-Za-z]+}" +
        "/speciesRestrictionRegex={speciesRestrictionRegex: [\\s\\w]*}/numberOfPhosphorylations={numberOfPhosphorylations: [0-3]}" +
        "/molWeightFrom={molWeightFrom: \\d*}/molWeightTo={molWeightTo: \\d*}" +
        "/isoelectricPointFrom={isoelectricPointFrom: \\d*\\.?\\d*}/isoelectricPointTo={isoelectricPointTo: \\d*\\.?\\d*}" +
        "/keywordRestrictionRegex={keywordRestrictionRegex: [\\s\\w]*}")
public class SequenceMatchWebService {
    /**
     * @param sequenceMatchRegex       The sequence pattern you are interested in. Regular expressions allowed.
     * @param datasourceNickName       The nickname of the datasource that will be searched (A list of valid datasources
     *                                 can be obtained by the getDatasources() method).
     * @param organismClass            A valid organism class (one of those returned by the getOrganismClasses() method).
     * @param speciesRestrictionRegex  A regular expression that restricts the search to proteins of a single species.
     * @param numberOfPhosphorylations A number of phosphorylations >= 0 and <=3. This parameter is only applied to
     *                                 searches using a molecular weight or pI limit. The MWs or pIs of single or multiple phosphorylated proteins
     *                                 are then checked for the given limit.
     * @param molWeightFrom            A lower molecular weight limit.
     * @param molWeightTo              An upper molecular weight limit.
     * @param isoelectricPointFrom     A lower pI limit.
     * @param isoelectricPointTo       An upper pI limit.
     * @param keywordRestrictionRegex  A regular expression that restricts the search to proteins related to a given keyword.
     * @return An object containing a list of otherservices accessions and counts that say how often the given pattern was found
     * in the proteins' sequences.
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public static SequenceMatchResult doSequenceMatch(
            @PathParam("sequenceMatchRegex") String sequenceMatchRegex,
            @PathParam("datasourceNick") String datasourceNickName,
            @PathParam("organismClass") String organismClass,
            @DefaultValue("") @PathParam("speciesRestrictionRegex") String speciesRestrictionRegex,
            @DefaultValue("") @PathParam("numberOfPhosphorylations") String numberOfPhosphorylations,
            @DefaultValue("") @PathParam("molWeightFrom") String molWeightFrom,
            @DefaultValue("") @PathParam("molWeightTo") String molWeightTo,
            @DefaultValue("") @PathParam("isoelectricPointFrom") String isoelectricPointFrom,
            @DefaultValue("") @PathParam("isoelectricPointTo") String isoelectricPointTo,
            @DefaultValue("") @PathParam("keywordRestrictionRegex") String keywordRestrictionRegex
    ) {
        if (sequenceMatchRegex == null || sequenceMatchRegex.isEmpty()) {
            throw new ScansiteWebServiceException("No sequence match regular expression given.");
        }
        Integer nPhos = 0;
        Double piFrom = null;
        Double piTo = null;
        Double mwFrom = null;
        Double mwTo = null;

        if (speciesRestrictionRegex != null && speciesRestrictionRegex.isEmpty()) {
            speciesRestrictionRegex = null;
        }

        if (keywordRestrictionRegex != null && keywordRestrictionRegex.isEmpty()) {
            keywordRestrictionRegex = null;
        }

        if (numberOfPhosphorylations != null && !numberOfPhosphorylations.isEmpty()) {
            try {
                nPhos = Integer.parseInt(numberOfPhosphorylations);
                if (nPhos < 0 || nPhos > 3) {
                    nPhos = 0;
                }
            } catch (Exception e) {
            }
        }

        if (molWeightFrom != null && !molWeightFrom.isEmpty()) {
            try {
                mwFrom = Double.parseDouble(molWeightFrom);
            } catch (Exception e) {
            }
        }

        if (molWeightTo != null && !molWeightTo.isEmpty()) {
            try {
                mwTo = Double.parseDouble(molWeightTo);
            } catch (Exception e) {
            }
        }

        if (isoelectricPointFrom != null && !isoelectricPointFrom.isEmpty()) {
            try {
                piFrom = Double.parseDouble(isoelectricPointFrom);
            } catch (Exception e) {
            }
        }

        if (isoelectricPointTo != null && !isoelectricPointTo.isEmpty()) {
            try {
                piTo = Double.parseDouble(isoelectricPointTo);
            } catch (Exception e) {
            }
        }

        DataSource ds = null;
        try {
            ds = ServiceLocator.getWebServiceInstance().getDaoFactory().getDataSourceDao().get(datasourceNickName);
        } catch (Exception e) {
            throw new ScansiteWebServiceException("No valid datasource nickname given.");
        }
        if (datasourceNickName == null || datasourceNickName.isEmpty()) {
            throw new ScansiteWebServiceException("No valid datasource nickname given.");
        }

        OrganismClass oc = OrganismClass.getByStringRepresentation(organismClass);
        if (oc == null) {
            throw new ScansiteWebServiceException("No valid organismClass given.");
        }

        List<SequencePattern> patterns = new ArrayList<SequencePattern>(1);
        SequencePattern pattern = new SequencePattern();
        pattern.addPosition(new PatternPosition(sequenceMatchRegex, false,
                false));
        patterns.add(pattern);


        List<SequencePattern> sequencePatterns = new ArrayList<>();
        SequencePattern sequencePattern = new SequencePattern();
        sequencePatterns.add(sequencePattern); //sequenceMatchRegex
//        ds
        List<String> seqList = new ArrayList<String>();
        seqList.add(sequenceMatchRegex);
        RestrictionProperties restrictionProperties = new RestrictionProperties(oc, speciesRestrictionRegex, nPhos, mwFrom, mwTo, piFrom, piTo, keywordRestrictionRegex, seqList);
//        oc
//        HistogramStringency.valueOf("high");
        boolean limitResultsToPhosphorylatedProteins = (nPhos > 0); // [todo:] again... no idea
        boolean doCreateFiles = false;
        boolean publicOnly = true;
        String realPath = "";

        try {
            Properties config = ServiceLocator.getWebServiceInstance().getDbAccessFile();
            DbConnector connector = new DbConnector(config);
            SequenceMatchFeature feature = new SequenceMatchFeature(connector);


            edu.mit.scansite.shared.dispatch.features.SequenceMatchResult result = feature.doSequenceMatch(sequencePatterns, ds,
                    restrictionProperties, limitResultsToPhosphorylatedProteins, doCreateFiles, publicOnly, realPath);
            SequenceMatch[] matches = null;
            if (result.getMatches() != null && result.getMatches().size() > 0) {
                matches = new SequenceMatch[result.getMatches().size()];
                int i = 0;
                for (ProteinSequenceMatch match : result.getMatches()) {
                    matches[i++] = new SequenceMatch(match.getProtein().getIdentifier(), match.getNrOfSequenceMatches()[0]);
                }
            } else {
                matches = new SequenceMatch[]{};
            }
            return new SequenceMatchResult(sequenceMatchRegex, datasourceNickName, matches, result.getSequencePatternMatchCount(), result.getProteinsInDbCount());
        } catch (DataAccessException e) {
            throw new ScansiteWebServiceException("Running sequence match search failed. Please try again later or, if this problem persists, contact the system's administrator.");
        } catch (DatabaseException e) {
            e.printStackTrace();
            throw new ScansiteWebServiceException("Running sequence match search failed. Please try again later or, if this problem persists, contact the system's administrator.");
        }
    }
}
