package edu.mit.scansite.server.updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.util.Calendar;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.file.Downloader;
import edu.mit.scansite.server.updater.transliterator.FileTransliterator;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.util.Formatter;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 * @author Thomas Bernwinkler
 */
public abstract class DbUpdater implements Runnable {
	private static final String TEMP_PREFIX = "temp_";
	private static final String TXT_SUFFIX = ".txt";

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected DataSourceMetaInfo dataSourceMetaInfo;
	protected DaoFactory fac;

	private List<URL> dbDownloadUrls = new LinkedList<>();
	protected URL versionFileDownloadUrl;
	private File tempDir;
	private List<String> dbFileNames = new LinkedList<>();
	private List<BufferedReader> readers = new LinkedList<>();

	protected String errFileName;
	protected String tempFileName;

	public DbUpdater() {
	}

	public void init(String tempDirPath, String invalidFilePrefix, DataSourceMetaInfo dataSourceMetaInfo)
			throws ScansiteUpdaterException {
		try {
			this.dataSourceMetaInfo = dataSourceMetaInfo;
			this.dbDownloadUrls.add(new URL(dataSourceMetaInfo.getUrl()));
			try {
				this.versionFileDownloadUrl = (dataSourceMetaInfo.getVersionUrl() == null
						|| dataSourceMetaInfo.getVersionUrl().isEmpty()) ? null
								: new URL(dataSourceMetaInfo.getVersionUrl());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				this.versionFileDownloadUrl = null;
			}
			String db = dataSourceMetaInfo.getDataSource().getShortName();
			this.errFileName = invalidFilePrefix + "_" + db + TXT_SUFFIX;
			this.tempFileName = TEMP_PREFIX + db + TXT_SUFFIX;

			Formatter formatter = new Formatter();
			this.tempDir = new File(formatter.formatFilePath(tempDirPath));
			if (!this.tempDir.exists()) {
				if (!tempDir.mkdir()) {
					if (!tempDir.mkdirs()) {
						logger.error("Given temporary directory does not exist and could not be created");
						throw new ScansiteUpdaterException(
								"Given temporary directory does not exist and could not be created");
					}
				}
			}
			fac = ServiceLocator.getDaoFactory();
		} catch (MalformedURLException e) {
			logger.error("Given URLs are invalid: " + e.getMessage(), e);
			throw new ScansiteUpdaterException("Given URLs are invalid: " + e.getMessage(), e);
		} catch (DataAccessException e) {
			logger.error("Can not create DaoFactory: " + e.getMessage(), e);
			throw new ScansiteUpdaterException("Can not create DaoFactory: " + e.getMessage(), e);
		}
	}

	/**
	 * - download files - transliterate files to temporary scansite-specific files
	 * (extracting information) - create temporary tables - transliterate
	 * scansite-specific files to temporary table in database - update taxa in
	 * histograms - rename old table, rename temp table - clean up (remove temporary
	 * files and remove old tables)
	 * 
	 * @throws ScansiteUpdaterException
	 *             Is thrown if any of the tasks encounters a problem.
	 */
	private void runUpdate() throws ScansiteUpdaterException {
		logger.info("setting data source version");
		dataSourceMetaInfo.getDataSource().setVersion(getVersion());
		logger.info("data source version set");

		logger.info("download files unless already loaded");
		String tempFilePath = getFilePath(tempFileName);
		File f = new File(tempFilePath);
		if (!f.exists()) {
			downloadDbFiles(dbDownloadUrls);
		}
		logger.info("download finished");

		logger.info("transliterating files to temporary scansite-specific files (extracting information)");
		if (!f.exists()) {
			for (String dbFileName : dbFileNames) {
				logger.info("Transliterating file: {}", dbFileName);
				initReader(dbFileName);
				FileTransliterator transliterator = getDbFileTransliterator(tempFilePath, getFilePath(errFileName));
				transliterator.transliterate();
			}
//			try {
//				logger.info(
//						"Reestablishing database connection to be on the safe side (long download and transliteration times may cause issues)");
//				DbConnector.getInstance().resetConnections();
//			} catch (SQLException e) {
//				logger.error("Failed to reestablish the database connection after long period timeout", e);
//			}
		}

		logger.info("Prepare file transliterator to transfer data to database");
		initReader(tempFileName);
		logger.info("Deleting downloaded files after processing to plain text file...");
        for (String dbFileName : dbFileNames) {
            try {
                Files.deleteIfExists(Paths.get(getFilePath(dbFileName)));
            } catch (IOException e) {
                logger.error("Could not delete downloaded files after transliterating");
            }
        }
		FileTransliterator ssTransliterator = getScansiteFileTransliterator(getFilePath("DB_" + errFileName));
		try {
			logger.info("transliterate scansite-specific files to temporary table in database");
			logger.info("disables auto-commit, unique and foreign key checks");
			fac.getDataSourceDao().disableChecks();
			logger.info("creating temp tables for inserts");
			createTables();
			logger.info("saving data source entry in dataSources table");
			saveDataSource(dataSourceMetaInfo.getDataSource());
			logger.info("transliterating to database: {}", tempFileName);
			ssTransliterator.transliterate();

			logger.info("update histograms so that they are consistent with the new tables");
			try {
				logger.info("updating histogram data {}", dataSourceMetaInfo.getDataSource().getDisplayName());
				updateHistogramData();
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				// if it fails, the database is probably set up the first time
			}

			logger.info("renaming tables {}", dataSourceMetaInfo.getDataSource().getDisplayName());
			renameTables();
			logger.info("enabling auto-commit, unique and foreign key checks");
			fac.getDataSourceDao().enableChecks();
			logger.info("successful");
		} catch (ScansiteUpdaterException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			dropTempTables();
			throw e;
		}

		// clean up, remove old tables and delete temporary files
		cleanup();
        System.out.println("Deleting transliterated text file after inserting data into database tables...");
        try {
            Files.deleteIfExists(Paths.get(getFilePath(tempFileName)));
            Files.deleteIfExists(Paths.get(getFilePath("DB_" + errFileName)));
            Files.deleteIfExists(Paths.get(getFilePath(errFileName)));
        } catch (IOException e) {
            logger.error("Could not delete text file after transliterating", e);
        }
	}

	private void saveDataSource(DataSource dataSource) {
		try {
			fac.getDataSourceDao().addOrUpdate(dataSource);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

	protected abstract FileTransliterator getScansiteFileTransliterator(String errorFilePath)
			throws ScansiteUpdaterException;

	/**
	 * This method is called if a version download URI is given in the
	 * configuration. What needs to be done here is to fetch the version from the
	 * given URI and return it. If null is returned, the current date is used as
	 * version name.
	 * 
	 * @return A string describing the current version, or NULL.
	 * @throws ScansiteUpdaterException
	 */
	protected abstract String doGetVersion() throws ScansiteUpdaterException;

	/**
	 * Instantiate a file transliterator that can be used for the downloaded public
	 * database file and return it.
	 * 
	 * @param tempFilePath
	 *            the path to the temporary file.
	 * @param errorFilePath
	 *            the path to the error-file (file that contains invalid / failed
	 *            entries)
	 * @return A file transliterator that reads / parses and transliterates the
	 *         downloaded files.
	 * @throws ScansiteUpdaterException
	 */
	protected abstract FileTransliterator getDbFileTransliterator(String tempFilePath, String errorFilePath)
			throws ScansiteUpdaterException;

	/**
	 * Prepare the URIs of the files that have to be downloaded. This can either be
	 * a list containing the file that is given in the configuration file (most
	 * simple case), or a list of files that is determined by accessing the given
	 * URI.
	 * 
	 * @param dbDownloadUrl
	 *            the URI given in the configuration file.
	 * @return A list of files to download (defined by their URIs).
	 * @throws ScansiteUpdaterException
	 */
	protected abstract List<URL> prepareDownloadUrls(List<URL> dbDownloadUrl) throws ScansiteUpdaterException;

	protected abstract void createTables() throws ScansiteUpdaterException;

	protected abstract void renameTables() throws ScansiteUpdaterException;

	protected abstract void dropOldTables() throws ScansiteUpdaterException;

	protected abstract void dropTempTables() throws ScansiteUpdaterException;

	protected abstract void updateHistogramData() throws ScansiteUpdaterException;

	private String getVersion() throws ScansiteUpdaterException {
		String version = null;
		if (versionFileDownloadUrl != null) {
			try {
				version = doGetVersion();
			} catch (Exception e) {
			}
		}
		if (version == null || version.isEmpty()) {
			Calendar cal = Calendar.getInstance();
			String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
			String year = String.valueOf(cal.get(Calendar.YEAR));
			String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
			version = year + "/" + month + "/" + day;
		}
		return version;
	}

	/**
	 * Initializes the reader that is used for accessing the given file.
	 * 
	 * @throws ScansiteUpdaterException
	 *             Is thrown if the downloaded file can not be accessed.
	 */
	private void initReader(String fileName) throws ScansiteUpdaterException {
		File f = new File(getFilePath(fileName));
		try {
			if (!readers.isEmpty()) {
				for (BufferedReader reader : readers) {
					reader.close();
				}
				readers.clear();
			}
			if (f.toString().endsWith(".gz")) {
				GZIPInputStream gzIn = new GZIPInputStream(new FileInputStream(f));
				if (dataSourceMetaInfo.getEncoding() != null && !dataSourceMetaInfo.getEncoding().isEmpty()) {
					readers.add(new BufferedReader(new InputStreamReader(gzIn, dataSourceMetaInfo.getEncoding())));
				} else {
					readers.add(new BufferedReader(new InputStreamReader(gzIn)));
				}
			} else if (f.toString().endsWith(".zip")) {
				ZipInputStream zis = new ZipInputStream(new FileInputStream(f));
				ZipEntry zippedFile = zis.getNextEntry();

				while (zippedFile != null) {
					File newFile = new File(tempDir + File.separator + dataSourceMetaInfo.getDataSource().getShortName()
							+ "_" + zippedFile.getName());
					FileOutputStream outputStream = new FileOutputStream(newFile);
					int len;
					byte[] buffer = new byte[1024];
					while ((len = zis.read(buffer)) > 0) {
						outputStream.write(buffer, 0, len);
					}

					outputStream.close();
					zippedFile = zis.getNextEntry();
					readers.add(new BufferedReader(new FileReader(newFile)));
				}

				zis.closeEntry();
				zis.close();
			} else {
				readers.add(new BufferedReader(new FileReader(f)));
			}
		} catch (Exception e) {
			logger.error("Local file " + f + " can not be accessed: " + e.getMessage(), e);
			throw new ScansiteUpdaterException("Local file " + f + " can not be accessed!", e);
		}
	}

	/**
	 * Performs the update.
	 **/
	@Override
	public void run() {
		try {
			runUpdate();
		} catch (ScansiteUpdaterException e) {
			logger.error("failed at " + dataSourceMetaInfo.getDataSource().getShortName() + "): " + e.getMessage(), e);
			e.printStackTrace();
		}
	}

	/**
	 * Deletes all the files that have been created before.
	 */
	protected void cleanup() {
		try {
			for (BufferedReader reader : readers) {
				reader.close();
			}
			readers.clear();
			dropOldTables();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		// Moved to DataInsertionManager
		// File dir;
		// if (f.isDirectory()){
		// dir = f;
		// } else {
		// dir = f.getParentFile();
		// f.delete();
		// }
		//
		// try {
		// FileUtils.deleteDirectory(dir);
		// } catch (IOException e) {
		// logger.warn(e.getMessage());
		// logger.warn("Could not delete the temporary directory [" +
		// dir.getAbsolutePath() + "]! Please do so manually!");
		// }
	}

	protected String getFilePath(String filename) {
		return tempDir + "/" + filename;
	}

	/**
	 * Downloads the files that are needed.
	 * 
	 * @throws ScansiteUpdaterException
	 *             Is thrown if one of the files can not be downloaded.
	 */
	private void downloadDbFiles(List<URL> dbDownloadUrls) throws ScansiteUpdaterException {
		this.dbDownloadUrls = prepareDownloadUrls(dbDownloadUrls);
		for (URL dbDownloadUrl : this.dbDownloadUrls) {
			String dbFileName = dataSourceMetaInfo.getDataSource().getShortName() + "_"
					+ getFileNameFromURL(dbDownloadUrl.toString());
			this.dbFileNames.add(dbFileName);
			String path = getFilePath(dbFileName);
			File f = new File(path);
			if (!f.exists()) {
				logger.info("download necessary, starting to download");
				downloadFile(dbDownloadUrl, path);
			} else {
				logger.info("download not necessary, file already downloaded");
			}
		}
	}

	private void downloadFile(URL from, String to) throws ScansiteUpdaterException {
		Downloader downloader = new Downloader();
		downloader.downloadFile(from, to);
	}

	/**
	 * Returns the filename of the given URL.
	 * 
	 * @param url
	 *            The url. If "/var/www/poop.txt" is given, "poop.txt" is returned
	 * @return The filename of the given URL.
	 */
	private String getFileNameFromURL(String url) {
		return url.substring(url.lastIndexOf('/') + 1, url.length());
	}

	protected List<BufferedReader> getReaders() {
		return readers;
	}

	protected File getTempDir() {
		return tempDir;
	}

	protected DataSourceMetaInfo getDatabase() {
		return dataSourceMetaInfo;
	}

	protected URL getVersionFileDownloadUrl() {
		return versionFileDownloadUrl;
	}
}
