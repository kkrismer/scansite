package edu.mit.scansite.server.features;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.dataaccess.file.SequenceMatchResultFileWriter;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.dispatch.features.SequenceMatchResult;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.OrganismClass;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.ProteinSequenceMatch;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import edu.mit.scansite.shared.transferobjects.SequencePattern;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SequenceMatchFeature {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public SequenceMatchFeature() {
	}

	public SequenceMatchResult doSequenceMatch(
			List<SequencePattern> sequencePatterns, DataSource dataSource,
			RestrictionProperties restrictionProperties,
			boolean limitResultsToPhosphorylatedProteins,
			boolean doCreateFiles, boolean publicOnly, String realPath)
			throws DataAccessException {
		return doSequenceMatch(sequencePatterns, dataSource, restrictionProperties,
				restrictionProperties.getOrganismClass(), HistogramStringency.STRINGENCY_HIGH,
				limitResultsToPhosphorylatedProteins, doCreateFiles, publicOnly, realPath);
	}

	/**
	 * Description not up to date
	 * Runs a sequence match search using the given parameters.
	 * 
	 * @param sequenceMatchRegexs
	 *            An array of regular expressions that are applied to match
	 *            sequences.
	 * @param phosphoSitePositions
	 *            A list of amino acid positions and corresponding expected
	 *            phosphorylation sites for each regular expression
	 * @param datasource
	 *            The datasource that will be searched.
	 * @param organismClass
	 *            An organism class.
	 * @param speciesRegexRestriction
	 *            A regular expression that restricts the search to proteins of
	 *            a single species.
	 * @param numberOfPhosphorylations
	 *            A number of phosphorylations >= 0 and <=3. This parameter is
	 *            only applied to searches using a molecular weight or pI limit.
	 *            The MWs or pIs of single or multiple phosphorylated proteins
	 *            are then checked for the given limit.
	 * @param molWeightFrom
	 *            A lower molecular weight limit.
	 * @param molWeightTo
	 *            An upper molecular weight limit.
	 * @param isoelectricPointFrom
	 *            A lower pI limit.
	 * @param isoelectricPointTo
	 *            An upper pI limit.
	 * @param keywordRegexRestriction
	 *            A regular expression that restricts the search to proteins
	 *            related to a given keyword.
	 * @param doCreateFiles
	 *            TRUE if files should be created on the server, otherwise
	 *            FALSE.
	 * @return An object containing a list of sites that are found searching the
	 *         selected database using the selected motif.
	 */
	public SequenceMatchResult doSequenceMatch(
			List<SequencePattern> sequencePatterns, DataSource dataSource,
			RestrictionProperties restrictionProperties,
			OrganismClass motifOrganismClass, HistogramStringency stringency,
			boolean limitResultsToPhosphorylatedProteins,
			boolean doCreateFiles, boolean publicOnly, String realPath)
			throws DataAccessException {
		DaoFactory factory = ServiceLocator.getDaoFactory();
		restrictionProperties.setSequenceRegEx(SequencePattern
				.getRegExs(sequencePatterns));
		List<Protein> proteins = factory.getProteinDao().get(dataSource,
				restrictionProperties, true, true);
		restrictionProperties.setSequenceRegEx(null);
		SequenceMatchResult result = new SequenceMatchResult();
		result.setProteinsInDbCount(factory.getProteinDao().getProteinCount(
				dataSource));
		result.setDataSource(dataSource);
		result.setRestrictedProteinsInDbCount(proteins.size());
		result.setRestrictionProperties(restrictionProperties);
		result.setSequencePatterns(sequencePatterns);
		result.setCompatibleOrthologyDataSources(factory.getIdentifierDao()
				.getCompatibleOrthologyDataSourcesForIdentifierType(
						dataSource.getIdentifierType()));
		ArrayList<ProteinSequenceMatch> matches = new ArrayList<ProteinSequenceMatch>();
		ProteinSequenceMatch match = null;
		int totalNrOfMatches = 0;
		PhosphoSitesFeature phosphoSiteFinder = new PhosphoSitesFeature();

		for (Protein p : proteins) {
			match = new ProteinSequenceMatch();
			int[] nrOfMatches = new int[sequencePatterns.size()];
			List<ScanResultSite> phosphoSites = new LinkedList<ScanResultSite>();
			for (int i = 0; i < sequencePatterns.size(); ++i) {
				List<Integer> patternStartPositions = new LinkedList<Integer>();
				Pattern regex = Pattern.compile(sequencePatterns.get(i)
						.getRegEx(), Pattern.CASE_INSENSITIVE | Pattern.DOTALL
						| Pattern.MULTILINE);
				Matcher matcher = regex.matcher(p.getSequence());
				nrOfMatches[i] = 0;
				while (matcher.find()) {
					patternStartPositions.add(matcher.start());
					++nrOfMatches[i];
					++totalNrOfMatches;
				}

				if (sequencePatterns.get(i).hasExpectedPhosphorylationSites()) {
					for (Integer patternStartPosition : patternStartPositions) {
						for (int j = 0; j < sequencePatterns.get(i)
								.getPositions().size(); ++j) {
							if (sequencePatterns.get(i).getPositions().get(j)
									.isExpectedPhosphorylationSite()) {
								phosphoSites.addAll(phosphoSiteFinder
										.checkPositionSpecificPhosphoSites(p,
												patternStartPosition + j,
												stringency, motifOrganismClass,
												publicOnly));
							}
						}
					}
				}

			}
			match.setNrOfSequenceMatches(nrOfMatches);
			p.setSequence(""); // remove sequence from protein to not send too
								// much
								// data to client
			match.setProtein(p);
			if (!limitResultsToPhosphorylatedProteins
					|| phosphoSites.size() > 0) {
				match.setPhosphorylationSites(phosphoSites);
				matches.add(match);
			}
		}

		if (matches.size() > ScansiteConstants.SEQUENCE_MATCH_MAX_RESULTS_BROWSER) {
			ArrayList<ProteinSequenceMatch> tempMatches = new ArrayList<ProteinSequenceMatch>(
					ScansiteConstants.SEQUENCE_MATCH_MAX_RESULTS_BROWSER);
			for (int i = 0; i < ScansiteConstants.SEQUENCE_MATCH_MAX_RESULTS_BROWSER
					&& i < matches.size(); ++i) {
				tempMatches.add(matches.get(i));
			}
			matches = tempMatches;
			result.setMoreMatchesThanMaxAllowed(true);
		}

		if (doCreateFiles) {
			SequenceMatchResultFileWriter writer = new SequenceMatchResultFileWriter(
					sequencePatterns);
			try {
				result.setResultFileName(writer.writeResults(realPath, matches)
						.replace(realPath, ""));
			} catch (Exception e) {
				logger.error("Error writing result file for sequence match feature: "
						+ e.toString());
			}
		}

		result.setSequencePatternMatchCount(totalNrOfMatches);
		result.setMatches(matches);

		Runtime.getRuntime().gc();

		return result;
	}

	// /**
	// * Runs a sequence match search using the given parameters. This method is
	// * intended to be used by the web service.
	// *
	// * @param sequenceMatchRegex
	// * A regular expression that is applied to match sequences.
	// * @param datasource
	// * The datasource that will be searched.
	// * @param organismClass
	// * An organism class.
	// * @param speciesRegexRestriction
	// * A regular expression that restricts the search to proteins of a
	// * single species.
	// * @param numberOfPhosphorylations
	// * A number of phosphorylations >= 0 and <=3. This parameter is only
	// * applied to searches using a molecular weight or pI limit. The MWs
	// * or pIs of single or multiple phosphorylated proteins are then
	// * checked for the given limit.
	// * @param molWeightFrom
	// * A lower molecular weight limit.
	// * @param molWeightTo
	// * An upper molecular weight limit.
	// * @param isoelectricPointFrom
	// * A lower pI limit.
	// * @param isoelectricPointTo
	// * An upper pI limit.
	// * @param keywordRegexRestriction
	// * A regular expression that restricts the search to proteins related
	// * to a given keyword.
	// * @return An object containing a list of sites that are found searching
	// the
	// * selected database using the selected motif.
	// */
	// public static SequenceMatchResult doSequenceMatch(String
	// sequenceMatchRegex,
	// DataSource datasource, OrganismClass organismClass,
	// String speciesRegexRestriction, Integer numberOfPhosphorylations,
	// Double molWeightFrom, Double molWeightTo, Double isoelectricPointFrom,
	// Double isoelectricPointTo, String keywordRegexRestriction)
	// throws DataAccessException {
	// return doSequenceMatch(new String[] { sequenceMatchRegex }, null,
	// datasource, organismClass, speciesRegexRestriction,
	// numberOfPhosphorylations, molWeightFrom, molWeightTo,
	// isoelectricPointFrom, isoelectricPointTo, keywordRegexRestriction,
	// false);
	// }

}
