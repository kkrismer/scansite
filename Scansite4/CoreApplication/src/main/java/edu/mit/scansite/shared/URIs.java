package edu.mit.scansite.shared;

import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.IdentifierType;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;

/**
 * All static URIs that are used as links are saved here.
 * 
 * @author tobieh
 * @author Konstantin Krismer
 */
public class URIs {
	public static final String BLAST_DIRECT = "http://blast.ncbi.nlm.nih.gov/Blast.cgi?PROGRAM=blastp&PAGE_TYPE=BlastSearch&QUERY=";

	public static final String GENEINFO_GENECARD = "http://www.genecards.org/";
	public static final String GENEINFO_GENECARD_KEYWORD = "http://www.genecards.org/index.php?path=/Search/keyword/";
	public static final String GENEINFO_GENECARD_DIRECT = "http://www.genecards.org/cgi-bin/carddisp.pl?gene=";

	public static final String PFAM_DOMAIN_DIRECT = "http://pfam.sanger.ac.uk/family/";
	public static final String PFAM_PROTEIN_DIRECT = "http://pfam.sanger.ac.uk/protein/";

	public static final String PHOSPHOSITE = "http://www.phosphosite.org/";
	public static final String PHOSPHOSITE_DIRECT_PROTEIN_URL = "http://www.phosphosite.org/uniprotAccAction.do?id=";

	public static final String UNIPROT = "http://www.uniprot.org/";
	private static final String UNIPROT_DIRECT = "http://www.uniprot.org/uniprot/";
	public static final String ENSEMBL = "http://www.ensembl.org/";
	private static final String ENSEMBL_DIRECT = "http://ensembl.org/Multi/Search/Results?species=all;idx=;q=";
	public static final String YEAST_SGD = "http://www.yeastgenome.org/";
	private static final String YEAST_SGD_DIRECT = "http://www.yeastgenome.org/cgi-bin/locus.fpl?locus=";
	public static final String NCBI_PROTEIN = "http://www.ncbi.nlm.nih.gov/protein/";
	private static final String NCBI_PROTEIN_DIRECT = "http://www.ncbi.nlm.nih.gov/protein/";

	public static String directUniprotLink(String id) {
		return UNIPROT_DIRECT + id;
	}

	public static String directEnsemblLink(String id) {
		return ENSEMBL_DIRECT + id;
	}

	public static String directYeastLink(String id) {
		return YEAST_SGD_DIRECT + id;
	}

	public static String directNcbiProtLink(String id) {
		return NCBI_PROTEIN_DIRECT + id;
	}

	public static String directPfamLink(String domainId) {
		return PFAM_DOMAIN_DIRECT + domainId;
	}

	/**
	 * Examples by Sasha Tkachev from Phosphosite:
	 * http://www.phosphosite.org/uniprotAccAction.do?id=P31749
	 * http://www.phosphosite.org/uniprotAccSiteAction.do?id=P31749&site=T308
	 * Sites available @
	 * http://www.phosphosite.org/downloads/Phosphorylation_site_dataset.gz
	 * 
	 * @param uniprotAccession
	 *            the protein's uniprot id.
	 * @return A direct url to phosphosite's protein entry page.
	 */
	public static final String directPhosphositeProteinLink(
			String uniprotAccession) {
		return PHOSPHOSITE_DIRECT_PROTEIN_URL + uniprotAccession;
	}

	public static String getDirectBlastLink(String sequence) {
		return BLAST_DIRECT + sequence;
	}

	public static String getDirectIdentifierInfoLink(String identifier,
			IdentifierType identifierType) {
		if (identifier != null && identifierType != null) {
			switch (identifierType.getId()) {
			case 1:
				return directEnsemblLink(identifier);
			case 2:
				return directEnsemblLink(identifier);
			case 3:
				return directNcbiProtLink(identifier);
			case 4:
				return GENEINFO_GENECARD_DIRECT + identifier;
			case 5:
				return directYeastLink(identifier);
			case 6:
				return directUniprotLink(identifier);
			default:
				return null;
			}
		} else {
			return null;
		}
	}

	public static String getDirectIdentifierInfoLink(Identifier identifier) {
		if (identifier != null) {
			return getDirectIdentifierInfoLink(identifier.getValue(),
					identifier.getType());
		} else {
			return null;
		}
	}

	public static String getDirectIdentifierInfoLink(LightWeightProtein protein) {
		if (protein != null && protein.getDataSource() != null) {
			return getDirectIdentifierInfoLink(protein.getIdentifier(), protein
					.getDataSource().getIdentifierType());
		} else {
			return null;
		}
	}

	public static String directPfamLinkByProteinAccession(String accessionNr) {
		return PFAM_PROTEIN_DIRECT + accessionNr;
	}
}
