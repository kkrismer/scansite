package edu.mit.scansite.shared;

import com.google.gwt.user.client.ui.Image;

/**
 * A class for instantiating images.
 * 
 * @author tobieh
 * @author Konstantin Krismer
 */
public class FilePaths {
	public static final String STATIC_IMAGE_DIRECTORY = "img/";
	public static final String TEMP_DIRECTORY = "scansite4_temp/";
	public static final String HIST_DIRECTORY = TEMP_DIRECTORY + "histograms/";
	public static final String PROTPLOT_DIRECTORY = TEMP_DIRECTORY + "proteinPlots/";
	public static final String MOTIFLOGO_DIRECTORY = TEMP_DIRECTORY + "motifLogos/";
	public static final String DOMAINSEQ_DIRECTORY = TEMP_DIRECTORY + "domainSequences/";
	public static final String RESULTS_DIRECTORY = TEMP_DIRECTORY + "resultFiles/";
	public static final String HTML_DIRECTORY = TEMP_DIRECTORY + "html/";

	public static final String SPACER = "_";

	// static images
	public static final String WAIT_SMALL = "wait_small.gif";
	public static final String WAIT_LARGE = "wait_large.gif";
	public static final String WAIT_HUGE = "wait_huge.gif";

	// histogram vars
	private static final String HIST_PREFIX = "hist" + SPACER;
	private static final String HIST_IMAGE_TYPE = ScansiteConstants.IMAGE_TYPE;
	private static final String HIST_POSTFIX = "." + HIST_IMAGE_TYPE;

	// protein plot vars
	private static final String PROTPLOT_PREFIX = "protPlot" + SPACER;
	private static final String PROTPLOT_IMAGE_TYPE = ScansiteConstants.IMAGE_TYPE;
	private static final String PROTPLOT_POSTFIX = "." + PROTPLOT_IMAGE_TYPE;

	// motiflogo plot vars
	private static final String MOTIFLOGO_PREFIX = "motifLogo" + SPACER;
	private static final String MOTIFLOGO_IMAGE_TYPE = ScansiteConstants.IMAGE_TYPE;
	private static final String MOTIFLOGO_POSTFIX = "." + MOTIFLOGO_IMAGE_TYPE;

	/**
	 * @param imageName
	 *            The name of the static image (eg one of this class's
	 *            constants).
	 * @return The path to the static image.
	 */
	public static final String getStaticImagePath(String imageName) {
		return STATIC_IMAGE_DIRECTORY + imageName;
	}

	/**
	 * @param imageName
	 *            The name of the static image (eg one of this class's
	 *            constants).
	 * @return An image object of the chosen image.
	 */
	public static final Image getStaticImage(String imageName) {
		return new Image(getStaticImagePath(imageName));
	}

	/**
	 * @return A unique filename for a given histogram, ending in regex:
	 *         "_\d+\.png$", where 'png' is the value of the IMAGE_TYPE,
	 *         declared in ScansiteAlgorithmConstants and the sequence of digits
	 *         '\d+' is the current system time in ms. This number is in the
	 *         file name to reduce the probability of overwriting another file.
	 */
	public static final String getHistogramFilePath(String realPath) {
		return getHistogramFilePath(realPath, null, null);
	}

	/**
	 * @param middleName
	 *            A string that will be part of the fileName.
	 * @param systemTimeMs
	 *            The current systemTime in milliseconds, or another unique
	 *            number. Or, null in order to use the actual current system
	 *            time.
	 * @return A unique filename for a given histogram, ending in regex:
	 *         "_\d+\.png$", where 'png' is the value of the IMAGE_TYPE,
	 *         declared in ScansiteAlgorithmConstants and the sequence of digits
	 *         '\d+' is the given number or the actual current system time in
	 *         milliseconds. This number is in the filename to reduce the
	 *         probability of overwriting another file.
	 */
	public static final String getHistogramFilePath(String realPath,
			String middleName, Long systemTimeMs) {
		return getFilePath(realPath, HIST_DIRECTORY, HIST_PREFIX, middleName,
				HIST_POSTFIX, systemTimeMs);
	}

	/**
	 * @param filePath
	 *            A filepath, as returned by get...FilePath.
	 * @return The 'unique' number at the end of the filepath or NULL, in case
	 *         the number cannot be parsed.
	 */
	public static final Long getFilePathNumber(String filePath) {
		Long nr = null;
		try {
			String parts[] = filePath.split(SPACER);
			if (parts != null) {
				String nrStr = parts[parts.length - 1].split("\\.")[0];
				nr = Long.valueOf(nrStr);
			}
		} catch (Exception e) {
		}
		return nr;
	}

	/**
	 * @return A unique filename for a given protein plot, ending in regex:
	 *         "_\d+\.png$", where 'png' is the value of the IMAGE_TYPE,
	 *         declared in ScansiteAlgorithmConstants and the sequence of digits
	 *         '\d+' is the current system time in ms. This number is in the
	 *         file name to reduce the probability of overwriting another file.
	 */
	public static String getProteinPlotFilePath(String realPath) {
		return getProteinPlotFilePath(realPath, null, null);
	}

	/**
	 * @param middleName
	 *            A string that will be part of the fileName.
	 * @param systemTimeMs
	 *            The current systemTime in milliseconds, or another unique
	 *            number. Or, null in order to use the actual current system
	 *            time.
	 * @return A unique filename for a given protein plot, ending in regex:
	 *         "_\d+\.png$", where 'png' is the value of the IMAGE_TYPE,
	 *         declared in ScansiteAlgorithmConstants and the sequence of digits
	 *         '\d+' is the given number or the actual current system time in
	 *         milliseconds. This number is in the filename to reduce the
	 *         probability of overwriting another file.
	 */
	public static String getProteinPlotFilePath(String realPath,
			String middleName, Long systemTimeMs) {
		return getFilePath(realPath, PROTPLOT_DIRECTORY, PROTPLOT_PREFIX,
				middleName, PROTPLOT_POSTFIX, systemTimeMs);
	}

	public static String getMotifLogoPath(String realPath, String shortName) {
		return getFilePath(realPath, MOTIFLOGO_DIRECTORY, MOTIFLOGO_PREFIX,
				shortName, MOTIFLOGO_POSTFIX, null);
	}

	private static String getFilePath(String realPath, String dir,
			String prefix, String middleName, String postfix, Long systemTimeMs) {
		if (realPath.length() == 0) {
			return dir
					+ prefix
					+ ((middleName == null) ? "" : middleName + SPACER)
					+ String.valueOf(systemTimeMs == null ? System
							.currentTimeMillis() : systemTimeMs) + postfix;
		} else {
			return (realPath.endsWith("/") ? realPath : (realPath + "/"))
					+ dir
					+ prefix
					+ ((middleName == null) ? "" : middleName + SPACER)
					+ String.valueOf(systemTimeMs == null ? System
							.currentTimeMillis() : systemTimeMs) + postfix;
		}
	}

	public static String getDomainSeqFilePath(String realPath, String seqFileName) {
		return (realPath.endsWith("/") ? realPath : (realPath + "/"))
				+ DOMAINSEQ_DIRECTORY + seqFileName;
	}

	public static String getResultFilePath(String realPath, String fileName) {
		return (realPath.endsWith("/") ? realPath : (realPath + "/"))
				+ RESULTS_DIRECTORY + fileName;
	}

//	public static String getHtmlFilePath() {
//		return HTML_DIRECTORY + String.valueOf(System.currentTimeMillis())
//				+ ".html";
//	}
}
