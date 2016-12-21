package edu.mit.scansite.server.updater;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DataSourceDao;
import edu.mit.scansite.server.dataaccess.GOTermDao;
import edu.mit.scansite.server.dataaccess.IdentifierDao;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.dataaccess.file.UpdaterConfigXmlFileReader;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.DataSourceType;
import edu.mit.scansite.shared.transferobjects.EvidenceCode;
import edu.mit.scansite.shared.transferobjects.IdentifierType;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class Updater {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final DbConnector dbConnector;

	public Updater(final DbConnector dbConnector) {
		this.dbConnector = dbConnector;
	}

	public void update() throws ScansiteUpdaterException {
		DbUpdaterConfig updaterConfig;
		try {
			InputStream configFileStream = ServiceLocator.getInstance()
					.getUpdaterConstantsFilePath();
			InputStream configDTDFileStream = ServiceLocator.getInstance()
					.getUpdaterConstantsDTDFilePath();
			UpdaterConfigXmlFileReader reader = new UpdaterConfigXmlFileReader();
			updaterConfig = reader.readConfig(configFileStream,
					configDTDFileStream);
		} catch (Exception e) {
			throw new ScansiteUpdaterException(e);
		}

		fillDataSourceTypesTable(updaterConfig.getDataSourceTypes());
		fillIdentifierTypesTable(updaterConfig.getIdentifierTypes());
		fillEvidenceCodesTable(updaterConfig.getEvidenceCodes());

		//database issues with parallel attempt -> switch to serial
		for (DataSourceMetaInfo db : updaterConfig.getDataSourceMetaInfos()) {
			ExecutorService es = Executors.newCachedThreadPool();
			String updaterClass = db.getUpdaterClass();
			DbUpdater updater = getUpdater(updaterConfig.getTempDirPath(),
					updaterConfig.getInvalidFilePrefix(), db, dbConnector);
			es.execute(updater);
			logger.info("Running updater: " + updaterClass);
			es.shutdown();
			try {
				es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
				throw new ScansiteUpdaterException(e);
			}
		}
	}


    private void fillIdentifierTypesTable(List<IdentifierType> identifierTypes) {
		try {
			IdentifierDao dao = ServiceLocator.getInstance()
					.getDaoFactory(dbConnector).getIdentifierDao();
			for (IdentifierType identifierType : identifierTypes) {
				dao.addOrUpdateIdentifierType(identifierType);
			}
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
	}

	private void fillEvidenceCodesTable(List<EvidenceCode> evidenceCodes) {
		try {
			GOTermDao dao = ServiceLocator.getInstance()
					.getDaoFactory(dbConnector).getGOTermDao();
			for (EvidenceCode evidenceCode : evidenceCodes) {
				dao.addEvidenceCode(evidenceCode);
			}
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
	}

	private void fillDataSourceTypesTable(List<DataSourceType> dataSourceTypes) {
		try {
			DataSourceDao dao = ServiceLocator.getInstance()
					.getDaoFactory(dbConnector).getDataSourceDao();
			for (DataSourceType dataSourceType : dataSourceTypes) {
				dao.addOrUpdateDataSourceType(dataSourceType);
			}
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Instantiates an updater by reflection (using DbUpdaterFactory) and
	 * returns it
	 * 
	 * @param tempDirPath
     * @param invalidFilePrefix
	 * @param db
     * @param dbConnector
	 * @return
	 * @throws ScansiteUpdaterException
	 */
	private DbUpdater getUpdater(String tempDirPath, String invalidFilePrefix,
			DataSourceMetaInfo db, DbConnector dbConnector)
			throws ScansiteUpdaterException {
		DbUpdater updater;
		try {
			updater = DbUpdaterFactory.getDbUpdater(db.getUpdaterClass());
		} catch (DbUpdaterException e) {
			throw new ScansiteUpdaterException(e);
		}
		updater.init(tempDirPath, invalidFilePrefix, db, dbConnector);
		return updater;
	}
}
