package edu.mit.scansite.server;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ServiceLocator {
	private static final String SLASH_DIR = "/cfg/";
	private static final String BACKSLASH_DIR = "\\cfg\\";
	private static final String DB_ACCESS_FILE = "DatabaseAccess.properties";
	private static final String DB_CONSTANTS_FILE = "DatabaseConstants.properties";
	private static final String DOMAINLOCATOR_ACCESS_FILE = "DomainLocator.properties";
	private static final String UPDATER_CONSTANTS_FILE = "UpdaterConstants.xml";
	private static final String UPDATER_CONSTANTS_DTD_FILE = "UpdaterConstants.dtd";

	private static ServiceLocator instance;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Properties dbAccessProperties = null;
	private Properties dbConstantsProperties = null;
	private Properties domainLocatorConfigProperties = null;

	public static ServiceLocator getInstance() {
		if (instance == null) {
			instance = new ServiceLocator();
		}
		return instance;
	}

	protected ServiceLocator() {
	}

	public Properties getDbAccessFile() throws DataAccessException {
		if (dbAccessProperties == null) {
			dbAccessProperties = initPropertiesFile(DB_ACCESS_FILE);
		}
		return dbAccessProperties;
	}

	public Properties getDbConstantsFile() throws DataAccessException {
		if (dbConstantsProperties == null) {
			dbConstantsProperties = initPropertiesFile(DB_CONSTANTS_FILE);
		}
		return dbConstantsProperties;
	}

	public Properties getDomainLocatorConfigFile() throws DataAccessException {
		if (domainLocatorConfigProperties == null) {
			domainLocatorConfigProperties = initPropertiesFile(DOMAINLOCATOR_ACCESS_FILE);
		}
		return domainLocatorConfigProperties;
	}

	public InputStream getUpdaterConstantsFilePath() throws DataAccessException {
		return initConfigFileStream(UPDATER_CONSTANTS_FILE);
	}

	public InputStream getUpdaterConstantsDTDFilePath()
			throws DataAccessException {
		return initConfigFileStream(UPDATER_CONSTANTS_DTD_FILE);
	}

	/**
	 * @param dbConnector
	 *            A dbConnector, if an already existing connection should be
	 *            used, or NULL for a temporary connection (which is most of the
	 *            time the better choice).
	 * @return A DaoFactory.
	 * @throws DataAccessException
	 */
	public DaoFactory getDaoFactory(DbConnector dbConnector)
			throws DataAccessException {
		try {
			Properties accConfig = getDbAccessFile();
			Properties constConfig = getDbConstantsFile();
			return new DaoFactory(accConfig, constConfig, dbConnector);
		} catch (Exception e) {
			throw getFilesNotFoundException(e);
		}
	}

	/**
	 * @return A DaoFactory.
	 * @throws DataAccessException
	 */
	public DaoFactory getDaoFactory() throws DataAccessException {
		try {
			Properties accConfig = getDbAccessFile();
			Properties constConfig = getDbConstantsFile();
			return new DaoFactory(accConfig, constConfig, null);
		} catch (Exception e) {
			throw getFilesNotFoundException(e);
		}
	}

	private DataAccessException getFilesNotFoundException(Exception e)
			throws DataAccessException {
		File f = new File(DB_ACCESS_FILE);
		String path = f.getAbsolutePath();
		return new DataAccessException(
				"Configuration-files can not be accessed at '"
						+ path.substring(0, path.lastIndexOf('/')) + "/'!\n"
						+ e.getMessage());
	}

	private Properties initPropertiesFile(String file)
			throws DataAccessException {
		Properties p = new Properties();
		InputStream fin;
		try {
			fin = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(SLASH_DIR + file);
			if (fin == null) { // try with backslashes
				fin = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(BACKSLASH_DIR + file);
				logger.info("properties resource file retrieval: used backslash-containing path");
			} else {
				logger.info("properties resource file retrieval: used slash-containing path");
			}
			p.load(fin);
			fin.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return p;
	}

	private InputStream initConfigFileStream(String file)
			throws DataAccessException {
		InputStream fin;
		try {
			fin = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(SLASH_DIR + file);
			if (fin == null) { // try with backslashes
				fin = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(BACKSLASH_DIR + file);
				logger.info("config resource file retrieval: used backslash-containing path");
			} else {
				logger.info("config resource file retrieval: used slash-containing path");
			}
			return fin;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}
}
