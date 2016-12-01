package edu.mit.scansite.server.dataaccess;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DaoFactory {
	private Properties dbAccessConfig;
	private Properties dbConstantsConfig;
	private DbConnector dbConnector;

	public DaoFactory(Properties dbAccessConfig, Properties dbConstantsConfig,
			DbConnector dbConnector) {
		this.dbAccessConfig = dbAccessConfig;
		this.dbConstantsConfig = dbConstantsConfig;
		this.dbConnector = dbConnector;
	}

	public UserDao getUserDao() {
		return new UserDaoImpl(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	public NewsDao getNewsDao() {
		return new NewsDaoImpl(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	public MotifGroupsDao getGroupsDao() {
		return new MotifGroupsDaoImpl(dbAccessConfig, dbConstantsConfig,
				dbConnector);
	}

	public ProteinDao getProteinDao() {
		return new ProteinDaoImpl(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	public OrthologyDao getOrthologDao() {
		return new OrthologyDaoImpl(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	public TaxonDao getTaxonDao() {
		return new TaxonDaoImpl(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	public AnnotationDao getAnnotationDao() {
		return new AnnotationDaoImpl(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	public DataSourceDao getDataSourceDao() {
		return new DataSourceDaoImpl(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	public MotifDao getMotifDao() {
		return new MotifDaoImpl(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	public HistogramDao getHistogramDao() {
		return new HistogramDaoImpl(dbAccessConfig, dbConstantsConfig, dbConnector);
	}

	public SiteEvidenceDao getSiteEvidenceDao() {
		return new SiteEvidenceDaoImpl(dbAccessConfig, dbConstantsConfig,
				dbConnector);
	}

	public IdentifierDao getIdentifierDao() {
		return new IdentifierDaoImpl(dbAccessConfig, dbConstantsConfig,
				dbConnector);
	}

	public LocalizationDao getLocalizationDao() {
		return new LocalizationDaoImpl(dbAccessConfig, dbConstantsConfig,
				dbConnector);
	}

	public GOTermDao getGOTermDao() {
		return new GOTermDaoImpl(dbAccessConfig, dbConstantsConfig, dbConnector);
	}
}
