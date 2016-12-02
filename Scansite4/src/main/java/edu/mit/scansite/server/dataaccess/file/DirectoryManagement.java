package edu.mit.scansite.server.dataaccess.file;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Konstantin Krismer
 */
public class DirectoryManagement {
	private static final Logger logger = LoggerFactory
			.getLogger(DirectoryManagement.class);

	// 1 week in ms (7 * 1000 * 60 * 60 * 24)
	private static final long FILE_DELETE_AGE_MS = 604800000;

	// 1 hour in ms (1000 * 60 * 60)
	private static final long FILE_DELETE_AGE_DECREASE_MS = 3600;

	// 0.5 hour in ms (1000 * 60 * 30)
	private static final long FILE_DELETE_AGE_MIN_MS = 1800;

	// 2 Gigabytes (2 * 1024 * 1024 * 1024)
	private static final long MIN_DISK_SPACE_BYTES = 2147483648L;

	public static final String TEMP_DIRECTORY = "scansite4_temp/";
	public static final String HIST_DIRECTORY = TEMP_DIRECTORY + "histograms/";
	public static final String PROTPLOT_DIRECTORY = TEMP_DIRECTORY
			+ "proteinPlots/";
	public static final String MOTIFLOGO_DIRECTORY = TEMP_DIRECTORY
			+ "motifLogos/";
	public static final String DOMAINSEQ_DIRECTORY = TEMP_DIRECTORY
			+ "domainSequences/";
	public static final String RESULTS_DIRECTORY = TEMP_DIRECTORY
			+ "resultFiles/";

	public static void prepareHistogramDirectory() {
		prepareHistogramDirectory("");
	}

	public static void prepareHistogramDirectory(String realPathPrefix) {
		if (realPathPrefix.endsWith("/") || realPathPrefix.endsWith("\\")
				|| realPathPrefix.length() == 0) {
			prepareDirectory(realPathPrefix + HIST_DIRECTORY, false);
		} else {
			prepareDirectory(realPathPrefix + "/" + HIST_DIRECTORY, false);
		}
	}

	// public static void prepareProteinPlotDirectory() {
	// prepareProteinPlotDirectory("");
	// }
	//
	// public static void prepareProteinPlotDirectory(String realPathPrefix) {
	// if(realPathPrefix.endsWith("/") || realPathPrefix.endsWith("\\") ||
	// realPathPrefix.length() == 0) {
	// prepareDirectory(realPathPrefix + PROTPLOT_DIRECTORY, false);
	// } else {
	// prepareDirectory(realPathPrefix + "/" + PROTPLOT_DIRECTORY, false);
	// }
	// }
	//
	// public static void prepareMotifLogoDirectory() {
	// prepareMotifLogoDirectory("");
	// }
	//
	// public static void prepareMotifLogoDirectory(String realPathPrefix) {
	// if(realPathPrefix.endsWith("/") || realPathPrefix.endsWith("\\") ||
	// realPathPrefix.length() == 0) {
	// prepareDirectory(realPathPrefix + MOTIFLOGO_DIRECTORY, false);
	// } else {
	// prepareDirectory(realPathPrefix + "/" + MOTIFLOGO_DIRECTORY, false);
	// }
	// }

	// public static void prepareDomainSeqDirectory() {
	// prepareDomainSeqDirectory("");
	// }

	public static void prepareDomainSeqDirectory(String realPathPrefix) {
		if (realPathPrefix.endsWith("/") || realPathPrefix.endsWith("\\")
				|| realPathPrefix.length() == 0) {
			prepareDirectory(realPathPrefix + DOMAINSEQ_DIRECTORY, false);
		} else {
			prepareDirectory(realPathPrefix + "/" + DOMAINSEQ_DIRECTORY, false);
		}
	}

	// public static void prepareResultsDirectory() {
	// prepareResultsDirectory("");
	// }

	public static void prepareResultsDirectory(String realPathPrefix) {
		if (realPathPrefix.endsWith("/") || realPathPrefix.endsWith("\\")
				|| realPathPrefix.length() == 0) {
			prepareDirectory(realPathPrefix + RESULTS_DIRECTORY, false);
		} else {
			prepareDirectory(realPathPrefix + "/" + RESULTS_DIRECTORY, false);
		}
	}

	/**
	 * equivalent to prepareDirectory(path, false).
	 */
	public static void prepareDirectory(final String path) {
		prepareDirectory(path, false);
	}

	/**
	 * creates a directory(-path) if it doesn't exist yet.
	 * 
	 * @param path
	 *            a path to a directory. if a filepath is given isFile has to be
	 *            TRUE.
	 * @param isFile
	 *            TRUE if the path points to a file.
	 */
	public static void prepareDirectory(final String path, boolean isFile) {
		String dirPath = path;
		if (isFile) {
			dirPath = dirPath.substring(0, dirPath.lastIndexOf("/"));
		}
		File dir = new File(dirPath);
		if (!dir.exists() || !dir.isDirectory()) {
			logger.info("Missing temporary directory. Attempting to create it...");
			boolean success = false;
			if (dir.mkdirs()) {
				success = true;
			} else {
				success = dir.mkdir();
			}
			if (success) {
				logger.info("Successfully created directory: " + dirPath);
			} else {
				logger.info("Failed to create directory: " + dirPath);
			}
		} else {
			deleteOldFiles(dir, FILE_DELETE_AGE_MS);
		}
	}

	private static void deleteOldFiles(File directory, long maxFileAge) {
		// with java instead of system command to stay platform independent on
		// the cost of a little performance
		if (directory.exists() && directory.isDirectory()
				&& maxFileAge > FILE_DELETE_AGE_MIN_MS) {
			try {
				long currentTime = System.currentTimeMillis();

				// delete old files
				for (File f : directory.listFiles()) {
					if (f.isFile()
							&& currentTime - f.lastModified() > maxFileAge) {
						f.delete();
					}
				}

				// check for free space and delete more if necessary
				if (directory.getUsableSpace() < MIN_DISK_SPACE_BYTES) {
					deleteOldFiles(directory, maxFileAge
							- FILE_DELETE_AGE_DECREASE_MS);
				}

			} catch (Exception e) {
				logger.warn("Failed to delete old temporary files: "
						+ e.getMessage());
			}
		}
	}
}
