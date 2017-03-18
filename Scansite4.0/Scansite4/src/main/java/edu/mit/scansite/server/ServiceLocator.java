package edu.mit.scansite.server;

import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.shared.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Thomas Bernwinkler
 * @author Konstantin Krismer
 * @author Tobieh
 */
final public class ServiceLocator {
	private static final String CFG_DIR = "cfg/";
	private static final String DB_ACCESS_FILENAME             = "DatabaseAccess.properties";
	private static final String DB_CONSTANTS_FILENAME          = "DatabaseConstants.properties";
	private static final String DOMAIN_LOCATOR_ACCESS_FILENAME = "DomainLocator.properties";
	private static final String UPDATER_CONSTANTS_FILENAME     = "UpdaterConstants.xml";
	private static final String UPDATER_CONSTANTS_DTD_FILENAME = "UpdaterConstants.dtd";

	private static ServiceLocator instance;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Properties dbAccessProperties = null;
	private Properties dbConstantsProperties = null;
	private Properties domainLocatorConfigProperties = null;

	private ServiceLocator(){}

	private static void ensureInstance() {
	    if (instance == null) {
	        instance = new ServiceLocator();
        }
    }

	public static Properties getDbAccessProperties() throws DataAccessException {
	    ensureInstance();
		if (instance.dbAccessProperties == null) {
			instance.dbAccessProperties = instance.initPropertiesFile(DB_ACCESS_FILENAME);
		}
		return instance.dbAccessProperties;
	}

	public static Properties getDbConstantsProperties() throws DataAccessException {
	    ensureInstance();
		if (instance.dbConstantsProperties == null) {
			instance.dbConstantsProperties = instance.initPropertiesFile(DB_CONSTANTS_FILENAME);
		}
		return instance.dbConstantsProperties;
	}

	public static Properties getDomainLocatorConfigProperties() throws DataAccessException {
	    ensureInstance();
		if (instance.domainLocatorConfigProperties == null) {
			instance.domainLocatorConfigProperties = instance.initPropertiesFile(DOMAIN_LOCATOR_ACCESS_FILENAME);
		}
		return instance.domainLocatorConfigProperties;
	}

	public static InputStream getUpdaterConstantsFileProperties() throws DataAccessException {
		return instance.initConfigFileStream(UPDATER_CONSTANTS_FILENAME);
	}

	public static InputStream getUpdaterConstantsDTDFileProperties()
			throws DataAccessException {
		return instance.initConfigFileStream(UPDATER_CONSTANTS_DTD_FILENAME);
	}

    private InputStream initConfigFileStream(String file)
            throws DataAccessException {
        try {
            return Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(CFG_DIR + file);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }


	/**
	 * @return A DaoFactory.
	 * @throws DataAccessException Exception for missing configuration file
	 */
	public static DaoFactory getDaoFactory() throws DataAccessException {
		try {
			Properties accConfig = getDbAccessProperties();
			Properties constConfig = getDbConstantsProperties();
			return new DaoFactory(accConfig, constConfig);
		} catch (Exception e) {
            String path = new File(DB_ACCESS_FILENAME).getAbsolutePath();
            throw new DataAccessException("Configuration-files can not be accessed at '"
                    + path.substring(0, path.lastIndexOf('/')) + "/'!\n"	+ e.getMessage());
		}
	}


	private Properties initPropertiesFile(String file) throws DataAccessException {
        return initPropertiesFile(CFG_DIR, file);
    }

	private Properties initPropertiesFile(String directory, String file) throws DataAccessException {
		Properties p = new Properties();
		InputStream fin;
		try {
			fin = Thread.currentThread().getContextClassLoader().getResourceAsStream(directory + file);
			if (fin == null) { // Web Service uses sources outside the classpath
			    fin = new FileInputStream(directory + file);
			}
			p.load(fin);
			fin.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return p;
	}

//	private InputStream initConfigFileStream(String file)
//			throws DataAccessException {
//		InputStream fin;
//		try {
//			fin = Thread.currentThread().getContextClassLoader()
//					.getResourceAsStream(CFG_DIR + file);
//			if (fin == null) { // try with backslashes
//				fin = Thread.currentThread().getContextClassLoader()
//						.getResourceAsStream(BACKSLASH_DIR + file);
//				logger.info("config resource file retrieval: used backslash-containing path");
//			} else {
//				logger.info("config resource file retrieval: used slash-containing path");
//			}
//			return fin;
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			throw new DataAccessException(e.getMessage(), e);
//		}
//	}
}
