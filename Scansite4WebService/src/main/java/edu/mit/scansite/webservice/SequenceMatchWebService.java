package edu.mit.scansite.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.features.SequenceMatchFeature;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.Datasource;
import edu.mit.scansite.shared.transferobjects.OrganismClass;
import edu.mit.scansite.shared.transferobjects.PatternPosition;
import edu.mit.scansite.shared.transferobjects.ProteinSequenceMatch;
import edu.mit.scansite.shared.transferobjects.SequencePattern;
import edu.mit.scansite.webservice.transferobjects.SequenceMatch;
import edu.mit.scansite.webservice.transferobjects.SequenceMatchResult;

@Path("/sequenceMatch/sequenceMatchRegex={sequenceMatchRegex}/datasourceNickname={datasourceNick: [A-Za-z]+}/organismClass={organismClass: [A-Za-z]+}" +
		"/speciesRestrictionRegex={speciesRestrictionRegex: [\\s\\w]*}/numberOfPhosphorylations={numberOfPhosphorylations: [0-3]}" +
		"/molWeightFrom={molWeightFrom: \\d*}/molWeightTo={molWeightTo: \\d*}" +
		"/isoelectricPointFrom={isoelectricPointFrom: \\d*\\.?\\d*}/isoelectricPointTo={isoelectricPointTo: \\d*\\.?\\d*}" +
		"/keywordRestrictionRegex={keywordRestrictionRegex: [\\s\\w]*}")
public class SequenceMatchWebService {
  /**
   * @param sequenceMatchRegex The sequence pattern you are interested in. Regular expressions allowed.
   * @param datasourceNickName The nickname of the datasource that will be searched (A list of valid datasources
   * can be obtained by the getDatasources() method).
   * @param organismClass A valid organism class (one of those returned by the getOrganismClasses() method).
   * @param speciesRegexRestriction A regular expression that restricts the search to proteins of a single species. 
   * @param numberOfPhosphorylations A number of phosphorylations >= 0 and <=3. This parameter is only applied to
   * searches using a molecular weight or pI limit. The MWs or pIs of single or multiple phosphorylated proteins
   * are then checked for the given limit.
   * @param molWeightFrom A lower molecular weight limit.
   * @param molWeightTo An upper molecular weight limit.
   * @param isoelectricPointFrom A lower pI limit.
   * @param isoelectricPointTo An upper pI limit.
   * @param keywordRegexRestriction A regular expression that restricts the search to proteins related to a given keyword. 
   * @return An object containing a list of protein accessions and counts that say how often the given pattern was found
   * in the proteins' sequences.
   */
  @GET
  @Produces( { MediaType.APPLICATION_XML })
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
      } catch (Exception e) {}
    }
    
    if (molWeightFrom != null && !molWeightFrom.isEmpty()) {
      try {
        mwFrom = Double.parseDouble(molWeightFrom);
      } catch (Exception e) {}
    }
    
    if (molWeightTo != null && !molWeightTo.isEmpty()) {
      try {
        mwTo = Double.parseDouble(molWeightTo);
      } catch (Exception e) {}
    }
    
    if (isoelectricPointFrom != null && !isoelectricPointFrom.isEmpty()) {
      try {
        piFrom = Double.parseDouble(isoelectricPointFrom);
      } catch (Exception e) {}
    }
    
    if (isoelectricPointTo != null && !isoelectricPointTo.isEmpty()) {
      try {
        piTo = Double.parseDouble(isoelectricPointTo);
      } catch (Exception e) {}
    }
        
    Datasource ds = null;
    try {
      ds = ServiceLocator.getInstance().getDaoFactory().getDatasourceDao().get(datasourceNickName);
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
    try {
      edu.mit.scansite.shared.dispatch.features.SequenceMatchResult result = SequenceMatchFeature.doSequenceMatch(patterns, ds, oc, speciesRestrictionRegex, 
          nPhos, mwFrom, mwTo, piFrom, piTo, keywordRestrictionRegex, false);
      SequenceMatch[] matches = null;
      if (result.getMatches() != null && result.getMatches().size() > 0) {
        matches  = new SequenceMatch[result.getMatches().size()];
        int i = 0;
        for (ProteinSequenceMatch match : result.getMatches()) {
          matches[i++] = new SequenceMatch(match.getProtein().getAccessionNr(), match.getNrOfSequenceMatches()[0]);
        }
      } else {
        matches = new SequenceMatch[]{};
      }
      return new SequenceMatchResult(sequenceMatchRegex, datasourceNickName, matches, result.getTotalNrOfMatches(), result.getNrOfProteinsInDb());
    } catch (DataAccessException e) {
      throw new ScansiteWebServiceException("Running sequence match search failed. Please try again later or, if this problem persists, contact the system's administrator.");
    }
  }
}
