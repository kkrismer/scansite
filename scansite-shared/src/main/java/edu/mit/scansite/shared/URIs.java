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
	public static final String BLAST_DIRECT = "https://blast.ncbi.nlm.nih.gov/Blast.cgi?PROGRAM=blastp&PAGE_TYPE=BlastSearch&QUERY=";

	public static final String GENEINFO_GENECARD = "https://www.genecards.org/";
	public static final String GENEINFO_GENECARD_KEYWORD = "https://www.genecards.org/Search/Keyword?queryString=";
	public static final String GENEINFO_GENECARD_DIRECT = "https://www.genecards.org/cgi-bin/carddisp.pl?gene=";

	public static final String PFAM_DOMAIN_DIRECT = "https://pfam.xfam.org/family/";
	public static final String PFAM_PROTEIN_DIRECT = "https://pfam.xfam.org/protein/";

	public static final String PHOSPHOSITE = "https://www.phosphosite.org/";
	public static final String PHOSPHOSITE_DIRECT_PROTEIN_URL = "https://www.phosphosite.org/uniprotAccAction.do?id=";

	public static final String UNIPROT = "https://www.uniprot.org/";
	private static final String UNIPROT_DIRECT = "https://www.uniprot.org/uniprot/";
	public static final String ENSEMBL = "https://www.ensembl.org/";
	private static final String ENSEMBL_DIRECT = "https://ensembl.org/Multi/Search/Results?q=";
	public static final String YEAST_SGD = "https://www.yeastgenome.org/";
	private static final String YEAST_SGD_DIRECT = "https://www.yeastgenome.org/locus/";
	public static final String NCBI_PROTEIN = "https://www.ncbi.nlm.nih.gov/protein/";
	private static final String NCBI_PROTEIN_DIRECT = "https://www.ncbi.nlm.nih.gov/protein/";

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
