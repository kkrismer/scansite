package edu.mit.scansite.server;

import java.io.File;
import java.io.FileInputStream;
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
	private static final String SLASH_DIR = "cfg/";
	private static final String BACKSLASH_DIR = "\\cfg\\";
	private static final String DB_ACCESS_FILE = "DatabaseAccess.properties";
	private static final String DB_CONSTANTS_FILE = "DatabaseConstants.properties";
	private static final String DOMAINLOCATOR_ACCESS_FILE = "DomainLocator.properties";
	private static final String WEBSERVICE_ACCESS_FILE = "ScansiteDataAccess.properties";
	private static final String UPDATER_CONSTANTS_FILE = "UpdaterConstants.xml";
	private static final String UPDATER_CONSTANTS_DTD_FILE = "UpdaterConstants.dtd";

	private static ServiceLocator instance;
	private static ServiceLocator webServiceInstance;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Properties dbAccessProperties = null;
	private Properties dbConstantsProperties = null;
	private Properties webServiceProperties = null;
	private Properties domainLocatorConfigProperties = null;

    public static ServiceLocator getInstance() {
        // for the functions called by the web service
        if (webServiceInstance != null)
            return webServiceInstance;
		if (instance == null) {
			instance = new ServiceLocator();
		}
		return instance;
	}

	public static ServiceLocator getWebServiceInstance() {
        if (webServiceInstance == null) {
            webServiceInstance = new ServiceLocator();
        }
        return webServiceInstance;
        // special use: getSvcDbAccessFile
        // special use: getSvcDbConstantsFile
    }

	protected ServiceLocator() {
	}

	public Properties getDbAccessFile() throws DataAccessException {
		if (dbAccessProperties == null) {
			dbAccessProperties = initPropertiesFile(SLASH_DIR, DB_ACCESS_FILE);
		}
		return dbAccessProperties;
	}

	public Properties getDbConstantsFile() throws DataAccessException {
		if (dbConstantsProperties == null) {
			dbConstantsProperties = initPropertiesFile(SLASH_DIR, DB_CONSTANTS_FILE);
		}
		return dbConstantsProperties;
	}

	public Properties getDomainLocatorConfigFile() throws DataAccessException {
		if (domainLocatorConfigProperties == null) {
			domainLocatorConfigProperties = initPropertiesFile(SLASH_DIR, DOMAINLOCATOR_ACCESS_FILE);
		}
		return domainLocatorConfigProperties;
	}

	// START SCANSITE WEB SERVICE SPECIFIC

	public Properties getScansiteConfigFilePath() throws DataAccessException {
	    if (webServiceProperties == null) {
	        webServiceProperties = initPropertiesFile(SLASH_DIR, WEBSERVICE_ACCESS_FILE);
        }
        return webServiceProperties;
    }

    public Properties getSvcDbAccessFile() throws DataAccessException {
        if (dbAccessProperties == null) {
            String directory = getScansiteConfigFilePath().getProperty("SCANSITE_CFG_PATH");
            directory += SLASH_DIR;
            dbAccessProperties = initPropertiesFile(directory, DB_ACCESS_FILE);
        }
        return dbAccessProperties;
    }

    public Properties getSvcDbConstantsFile() throws DataAccessException {
        if (dbConstantsProperties == null) {
            String directory = getScansiteConfigFilePath().getProperty("SCANSITE_CFG_PATH");
            directory += SLASH_DIR;
            dbConstantsProperties = initPropertiesFile(directory, DB_CONSTANTS_FILE);
        }
        return dbConstantsProperties;
    }

    // END SCANSITE WEB SERVICE SPECIFIC

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

	/**
     * @return A DaoFactory for the Scansite Web Service
     * @throws DataAccessException
     */
	public DaoFactory getSvcDaoFactory() throws DataAccessException {
        try {
            Properties accConfig = getSvcDbAccessFile();
            Properties constConfig = getSvcDbConstantsFile();
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

	private Properties initPropertiesFile(String directory, String file)
			throws DataAccessException {
		Properties p = new Properties();
		InputStream fin;
		try {
			fin = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(directory + file);
			if (fin == null) { // Web Service uses sources outside the classpath
			    fin = new FileInputStream(directory + file);
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
