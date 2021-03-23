package edu.mit.scansite.shared;

/**
 * @author tobieh
 * @author Konstantin Krismer
 */
public class ScansiteConstants {
  /**
   * general scansiteConstant. window size of motifs.
   */
  public static final int WINDOW_SIZE = 15;

  /**
   * general scansiteConstant. center index of motif windows.
   */
  public static final int WINDOW_CENTER_INDEX = 7;

  /**
   * general scansiteConstant. one half of motif window..
   */
  public static final int HALF_WINDOW = (WINDOW_SIZE - 1) / 2;

  /**
   * general scansiteConstant. fixed site score in a motif.
   */
  public static final int FIXED_SITE_SCORE = 21;

  /**
   * general scansiteConstant. default value in scansite motif matrices.
   */
  public static final double DEFAULT_MATRIX_VALUE = 1.0;

  /**
   * general scansiteConstant. default value in scansite motifs for terminals
   * (N/C).
   */
  public static final double DEFAULT_MATRIX_VALUE_TERMINALS = 0.0;

  /**
   * general scansiteConstant. pad character for sequences. applied if motif
   * matches at sequence terminals (N/C).
   */
  public static final String SEQ_PAD_CHAR = "_";

  /**
   * general scansiteConstant. pad character for sequences. applied if motif
   * matches at N terminal.
   */
  public static final String SEQ_PAD_CHAR_START = "$";

  /**
   * general scansiteConstant. pad character for sequences. applied if motif
   * matches at C terminal.
   */
  public static final String SEQ_PAD_CHAR_END = "*";

  /**
   * general scansiteConstant. general max score when scoring.
   */
  public static final double MAX_SCORING_SCORE = 5;


  public static final double STRINGENCY_HIGH = 0.002; // 0.2%
  public static final double STRINGENCY_MEDIUM = 0.01; // 1 %
  public static final double STRINGENCY_LOW = 0.05; // 5%
  public static final double STRINGENCY_MIN = 0.15; // 15%
  
  /**
   * general scansiteConstant. max score for database searches with uploaded
   * motifs.
   */
  public static final Double MAX_SCORING_SCORE_DBSEARCH_USERMOTIF = STRINGENCY_HIGH; // used to be 2D

  /**
   * histogram constant. maximum score displayed.
   */
  public static final double HIST_SCORE_MAX = 2.0;

  /**
   * histogram constant. window size.
   */
  public static double HISTOGRAM_SCORE_WINDOW_SIZE = 1 / 60D;

  /**
   * size of histogram image -- width.
   */
  public static final int HIST_IMAGE_HEIGHT = 600;

  /**
   * size of histogram image -- height.
   */
  public static final int HIST_IMAGE_WIDTH = 900;


  /**
   * default histogram selections. both fields have to contain the same number
   * of entries and their indices must correspond. in order to improve the user
   * experience, list those settings that will take more time for creating a
   * histogram at the end of the array (reason: order of displaying the
   * histograms when creating a motif).
   */
  public static final String[] HIST_DEFAULT_DATASOURCE_SHORTS = { "yeast",
      "swissprot" };
  public static final String[] HIST_DEFAULT_TAXON_NAMES = {
      "Saccharomyces cerevisiae (Baker's yeast)", "Vertebrata" };
  public static final int HIST_DEFAULT_INDEX = 1;

  public static final String IMAGE_TYPE = "png";

  /**
   * maximum number of sequence match motifs user is allowed to enter
   */
  public static final int SEQUENCE_MATCH_MAX_MOTIFS = 5;

  /**
   * constant for calculating a sequence's surface accessibility
   */
  public static final double SURFACE_ACCESSIBILITY_FACTOR = 17.6;

  /**
   * constants for pI calculation.
   */
  public static final double PH_MIN = 0;
  public static final double PH_MAX = 14;
  public static final double PI_THRESHOLD = 0.0001;
  public static final int MAX_PI_ITERATIONS = 1500;

  public static final double PHOSPHORYLATION_WEIGHT = 77.971168; // weight of
                                                                 // P+3*O-H
  public static final double PK_PHOS1 = 2.12;
  public static final double PK_PHOS2 = 7.21;
  public static final String PK_VALUE_RESIDUES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static final double PK_VALUES[][] = {
  /* Ct Nt Sm // AA */
  { 3.55, 7.59, 0.00 }, // A
      { 3.55, 7.50, 0.00 }, // B
      { 3.55, 7.50, 9.00 }, // C
      { 4.55, 7.50, 4.05 }, // D
      { 4.75, 7.70, 4.45 }, // E
      { 3.55, 7.50, 0.00 }, // F
      { 3.55, 7.50, 0.00 }, // G
      { 3.55, 7.50, 5.98 }, // H
      { 3.55, 7.50, 0.00 }, // I
      { 3.55, 7.50, 0.00 }, // J
      { 3.55, 7.50, 10.0 }, // K
      { 3.55, 7.50, 0.00 }, // L
      { 3.55, 7.00, 0.00 }, // M
      { 3.55, 7.50, 0.00 }, // N
      { 3.55, 7.50, 0.00 }, // O Pyrrolysine
      { 3.55, 8.36, 0.00 }, // P
      { 3.55, 7.50, 0.00 }, // Q
      { 3.55, 7.50, 12.0 }, // R
      { 3.55, 6.93, 0.00 }, // S
      { 3.55, 6.82, 0.00 }, // T
      { 3.55, 7.50, 5.24 }, // U Selenocysteine
      { 3.55, 7.44, 0.00 }, // V
      { 3.55, 7.50, 0.00 }, // W
      { 3.55, 7.50, 0.00 }, // X Any
      { 3.55, 7.50, 10.0 }, // Y
      { 3.55, 7.50, 0.00 } // Z
  };

  /**
   * constants for motif-file upload. error tag.
   */
  public static final String MOTIF_UPLOAD_TAG_ERROR = "errorMessage";

  /**
   * constants for motif-file upload. motif tag.
   */
  public static final String MOTIF_UPLOAD_TAG_MOTIF = "motif";

  /**
   * constants for motif-file upload. filename tag.
   */
  public static final String MOTIF_UPLOAD_TAG_FILENAME = "fileName";

  /**
   * maximum number of motifs allowed to use for db search
   */
  public static final int MAX_MOTIFS_DB_SEARCH = 5;

  /**
   * maximum number of distance restrictions allowed to use for db search
   */
  public static final int MAX_DIST_RESTRICTIONS_DB_SEARCH = 3;

  /**
   * maximum number of results displayed in sequence match.
   */
  public static final int SEQUENCE_MATCH_MAX_RESULTS_BROWSER = 500;

  /**
   * minimum length of protein accession string before server is queried for
   * proteins containing this query string.
   */
  public static final int MIN_LENGTH_PROTEIN_ORACLE_QUERY = 3;

  /**
   * maximum number of suggestions shown to users when entering a protein
   * accession.
   */
  public static final int MAX_SUGGESTIONS_PROTEIN_ACCESSIONS = 50;

  public static final String ANNOTATION_ACCESSION = "accession";
  public static final String ANNOTATION_DESCRIPTION = "description";
  public static final String ANNOTATION_LOCATION = "location";
  public static final String ANNOTATION_TYPE = "type";
  public static final String ANNOTATION_KEYWORD = "keyword";
  public static final String ANNOTATION_TRANSCRIPT = "transcript";
  public static final String ANNOTATION_GENE = "gene";
  public static final String ANNOTATION_OTHER = "other";

  /**
   * identifier mapping via biodbnet web service enables scan for orthologous
   * proteins when protein database uses a different identifier type than the
   * orthology database
   */
  public static final boolean IDENTIFIER_MAPPING = false;

}
