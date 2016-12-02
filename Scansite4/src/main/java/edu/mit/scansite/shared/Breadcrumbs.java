package edu.mit.scansite.shared;

import edu.mit.scansite.client.ui.event.NavigationEvent;

/**
 * @author Konstantin Krismer
 */
public class Breadcrumbs {
	public static final String SEPARATOR = " &rsaquo; ";
	public static final String BASE = "<a href=\".\">Scansite 4.0</a>";
	public static final String FEATURE = "Features";
	public static final String HOME = BASE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.HOME.getId() + "\">Home</a>";
	public static final String NEWS = BASE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.NEWS.getId() + "\">News</a>";
	public static final String FEATURE_SCAN_PROTEIN = BASE + SEPARATOR
			+ FEATURE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.FEATURE_SCAN_PROTEIN.getId()
			+ "\">Scan Protein for Motifs</a>";
	public static final String FEATURE_SCAN_PROTEIN_RESULT = FEATURE_SCAN_PROTEIN
			+ SEPARATOR
			+ "<a href=\"#"
			+ NavigationEvent.PageId.FEATURE_SCAN_PROTEIN_RESULT.getId()
			+ "\">Result</a>";
	public static final String FEATURE_SCAN_DB = BASE + SEPARATOR + FEATURE
			+ SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.FEATURE_SCAN_DB.getId()
			+ "\">Search a Sequence Database for Motifs</a>";
	public static final String FEATURE_SCAN_DB_RESULT = FEATURE_SCAN_DB
			+ SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.FEATURE_SCAN_DB_RESULT.getId()
			+ "\">Result</a>";
	public static final String FEATURE_SCAN_SEQ = BASE + SEPARATOR + FEATURE
			+ SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.FEATURE_SCAN_SEQ.getId()
			+ "\">Find Sequence Match</a>";
	public static final String FEATURE_SCAN_SEQ_RESULT = FEATURE_SCAN_SEQ
			+ SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.FEATURE_SCAN_SEQ_RESULT.getId()
			+ "\">Result</a>";
	public static final String FEATURE_SCAN_ORTHOLOGS = BASE + SEPARATOR
			+ FEATURE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.FEATURE_SCAN_ORTHOLOGS.getId()
			+ "\">Scan Orthologs</a>";
	public static final String FEATURE_SCAN_ORTHOLOGS_RESULT = FEATURE_SCAN_ORTHOLOGS
			+ SEPARATOR
			+ "<a href=\"#"
			+ NavigationEvent.PageId.FEATURE_SCAN_ORTHOLOGS_RESULT.getId()
			+ "\">Result</a>";
	public static final String FEATURE_PREDICT_LOCALIZATION = BASE + SEPARATOR
			+ FEATURE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.FEATURE_PREDICT_LOCALIZATION.getId()
			+ "\">Predict Localization</a>";
	public static final String FEATURE_PREDICT_LOCALIZATION_RESULT = FEATURE_PREDICT_LOCALIZATION
			+ SEPARATOR
			+ "<a href=\"#"
			+ NavigationEvent.PageId.FEATURE_PREDICT_LOCALIZATION_RESULT
					.getId() + "\">Result</a>";
	public static final String FEATURE_CALC_MOLWEIGHT = BASE + SEPARATOR
			+ FEATURE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.FEATURE_CALC_MOLWEIGHT.getId()
			+ "\">Calculate Molecular Weight and pI</a>";
	public static final String FEATURE_CALC_COMPOSITION = BASE + SEPARATOR
			+ FEATURE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.FEATURE_CALC_COMPOSITION.getId()
			+ "\">Calculate Amino Acid Composition</a>";
	public static final String MOTIFS = BASE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.MOTIFS.getId()
			+ "\">Databases and Motifs</a>";
	public static final String TUTORIAL = BASE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.TUTORIAL.getId() + "\">Tutorial</a>";
	public static final String ABOUT = BASE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.ABOUT.getId() + "\">About</a>";
	public static final String FAQ = BASE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.FAQ.getId() + "\">FAQ</a>";
	public static final String CITE = BASE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.CITE.getId() + "\">Citing Scansite</a>";
	public static final String FEEDBACK = BASE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.FEEDBACK.getId() + "\">Send Feedback</a>";
	public static final String ADMIN = BASE + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.ADMIN.getId()
			+ "\">Administrator and Collaborator Area</a>";
	public static final String ADMIN_NEWS = ADMIN + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.ADMIN_NEWS.getId()
			+ "\">News Management</a>";
	public static final String ADMIN_MOTIF = ADMIN + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.ADMIN_MOTIF.getId()
			+ "\">Motif Management</a>";
	public static final String ADMIN_MOTIF_GROUP = ADMIN + SEPARATOR
			+ "<a href=\"#" + NavigationEvent.PageId.ADMIN_MOTIF_GROUP.getId()
			+ "\">Motif Group Management</a>";
	public static final String ADMIN_USER = ADMIN + SEPARATOR + "<a href=\"#"
			+ NavigationEvent.PageId.ADMIN_USER.getId()
			+ "\">USER Management</a>";
	public static final String FILE_NOT_FOUND = BASE + SEPARATOR + "404";
}
