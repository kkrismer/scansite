package edu.mit.scansite.webservice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.features.DatabaseSearchFeature;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.features.DatabaseSearchResult;
import edu.mit.scansite.shared.transferobjects.Datasource;
import edu.mit.scansite.shared.transferobjects.DbSearchScanResultSite;
import edu.mit.scansite.shared.transferobjects.OrganismClass;
import edu.mit.scansite.webservice.transferobjects.DbSearchResult;
import edu.mit.scansite.webservice.transferobjects.MotifSiteDbSearch;

@Path("/databaseSearch/motifNickname={motifNick: \\S+}/datasourceNickname={datasourceNick: [A-Za-z]+}/organismClass={organismClass: [A-Za-z]+}" +
        "/speciesRestrictionRegex={speciesRestrictionRegex: [\\s\\w]*}/numberOfPhosphorylations={numberOfPhosphorylations: [0-3]}" +
        "/molWeightFrom={molWeightFrom: \\d*}/molWeightTo={molWeightTo: \\d*}" +
        "/isoelectricPointFrom={isoelectricPointFrom: \\d*\\.?\\d*}/isoelectricPointTo={isoelectricPointTo: \\d*\\.?\\d*}" +
        "/keywordRestrictionRegex={keywordRestrictionRegex: [\\w\\s]*}/sequenceRestrictionRegex={sequenceRestrictionRegex: [\\w\\s]*}")
public class DatabaseSearchWebService {
  /**
   * @param motifNickName The nickname of the motif that will be searched for (A list of valid motifs can be
   * obtained by the getMotifDefinitions() method).
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
   * @param sequenceRegexRestriction A pattern of AminoAcids that a protein's sequence has to contain in order to show up in the result list.
   * @return An object containing a the sites that are found searching the selected database using the selected motif and considering the given restrictions.
   */
  @GET
  @Produces( { MediaType.APPLICATION_XML })
  public static DbSearchResult doDatabaseSearch(
      @PathParam("motifNick") String motifNickName, 
      @PathParam("datasourceNick") String datasourceNickName,
      @PathParam("organismClass") String organismClass,
      @DefaultValue("") @PathParam("speciesRestrictionRegex") String speciesRestrictionRegex,
      @DefaultValue("") @PathParam("numberOfPhosphorylations") String numberOfPhosphorylations,
      @DefaultValue("") @PathParam("molWeightFrom") String molWeightFrom,
      @DefaultValue("") @PathParam("molWeightTo") String molWeightTo,
      @DefaultValue("") @PathParam("isoelectricPointFrom") String isoelectricPointFrom,
      @DefaultValue("") @PathParam("isoelectricPointTo") String isoelectricPointTo,
      @DefaultValue("") @PathParam("keywordRestrictionRegex") String keywordRestrictionRegex,
      @DefaultValue("") @PathParam("sequenceRestrictionRegex") String sequenceRestrictionRegex
      ) {

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
    
    if (motifNickName == null || motifNickName.isEmpty()) {
      throw new ScansiteWebServiceException("No motif nick given!");
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
    
    try {
      DatabaseSearchResult result = DatabaseSearchFeature.doDatabaseSearch(motifNickName, ds, oc, speciesRestrictionRegex, 
          nPhos, mwFrom, mwTo, piFrom, piTo, keywordRestrictionRegex, sequenceRestrictionRegex);
      
      MotifSiteDbSearch [] sites;
      if (result.isSuccess() && result.getDbSearchSites() != null && result.getDbSearchSites().size() > 0) {
        sites = new MotifSiteDbSearch[result.getDbSearchSites().size()];
        int i = 0;
        for (DbSearchScanResultSite site : result.getDbSearchSites()) {
          sites[i++] = new MotifSiteDbSearch(site.getScore(), site.getSite().getPercentile(), site.getSite().getMotif().getName(), site.getSite().getMotif().getShortName(),
              site.getSite().getSite(), site.getSite().getSiteSequence(), 
              site.getProtein().getAccessionNr());
        }
      } else {
        sites = new MotifSiteDbSearch[] {};
      }
      return new DbSearchResult(sites, datasourceNickName, result.getMedian(), result.getMedianAbsDev(), result.getNrOfProteinsFound(), result.getTotalNrOfSites(), result.getTotalNrOfProteinsInDb());
    } catch (DataAccessException e) {
      throw new ScansiteWebServiceException("Running database search failed. Please try again later or, if this problem persists, contact the system's administrator!");
    }
  }
}
