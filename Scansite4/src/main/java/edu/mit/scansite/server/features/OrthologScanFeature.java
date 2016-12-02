package edu.mit.scansite.server.features;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava3.alignment.Alignments;
import org.biojava3.alignment.Alignments.PairwiseSequenceAlignerType;
import org.biojava3.alignment.SimpleGapPenalty;
import org.biojava3.alignment.SimpleSubstitutionMatrix;
import org.biojava3.alignment.template.AlignedSequence;
import org.biojava3.alignment.template.GapPenalty;
import org.biojava3.alignment.template.Profile;
import org.biojava3.alignment.template.SequencePair;
import org.biojava3.alignment.template.SubstitutionMatrix;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.dispatch.features.OrthologScanMotifResult;
import edu.mit.scansite.shared.dispatch.features.OrthologScanResult;
import edu.mit.scansite.shared.dispatch.features.OrthologScanSequencePatternResult;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Ortholog;
import edu.mit.scansite.shared.transferobjects.OrthologScanSiteRegion;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import edu.mit.scansite.shared.transferobjects.SequenceAlignment;
import edu.mit.scansite.shared.transferobjects.SequenceAlignmentElement;
import edu.mit.scansite.shared.transferobjects.SequencePattern;

/**
 * @author Konstantin Krismer
 */
public class OrthologScanFeature {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private DbConnector dbConnector;

	public OrthologScanFeature(DbConnector dbConnector) {
		this.dbConnector = dbConnector;
	}

	private static char GAP_SYMBOL = '-';

	private interface ProteinProcessor {
		List<Integer> findPhosphoSitePositions(LightWeightProtein protein)
				throws DataAccessException;

		List<ScanResultSite> checkSite(Protein protein,
				PhosphoSitesFeature phosphoSiteFinder, int position,
				boolean publicOnly) throws DataAccessException;

		int findSiteForAlignment(LightWeightProtein protein);

		boolean init(List<Protein> proteins,
				PhosphoSitesFeature phosphoSiteFinder, boolean publicOnly)
				throws DataAccessException;
	}

	public OrthologScanSequencePatternResult scanOrthologsBySequencePattern(
			SequencePattern sequencePattern, DataSource orthologyDataSource,
			LightWeightProtein lightWeightProtein,
			HistogramStringency stringency, int alignmentRadius, boolean publicOnly)
			throws DatabaseException {
		Protein protein = ServiceLocator
				.getInstance()
				.getDaoFactory(dbConnector)
				.getProteinDao()
				.get(lightWeightProtein.getIdentifier(),
						lightWeightProtein.getDataSource());
		if (protein == null) {
			return new OrthologScanSequencePatternResult(
					"No protein with protein identifier "
							+ lightWeightProtein.getIdentifier()
							+ " and data source "
							+ lightWeightProtein.getDataSource()
									.getDisplayName() + " found");
		}
		final OrthologScanSequencePatternResult result = new OrthologScanSequencePatternResult(
				new OrthologScanResult(orthologyDataSource, protein,
						stringency, alignmentRadius));
		final SubstitutionMatrix<AminoAcidCompound> matrix = new SimpleSubstitutionMatrix<AminoAcidCompound>();
		final GapPenalty penalty = new SimpleGapPenalty();

		result.setSequencePattern(sequencePattern);

		ProteinProcessor processor = new ProteinProcessor() {
			private String targetSequence;

			@Override
			public List<Integer> findPhosphoSitePositions(
					LightWeightProtein protein) throws DataAccessException {
				List<Integer> positions = new LinkedList<Integer>();
				List<Integer> patternStartPositions = new LinkedList<Integer>();
				Pattern regex = Pattern.compile(result.getSequencePattern()
						.getRegEx(), Pattern.CASE_INSENSITIVE | Pattern.DOTALL
						| Pattern.MULTILINE);
				Matcher matcher = regex.matcher(protein.getSequence());

				while (matcher.find()) {
					patternStartPositions.add(matcher.start());
				}

				if (result.getSequencePattern()
						.hasExpectedPhosphorylationSites()) {
					for (Integer patternStartPosition : patternStartPositions) {
						for (int j = 0; j < result.getSequencePattern()
								.getPositions().size(); ++j) {
							if (result.getSequencePattern().getPositions()
									.get(j).isExpectedPhosphorylationSite()) {
								positions.add(patternStartPosition + j);
							}
						}
					}
				}
				return positions;
			}

			@Override
			public List<ScanResultSite> checkSite(Protein protein,
					PhosphoSitesFeature phosphoSiteFinder, int position,
					boolean publicOnly) throws DataAccessException {
				return phosphoSiteFinder.checkPositionSpecificPhosphoSites(
						protein, position, result.getStringency(), publicOnly);
			}

			@Override
			public int findSiteForAlignment(LightWeightProtein protein) {
				return localizeSite(new ProteinSequence(targetSequence),
						new ProteinSequence(protein.getSequence()), matrix,
						penalty);
			}

			@Override
			public boolean init(List<Protein> proteins,
					PhosphoSitesFeature phosphoSiteFinder, boolean publicOnly)
					throws DataAccessException {
				Protein queryProtein = getProteinById(proteins, result
						.getProtein().getIdentifier());
				List<Integer> queryProteinPhosphoSites = findPhosphoSitePositions(queryProtein);
				List<ScanResultSite> sites = new LinkedList<ScanResultSite>();
				for (Integer phosphoSite : queryProteinPhosphoSites) {
					sites.addAll(checkSite(queryProtein, phosphoSiteFinder,
							phosphoSite, publicOnly));
				}
				if (sites.size() > 0) {
					targetSequence = sites.get(0).getSiteSequence()
							.toUpperCase();
					return true;
				} else {
					return false;
				}
			}

		};

		findSites(processor, result, publicOnly);
		return result;
	}

	public OrthologScanMotifResult scanOrthologsByMotifGroup(int sitePosition,
			LightWeightMotifGroup motifGroup, DataSource orthologyDataSource,
			LightWeightProtein lightWeightProtein,
			HistogramStringency stringency, int alignmentRadius, boolean publicOnly)
			throws DatabaseException {
		Protein protein = ServiceLocator
				.getInstance()
				.getDaoFactory(dbConnector)
				.getProteinDao()
				.get(lightWeightProtein.getIdentifier(),
						lightWeightProtein.getDataSource());
		if (protein == null) {
			return new OrthologScanMotifResult(
					"No protein with protein identifier "
							+ lightWeightProtein.getIdentifier()
							+ " and data source "
							+ lightWeightProtein.getDataSource()
									.getDisplayName() + " found");
		}
		final OrthologScanMotifResult result = new OrthologScanMotifResult(
				new OrthologScanResult(orthologyDataSource, protein,
						stringency, alignmentRadius));
		final SubstitutionMatrix<AminoAcidCompound> matrix = new SimpleSubstitutionMatrix<AminoAcidCompound>();
		final GapPenalty penalty = new SimpleGapPenalty();

		result.setSitePosition(sitePosition);
		result.setMotifGroup(motifGroup);

		ProteinProcessor processor = new ProteinProcessor() {
			private int lastPosition;
			private String targetSequence;

			@Override
			public List<Integer> findPhosphoSitePositions(
					LightWeightProtein protein) throws DataAccessException {
				List<Integer> positions = new LinkedList<Integer>();
				lastPosition = localizeSite(
						new ProteinSequence(targetSequence),
						new ProteinSequence(protein.getSequence()), matrix,
						penalty);
				positions.add(lastPosition);
				return positions;
			}

			@Override
			public List<ScanResultSite> checkSite(Protein protein,
					PhosphoSitesFeature phosphoSiteFinder, int position,
					boolean publicOnly) throws DataAccessException {
				return phosphoSiteFinder.checkPositionSpecificPhosphoSites(
						protein, position, result.getStringency(),
						result.getMotifGroup(), publicOnly);
			}

			@Override
			public int findSiteForAlignment(LightWeightProtein protein) {
				return lastPosition;
			}

			@Override
			public boolean init(List<Protein> proteins,
					PhosphoSitesFeature phosphoSiteFinder, boolean publicOnly) {
				try {
					targetSequence = extractSiteSequence(result.getProtein()
							.getSequence(), result.getSitePosition());
					return true;
				} catch (IllegalArgumentException ex) {
					return false;
				}
			}
		};

		findSites(processor, result, publicOnly);

		return result;
	}

	private void findSites(ProteinProcessor processor, OrthologScanResult result, boolean publicOnly)
			throws DatabaseException {
		List<Protein> proteins = ServiceLocator
				.getInstance()
				.getDaoFactory(dbConnector)
				.getOrthologDao()
				.getOrthologs(result.getOrthologyDataSource(),
						result.getProtein().getDataSource(),
						result.getProtein().getIdentifier());
		PhosphoSitesFeature phosphoSiteFinder = new PhosphoSitesFeature(
				dbConnector);
		List<Ortholog> orthologs = new LinkedList<Ortholog>();
		List<OrthologScanSiteRegion> siteRegions = new LinkedList<OrthologScanSiteRegion>();
		Ortholog ortholog = null;
		int nrOfConservedPhosphoSites = 0;

		if (proteins != null && proteins.size() > 1) {
			Collections.sort(proteins, new Comparator<Protein>() {
				@Override
				public int compare(Protein proteinA, Protein proteinB) {
					return proteinA.getIdentifier().compareTo(
							proteinB.getIdentifier());
				}
			});

			if (processor.init(proteins, phosphoSiteFinder, publicOnly)) {
				for (Protein protein : proteins) {
					ortholog = new Ortholog();
					List<Integer> phosphoSitePositions = processor
							.findPhosphoSitePositions(protein);
					List<ScanResultSite> phosphoSites = new LinkedList<ScanResultSite>();
					List<Integer> nonhitPositions = new LinkedList<Integer>();

					protein.setSpecies(ServiceLocator
							.getInstance()
							.getDaoFactory(dbConnector)
							.getTaxonDao()
							.getById(protein.getSpecies().getId(),
									result.getProtein().getDataSource()));

					for (Integer sitePosition : phosphoSitePositions) {
						List<ScanResultSite> currentPhosphoSites = processor
								.checkSite(protein, phosphoSiteFinder,
										sitePosition, publicOnly);
						if (currentPhosphoSites != null
								&& currentPhosphoSites.size() > 0) {
							phosphoSites.addAll(currentPhosphoSites);
						} else {
							nonhitPositions.add(sitePosition);
						}
					}
					phosphoSitePositions.removeAll(nonhitPositions);

					if (phosphoSitePositions.size() == 0) {
						phosphoSitePositions.add(processor
								.findSiteForAlignment(protein));
					}

					for (Integer sitePosition : phosphoSitePositions) {
						siteRegions.add(buildAlignmentRegion(
								protein.getSequence(), protein.getIdentifier(),
								protein.getSpecies().getName(), sitePosition,
								isValidPhosphoSite(sitePosition, phosphoSites),
								result.getAlignmentRadius()));
					}

					ortholog.setProtein(protein);

					if (phosphoSites != null && phosphoSites.size() > 0) {
						++nrOfConservedPhosphoSites;
					}
					ortholog.setPhosphorylationSites(phosphoSites);
					orthologs.add(ortholog);
				}

				List<ProteinSequence> downstreamSequences = getDownstreamSequences(siteRegions);
				List<ProteinSequence> upstreamSequences = getUpstreamSequences(siteRegions);
				Profile<ProteinSequence, AminoAcidCompound> downstreamProfile = Alignments
						.getMultipleSequenceAlignment(downstreamSequences);
				Profile<ProteinSequence, AminoAcidCompound> upstreamProfile = Alignments
						.getMultipleSequenceAlignment(upstreamSequences);
				result.setSequenceAlignment(combineAlignments(
						downstreamProfile, upstreamProfile, siteRegions));
			} else {
				for (Protein protein : proteins) {
					ortholog = new Ortholog();
					ortholog.setProtein(protein);
					orthologs.add(ortholog);
				}
			}
		}

		result.setNrOfConservedPhosphoSites(nrOfConservedPhosphoSites);
		result.setOrthologs(orthologs);
	}

	private boolean isValidPhosphoSite(Integer sitePosition,
			List<ScanResultSite> phosphoSites) {
		for (ScanResultSite site : phosphoSites) {
			if (site.getPosition() == sitePosition) {
				return true;
			}
		}
		return false;
	}

	private Protein getProteinById(List<Protein> proteins, String identifier) {
		for (Protein protein : proteins) {
			if (protein.getIdentifier().equalsIgnoreCase(identifier)) {
				return protein;
			}
		}
		throw new IllegalArgumentException();
	}

	private String extractSiteSequence(String sequence, int sitePosition) {
		return sequence.substring(sitePosition - ScansiteConstants.HALF_WINDOW,
				sitePosition + ScansiteConstants.HALF_WINDOW);
	}

	private List<ProteinSequence> getDownstreamSequences(
			List<OrthologScanSiteRegion> siteRegions) {
		List<ProteinSequence> downstreamSequences = new LinkedList<ProteinSequence>();

		for (OrthologScanSiteRegion siteRegion : siteRegions) {
			downstreamSequences.add(new ProteinSequence(siteRegion
					.getDownstreamRegion()));
		}

		return downstreamSequences;
	}

	private List<ProteinSequence> getUpstreamSequences(
			List<OrthologScanSiteRegion> siteRegions) {
		List<ProteinSequence> upstreamSequences = new LinkedList<ProteinSequence>();

		for (OrthologScanSiteRegion siteRegion : siteRegions) {
			upstreamSequences.add(new ProteinSequence(siteRegion
					.getUpstreamRegion()));
		}

		return upstreamSequences;
	}

	private OrthologScanSiteRegion buildAlignmentRegion(String sequence,
			String identifier, String speciesName, int phosphoSitePosition,
			boolean isPredictedSite, int alignmentRadius) {
		OrthologScanSiteRegion siteRegion = new OrthologScanSiteRegion();
		int regionStart;
		int regionEnd;

		siteRegion.setIdentifier(identifier);
		siteRegion.setSpecies(speciesName);
		siteRegion.setPredictedSite(isPredictedSite);

		if (phosphoSitePosition < alignmentRadius) {
			regionStart = 0;
		} else {
			regionStart = phosphoSitePosition - alignmentRadius;
		}
		if (sequence.length() < phosphoSitePosition + alignmentRadius) {
			regionEnd = sequence.length() - 1;
		} else {
			regionEnd = phosphoSitePosition + alignmentRadius;
		}

		siteRegion.setDownstreamRegion(sequence.substring(regionStart,
				phosphoSitePosition).toUpperCase());
		siteRegion.setUpstreamRegion(sequence.substring(
				phosphoSitePosition + 1, regionEnd).toUpperCase());
		siteRegion.setPhosphorylatedAminoAcid(sequence
				.charAt(phosphoSitePosition));

		return siteRegion;
	}

	private int localizeSite(ProteinSequence targetSequence,
			ProteinSequence unlocalizedSiteSequence,
			SubstitutionMatrix<AminoAcidCompound> matrix, GapPenalty penalty) {
		SequencePair<ProteinSequence, AminoAcidCompound> pair = Alignments
				.getPairwiseAlignment(unlocalizedSiteSequence, targetSequence,
						PairwiseSequenceAlignerType.LOCAL, penalty, matrix);

		// remove gaps
		int offset = targetSequence.getSequenceAsString().indexOf(
				pair.getTarget().getSequenceAsString()
						.replaceAll(Character.toString(GAP_SYMBOL), ""));
		try {
			return pair.getIndexInQueryAt(ScansiteConstants.HALF_WINDOW
					- offset);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			// TODO used to be unreliable - solved?
			return 1;
		}
	}

	private SequenceAlignment combineAlignments(
			Profile<ProteinSequence, AminoAcidCompound> downstreamProfile,
			Profile<ProteinSequence, AminoAcidCompound> upstreamProfile,
			List<OrthologScanSiteRegion> siteRegions) {
		SequenceAlignment alignment = new SequenceAlignment();

		if (downstreamProfile.getAlignedSequences().size() > 0) {
			alignment.setPhosphoSitePosition(downstreamProfile
					.getAlignedSequences().get(0).getSequenceAsString()
					.length());
		}

		for (OrthologScanSiteRegion siteRegion : siteRegions) {
			alignment.addSequence(new SequenceAlignmentElement(siteRegion
					.getIdentifier(), findAlignment(
					downstreamProfile.getAlignedSequences(),
					siteRegion.getDownstreamRegion())
					+ siteRegion.getPhosphorylatedAminoAcid()
					+ findAlignment(upstreamProfile.getAlignedSequences(),
							siteRegion.getUpstreamRegion()), siteRegion
					.getSpecies(), siteRegion.isPredictedSite()));
		}
		return alignment;
	}

	private String findAlignment(
			List<AlignedSequence<ProteinSequence, AminoAcidCompound>> alignedSequences,
			String originalSequence) {
		for (AlignedSequence<ProteinSequence, AminoAcidCompound> alignedSequence : alignedSequences) {
			if (alignedSequence.getOriginalSequence().getSequenceAsString()
					.equals(originalSequence)) {
				return alignedSequence.getSequenceAsString();
			}
		}
		return "";
	}
}
