package edu.mit.scansite.server.dataaccess.commands;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.file.ConfigReader;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.util.Formatter;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class CommandConstants {
	private static CommandConstants instance;

	private String tUsers;
	private String tNews;
	private String tDataSources;
	private String tDataSourceTypes;
	private String tTaxa;
	private String tProteins;
	private String tMotifs;
	private String tMotifIdentifierMapping;
	private String tMotifMatrixData;
	private String tMotifGroups;
	private String tAnnotationTypes;
	private String tAnnotations;
	private String tHistograms;
	private String tHistogramData;
	private String tSiteEvidence;
	private String tEvidenceResources;
	private String tOrthologs;
	private String tLocalization;
	private String tIdentifierTypes;
	private String tLocalizationTypes;
	private String tEvidenceCodes;
	private String tGOTerms;
	private String tLocalizationGOTerms;

	private String cUsersEmail;
	private String cUsersFirstName;
	private String cUsersLastName;
	private String cUsersPassword;
	private String cUsersIsAdmin;
	private String cUsersIsSuperAdmin;

	private String cNewsId;
	private String cNewsTitle;
	private String cNewsText;
	private String cNewsDate;

	private String cDataSourcesId;
	private String cDataSourcesShortName;
	private String cDataSourcesDisplayName;
	private String cDataSourcesDescription;
	private String cDataSourcesVersion;
	private String cDataSourcesLastUpdate;
	private String cDataSourcesIsPrimary;

	private String cDataSourceTypesId;
	private String cDataSourceTypesShortName;
	private String cDataSourceTypesDisplayName;

	private String cTaxaId;
	private String cTaxaName;
	private String cTaxaParentTaxon;
	private String cTaxaIsSpecies;

	private String cProteinsIdentifier;
	private String cProteinsSequence;
	private String cProteinsPI;
	private String cProteinsClass;
	private String cProteinsPiPhos1;
	private String cProteinsPiPhos2;
	private String cProteinsPiPhos3;
	private String cProteinsMolWeight;

	private String cAnnotationTypesId;
	private String cAnnotationTypesTitle;

	private String cAnnotationsId;
	private String cAnnotationsAnnotation;

	private String cMotifsId;
	private String cMotifsDisplayName;
	private String cMotifsShortName;
	private String cMotifsIsPublic;
	private String cMotifsOptScore;
	private String cMotifsMotifClass;

	private String cMotifIdentifierMappingId;
	private String cMotifIdentifierMappingIdentifier;

	private String cMatrixDataPosition;
	private String cMatrixDataScorePrefix;
	private String cMatrixDataScoreEnd;
	private String cMatrixDataScoreStart;

	private String cMotifGroupsId;
	private String cMotifGroupsDisplayName;
	private String cMotifGroupsShortName;

	private String cHistogramsTaxonId;
	private String cHistogramsThreshHigh;
	private String cHistogramsThreshMed;
	private String cHistogramsThreshLow;
	private String cHistogramsMedian;
	private String cHistogramsMedianAbsDev;
	private String cHistogramsSitesScored;
	private String cHistogramsProteinsScored;
	private String cHistogramsPlotImage;

	private String cHistogramDataAbsFreq;
	private String cHistogramDataScore;

	private String cSiteEvidenceSite;

	private String cEvidenceResourcesResource;
	private String cEvidenceResourcesLink;

	private String cOrthologsId;
	private String cOrthologsGroupId;
	private String cOrthologsIdentifier;

	private String cIdentifierTypesId;
	private String cIdentifierTypesName;

	private String cLocalizationTypesId;
	private String cLocalizationTypesName;

	private String cEvidenceCodesId;
	private String cEvidenceCodesCode;
	private String cEvidenceCodesName;

	private String cGOTermsId;
	private String cGOTermsName;

	private String cLocalizationId;
	private String cLocalizationScore;

	private String cLocalizationGOTermsId;

	public static final String PARENT_TAXON_SEPARATOR = ".";

	public static final String SELECT = "SELECT ";
	public static final String FROM = " \nFROM ";
	public static final String WHERE = " \nWHERE ";
	public static final String ORDERBY = " \nORDER BY ";
	public static final String AND = " \n  AND ";
	public static final String AS = " AS ";
	public static final String COMMA = " , ";
	public static final String DOT = ".";
	public static final String EQ = " = ";
	public static final String INSERTINTO = "INSERT INTO ";
	public static final String VALUES = " \nVALUES ";
	public static final String DELETEFROM = "DELETE FROM ";
	public static final String LIKE = " LIKE ";
	public static final String UPDATE = "UPDATE ";
	public static final String REGEXP = " REGEXP ";
	public static final String SET = " \nSET ";
	public static final String DESC = " DESC ";
	public static final String IN = " IN ";
	public static final String DISTINCT = " DISTINCT ";
	public static final String GROUPBY = " \nGROUP BY ";
	public static final String JOIN = " JOIN ";
	public static final String INNERJOIN = " INNER JOIN ";
	public static final String LEFTJOIN = " LEFT JOIN ";
	public static final String USING = " USING ";
	public static final String NULL = " NULL ";
	public static final String OR = "\n  OR ";
	public static final String ON = " ON ";
	public static final String LIMIT = " LIMIT ";
	public static final String LPAR = " ( ";
	public static final String RPAR = " ) ";

	private static final String TABLENAME_SPACE_DELIMITER = "_";
	private static final String TEMP_TABLE_SUFFIX = TABLENAME_SPACE_DELIMITER
			+ "TEMP";
	private static final String OLD_TABLE_SUFFIX = TABLENAME_SPACE_DELIMITER
			+ "OLD";

	private static boolean useTemporaryTableNames = false;

	private CommandConstants(Properties config) {
		init(config);
	}

	public static CommandConstants instance(Properties config) {
		if (instance == null) {
			instance = new CommandConstants(config);
		}
		return instance;
	}

	public static CommandConstants instance() {
		return instance;
	}

	public static CommandConstants instance(boolean useTemporaryTables) {
		useTemporaryTableNames = useTemporaryTables;
		return instance;
	}

	private void init(Properties config) {
		ConfigReader cfgReader = new ConfigReader(config);
		// Table names
		tUsers = cfgReader.get("T_users");
		tNews = cfgReader.get("T_news");

		tDataSources = cfgReader.get("T_dataSources");
		tDataSourceTypes = cfgReader.get("T_dataSourceTypes");

		tTaxa = cfgReader.get("T_taxa");
		tProteins = cfgReader.get("T_proteins");
		tAnnotationTypes = cfgReader.get("T_annotationTypes");
		tAnnotations = cfgReader.get("T_annotations");
		tOrthologs = cfgReader.get("T_orthologs");
		tLocalization = cfgReader.get("T_localization");

		tMotifs = cfgReader.get("T_motifs");
		tMotifIdentifierMapping = cfgReader.get("T_motifIdentifierMapping");
		tMotifMatrixData = cfgReader.get("T_motifMatrixData");
		tMotifGroups = cfgReader.get("T_motifGroups");

		tHistograms = cfgReader.get("T_histograms");
		tHistogramData = cfgReader.get("T_histogramData");

		tSiteEvidence = cfgReader.get("T_siteEvidence");
		tEvidenceResources = cfgReader.get("T_evidenceResources");

		tIdentifierTypes = cfgReader.get("T_identifierTypes");
		tLocalizationTypes = cfgReader.get("T_localizationTypes");

		tEvidenceCodes = cfgReader.get("T_evidenceCodes");
		tGOTerms = cfgReader.get("T_goTerms");
		tLocalizationGOTerms = cfgReader.get("T_localizationGOTerms");

		// Columns in T_users
		cUsersEmail = cfgReader.get("T_users_C_email");
		cUsersFirstName = cfgReader.get("T_users_C_firstName");
		cUsersLastName = cfgReader.get("T_users_C_lastName");
		cUsersPassword = cfgReader.get("T_users_C_password");
		cUsersIsAdmin = cfgReader.get("T_users_C_isAdmin");
		cUsersIsSuperAdmin = cfgReader.get("T_users_C_isSuperAdmin");

		// Columns in T_news
		cNewsId = cfgReader.get("T_news_C_id");
		cNewsTitle = cfgReader.get("T_news_C_title");
		cNewsText = cfgReader.get("T_news_C_text");
		cNewsDate = cfgReader.get("T_news_C_date");

		// Columns in T_dataSources
		cDataSourcesId = cfgReader.get("T_dataSources_C_id");
		cDataSourcesShortName = cfgReader.get("T_dataSources_C_shortName");
		cDataSourcesDisplayName = cfgReader.get("T_dataSources_C_displayName");
		cDataSourcesDescription = cfgReader.get("T_dataSources_C_description");
		cDataSourcesVersion = cfgReader.get("T_dataSources_C_version");
		cDataSourcesLastUpdate = cfgReader.get("T_dataSources_C_lastUpdate");
		cDataSourcesIsPrimary = cfgReader.get("T_dataSources_C_isPrimary");

		// Columns in T_dataSourceTypes
		cDataSourceTypesId = cfgReader.get("T_dataSourceTypes_C_id");
		cDataSourceTypesShortName = cfgReader
				.get("T_dataSourceTypes_C_shortname");
		cDataSourceTypesDisplayName = cfgReader
				.get("T_dataSourceTypes_C_displayname");

		// Columns in T_taxa
		cTaxaId = cfgReader.get("T_taxa_C_id");
		cTaxaName = cfgReader.get("T_taxa_C_name");
		cTaxaParentTaxon = cfgReader.get("T_taxa_C_parentTaxon");
		cTaxaIsSpecies = cfgReader.get("T_taxa_C_isSpecies");

		// Columns in T_proteins
		cProteinsIdentifier = cfgReader.get("T_proteins_C_identifier");
		cProteinsMolWeight = cfgReader.get("T_proteins_C_molecularWeight");
		cProteinsClass = cfgReader.get("T_proteins_C_class");
		cProteinsPI = cfgReader.get("T_proteins_C_pI");
		cProteinsPiPhos1 = cfgReader.get("T_proteins_C_pIPhos1");
		cProteinsPiPhos2 = cfgReader.get("T_proteins_C_pIPhos2");
		cProteinsPiPhos3 = cfgReader.get("T_proteins_C_pIPhos3");
		cProteinsSequence = cfgReader.get("T_proteins_C_sequence");

		// Columns in T_annotationTypes
		cAnnotationTypesTitle = cfgReader.get("T_annotationTypes_C_title");
		cAnnotationTypesId = cfgReader.get("T_annotationTypes_C_id");

		// Columns in T_annotations
		cAnnotationsId = cfgReader.get("T_annotations_C_id");
		cAnnotationsAnnotation = cfgReader.get("T_annotations_C_annotation");

		// Columns in T_motifs
		cMotifsId = cfgReader.get("T_motifs_C_id");
		cMotifsDisplayName = cfgReader.get("T_motifs_C_displayName");
		cMotifsShortName = cfgReader.get("T_motifs_C_shortName");
		cMotifsOptScore = cfgReader.get("T_motifs_C_optScore");
		cMotifsIsPublic = cfgReader.get("T_motifs_C_isPublic");
		cMotifsMotifClass = cfgReader.get("T_motifs_C_motifClass");

		// Columns in T_motifIdentifierMapping
		cMotifIdentifierMappingId = cfgReader
				.get("T_motifIdentifierMapping_C_id");
		cMotifIdentifierMappingIdentifier = cfgReader
				.get("T_motifIdentifierMapping_C_identifier");

		// Columns in T_motifMatrixData
		cMatrixDataPosition = cfgReader.get("T_motifMatrixData_C_position");
		cMatrixDataScorePrefix = cfgReader
				.get("T_motifMatrixData_C_scorePrefix");
		cMatrixDataScoreEnd = cfgReader.get("T_motifMatrixData_C_scoreEnd");
		cMatrixDataScoreStart = cfgReader.get("T_motifMatrixData_C_scoreStart");

		// Columns in T_motifGroups
		cMotifGroupsId = cfgReader.get("T_motifGroups_C_id");
		cMotifGroupsDisplayName = cfgReader.get("T_motifGroups_C_displayName");
		cMotifGroupsShortName = cfgReader.get("T_motifGroups_C_shortName");

		// Columns in T_histograms
		cHistogramsTaxonId = cfgReader.get("T_histograms_C_taxonId");
		cHistogramsThreshHigh = cfgReader.get("T_histograms_C_threshHigh");
		cHistogramsThreshMed = cfgReader.get("T_histograms_C_threshMed");
		cHistogramsThreshLow = cfgReader.get("T_histograms_C_threshLow");
		cHistogramsMedian = cfgReader.get("T_histograms_C_median");
		cHistogramsMedianAbsDev = cfgReader.get("T_histograms_C_medianAbsDev");
		cHistogramsSitesScored = cfgReader.get("T_histograms_C_sitesScored");
		cHistogramsProteinsScored = cfgReader
				.get("T_histograms_C_proteinsScored");
		cHistogramsPlotImage = cfgReader.get("T_histograms_C_plotImage");

		// Columns in T_histogramData
		cHistogramDataScore = cfgReader.get("T_histogramData_C_score");
		cHistogramDataAbsFreq = cfgReader.get("T_histogramData_C_absFreq");

		// Columns in T_siteEvidence
		cSiteEvidenceSite = cfgReader.get("T_siteEvidence_C_site");

		// Columns in T_evidenceResources
		cEvidenceResourcesResource = cfgReader
				.get("T_evidenceResources_C_resoure");
		cEvidenceResourcesLink = cfgReader.get("T_evidenceResources_C_link");

		// Columns in T_orthologs
		cOrthologsId = cfgReader.get("T_orthologs_C_id");
		cOrthologsGroupId = cfgReader.get("T_orthologs_C_groupId");
		cOrthologsIdentifier = cfgReader.get("T_orthologs_C_identifier");

		// Columns in T_identifiertypes
		cIdentifierTypesId = cfgReader.get("T_identifierTypes_C_id");
		cIdentifierTypesName = cfgReader.get("T_identifierTypes_C_name");

		// Columns in T_localizationtypes
		cLocalizationTypesId = cfgReader.get("T_localizationTypes_C_id");
		cLocalizationTypesName = cfgReader.get("T_localizationTypes_C_name");

		// Columns in T_evidenceCodes
		cEvidenceCodesId = cfgReader.get("T_evidenceCodes_C_id");
		cEvidenceCodesCode = cfgReader.get("T_evidenceCodes_C_code");
		cEvidenceCodesName = cfgReader.get("T_evidenceCodes_C_name");

		// Columns in T_goTerms
		cGOTermsId = cfgReader.get("T_goTerms_C_id");
		cGOTermsName = cfgReader.get("T_goTerms_C_name");

		// Columns in T_localization
		cLocalizationId = cfgReader.get("T_localization_C_id");
		cLocalizationScore = cfgReader.get("T_localization_C_score");

		// Columns in T_localizationGOTerms
		cLocalizationGOTermsId = cfgReader.get("T_localizationGOTerms_C_id");
	}

	public String gettUsers() {
		return tUsers;
	}

	public String gettNews() {
		return tNews;
	}

	public String gettDataSources() {
		return tDataSources;
	}

	public String gettDataSourceTypes() {
		return tDataSourceTypes;
	}

	public String gettAnnotationTypes() {
		return tAnnotationTypes;
	}

	public String gettTaxa(DataSource dataSource) {
		if (useTemporaryTableNames) {
			return getTempTable(getTaxaTableName(dataSource));
		} else {
			return getTaxaTableName(dataSource);
		}
	}

	public String gettProteins(DataSource dataSource) throws IllegalArgumentException {
		if(!dataSource.getType().getShortName().equals("proteins")) {
			throw new IllegalArgumentException("Data source type invalid: Protein data source expected");
		}
		if (useTemporaryTableNames) {
			return getTempTable(getProteinsTableName(dataSource));
		} else {
			return getProteinsTableName(dataSource);
		}
	}

	public String gettAnnotations(DataSource dataSource) {
		if (useTemporaryTableNames) {
			return getTempTable(getAnnotationsTableName(dataSource));
		} else {
			return getAnnotationsTableName(dataSource);
		}
	}

	public String gettOrthology(DataSource dataSource) {
		if (useTemporaryTableNames) {
			return getTempTable(getOrthologsTableName(dataSource));
		} else {
			return getOrthologsTableName(dataSource);
		}
	}

	public String gettLocalization(DataSource dataSource) {
		if (useTemporaryTableNames) {
			return getTempTable(getLocalizationTableName(dataSource));
		} else {
			return getLocalizationTableName(dataSource);
		}
	}

	public String gettLocalizationGOTerms(DataSource dataSource) {
		if (useTemporaryTableNames) {
			return getTempTable(getLocalizationGOTermsTableName(dataSource));
		} else {
			return getLocalizationGOTermsTableName(dataSource);
		}
	}

	public String getProteinsTableName(DataSource dataSource) {
		return tProteins + TABLENAME_SPACE_DELIMITER
				+ dataSource.getShortName();
	}

	public String getTaxaTableName(DataSource dataSource) {
		return tTaxa + TABLENAME_SPACE_DELIMITER + dataSource.getShortName();
	}

	public String getAnnotationsTableName(DataSource dataSource) {
		return tAnnotations + TABLENAME_SPACE_DELIMITER
				+ dataSource.getShortName();
	}

	public String getOrthologsTableName(DataSource dataSource) {
		return tOrthologs + TABLENAME_SPACE_DELIMITER
				+ dataSource.getShortName();
	}

	public String getLocalizationTableName(DataSource dataSource) {
		return tLocalization + TABLENAME_SPACE_DELIMITER
				+ dataSource.getShortName();
	}

	public String getLocalizationGOTermsTableName(DataSource dataSource) {
		return tLocalizationGOTerms + TABLENAME_SPACE_DELIMITER
				+ dataSource.getShortName();
	}

	public String gettMotifs() {
		return tMotifs;
	}

	public String gettMotifIdentifierMapping() {
		return tMotifIdentifierMapping;
	}

	public String gettMotifMatrixData() {
		return tMotifMatrixData;
	}

	public String gettMotifGroups() {
		return tMotifGroups;
	}

	public String gettHistograms() {
		return tHistograms;
	}

	public String gettHistogramData() {
		return tHistogramData;
	}

	public String gettEvidenceResources() {
		return tEvidenceResources;
	}

	public String gettSiteEvidence() {
		return tSiteEvidence;
	}

	public String gettIdentifierTypes() {
		return tIdentifierTypes;
	}

	public String gettLocalizationTypes() {
		return tLocalizationTypes;
	}

	public String gettEvidenceCodes() {
		return tEvidenceCodes;
	}

	public String gettGOTerms() {
		return tGOTerms;
	}

	public String getcUsersEmail() {
		return cUsersEmail;
	}

	public String getcUsersFirstName() {
		return cUsersFirstName;
	}

	public String getcUsersLastName() {
		return cUsersLastName;
	}

	public String getcUsersIsAdmin() {
		return cUsersIsAdmin;
	}

	public String getcUsersIsSuperAdmin() {
		return cUsersIsSuperAdmin;
	}

	public String getcUsersPassword() {
		return cUsersPassword;
	}

	public String getcNewsId() {
		return cNewsId;
	}

	public String getcNewsTitle() {
		return cNewsTitle;
	}

	public String getcNewsText() {
		return cNewsText;
	}

	public String getcNewsDate() {
		return cNewsDate;
	}

	public String getcDataSourcesId() {
		return cDataSourcesId;
	}

	public String getcDataSourcesShortName() {
		return cDataSourcesShortName;
	}

	public String getcDataSourcesDisplayName() {
		return cDataSourcesDisplayName;
	}

	public String getcDataSourcesDescription() {
		return cDataSourcesDescription;
	}

	public String getcDataSourcesVersion() {
		return cDataSourcesVersion;
	}

	public String getcDataSourcesLastUpdate() {
		return cDataSourcesLastUpdate;
	}

	public String getcDataSourcesIsPrimary() {
		return cDataSourcesIsPrimary;
	}

	public String getcDataSourceTypesId() {
		return cDataSourceTypesId;
	}

	public String getcDataSourceTypesShortName() {
		return cDataSourceTypesShortName;
	}

	public String getcDataSourceTypesDisplayName() {
		return cDataSourceTypesDisplayName;
	}

	public String getcTaxaId() {
		return cTaxaId;
	}

	public String getcTaxaName() {
		return cTaxaName;
	}

	public String getcTaxaParentTaxa() {
		return cTaxaParentTaxon;
	}

	public String getcTaxaIsSpecies() {
		return cTaxaIsSpecies;
	}

	public String getcProteinsIdentifier() {
		return cProteinsIdentifier;
	}

	public String getcProteinsSequence() {
		return cProteinsSequence;
	}

	public String getcProteinsClass() {
		return cProteinsClass;
	}

	public String getcProteinsPI() {
		return cProteinsPI;
	}

	public String getcProteinsPIPhos1() {
		return cProteinsPiPhos1;
	}

	public String getcProteinsPIPhos2() {
		return cProteinsPiPhos2;
	}

	public String getcProteinsPIPhos3() {
		return cProteinsPiPhos3;
	}

	public String getcProteinsMolWeight() {
		return cProteinsMolWeight;
	}

	public String getcAnnotationTypesId() {
		return cAnnotationTypesId;
	}

	public String getcAnnotationTypesTitle() {
		return cAnnotationTypesTitle;
	}

	public String getcAnnotationsId() {
		return cAnnotationsId;
	}

	public String getcAnnotationsAnnotation() {
		return cAnnotationsAnnotation;
	}

	public String getcMotifsId() {
		return cMotifsId;
	}

	public String getcMotifsDisplayName() {
		return cMotifsDisplayName;
	}

	public String getcMotifsShortName() {
		return cMotifsShortName;
	}

	public String getcMotifsIsPublic() {
		return cMotifsIsPublic;
	}

	public String getcMotifsMotifClass() {
		return cMotifsMotifClass;
	}

	public String getcMotifsOptScore() {
		return cMotifsOptScore;
	}

	public String getcMotifIdentifierMappingId() {
		return cMotifIdentifierMappingId;
	}

	public String getcMotifIdentifierMappingIdentifier() {
		return cMotifIdentifierMappingIdentifier;
	}

	public String getcMatrixDataPosition() {
		return cMatrixDataPosition;
	}

	public String getcMatrixDataScorePrefix() {
		return cMatrixDataScorePrefix;
	}

	public String getcMatrixDataScoreEnd() {
		return cMatrixDataScoreEnd;
	}

	public String getcMatrixDataScoreStart() {
		return cMatrixDataScoreStart;
	}

	public String getcMotifGroupsId() {
		return cMotifGroupsId;
	}

	public String getcMotifGroupsDisplayName() {
		return cMotifGroupsDisplayName;
	}

	public String getcMotifGroupsShortName() {
		return cMotifGroupsShortName;
	}

	public String getcHistogramsTaxonId() {
		return cHistogramsTaxonId;
	}

	public String getcHistogramsThreshHigh() {
		return cHistogramsThreshHigh;
	}

	public String getcHistogramsThreshMed() {
		return cHistogramsThreshMed;
	}

	public String getcHistogramsThreshLow() {
		return cHistogramsThreshLow;
	}

	public String getcHistogramsMedian() {
		return cHistogramsMedian;
	}

	public String getcHistogramsMedianAbsDev() {
		return cHistogramsMedianAbsDev;
	}

	public String getcHistogramsSitesScored() {
		return cHistogramsSitesScored;
	}

	public String getcHistogramsProteinsScored() {
		return cHistogramsProteinsScored;
	}

	public String getcHistogramsPlotImage() {
		return cHistogramsPlotImage;
	}

	public String getcHistogramDataAbsFreq() {
		return cHistogramDataAbsFreq;
	}

	public String getcHistogramDataScore() {
		return cHistogramDataScore;
	}

	public String getcEvidenceResourcesLink() {
		return cEvidenceResourcesLink;
	}

	public String getcEvidenceResourcesResource() {
		return cEvidenceResourcesResource;
	}

	public String getcSiteEvidenceSite() {
		return cSiteEvidenceSite;
	}

	public String getcOrthologsId() {
		return cOrthologsId;
	}

	public String getcOrthologsGroupId() {
		return cOrthologsGroupId;
	}

	public String getcOrthologsIdentifier() {
		return cOrthologsIdentifier;
	}

	public String getcIdentifierTypesId() {
		return cIdentifierTypesId;
	}

	public String getcIdentifierTypesName() {
		return cIdentifierTypesName;
	}

	public String getcLocalizationTypesId() {
		return cLocalizationTypesId;
	}

	public String getcLocalizationTypesName() {
		return cLocalizationTypesName;
	}

	public String getcEvidenceCodesId() {
		return cEvidenceCodesId;
	}

	public String getcEvidenceCodesCode() {
		return cEvidenceCodesCode;
	}

	public String getcEvidenceCodesName() {
		return cEvidenceCodesName;
	}

	public String getcGOTermsId() {
		return cGOTermsId;
	}

	public String getcGOTermsName() {
		return cGOTermsName;
	}

	public String getcLocalizationId() {
		return cLocalizationId;
	}

	public String getcLocalizationScore() {
		return cLocalizationScore;
	}

	public String getcLocalizationGOTermsId() {
		return cLocalizationGOTermsId;
	}

	public static String enquote(String name) {
		Formatter f = new Formatter();
		return "\"" + f.replaceMagicQuotes(name) + "\"";
	}

	public static String count(String attributeName) {
		return " COUNT(" + attributeName + ") ";
	}

	public static String getTempTable(String tableName) {
		return tableName + TEMP_TABLE_SUFFIX;
	}

	public static String getOldTable(String tableName) {
		return tableName + OLD_TABLE_SUFFIX;
	}
}
