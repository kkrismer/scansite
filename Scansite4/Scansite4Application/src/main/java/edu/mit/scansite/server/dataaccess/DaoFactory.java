package edu.mit.scansite.server.dataaccess;

import java.util.Properties;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DaoFactory {
	private Properties dbAccessConfig;
	private Properties dbConstantsConfig;

	public DaoFactory(Properties dbAccessConfig, Properties dbConstantsConfig) {
		this.dbAccessConfig = dbAccessConfig;
		this.dbConstantsConfig = dbConstantsConfig;
	}

	public UserDao getUserDao() {
		return new UserDaoImpl(dbAccessConfig, dbConstantsConfig);
	}

	public NewsDao getNewsDao() {
		return new NewsDaoImpl(dbAccessConfig, dbConstantsConfig);
	}

	public MotifGroupsDao getGroupsDao() {
		return new MotifGroupsDaoImpl(dbAccessConfig, dbConstantsConfig);
	}

	public ProteinDao getProteinDao() {
		return new ProteinDaoImpl(dbAccessConfig, dbConstantsConfig);
	}

	public OrthologyDao getOrthologDao() {
		return new OrthologyDaoImpl(dbAccessConfig, dbConstantsConfig);
	}

	public TaxonDao getTaxonDao() {
		return new TaxonDaoImpl(dbAccessConfig, dbConstantsConfig);
	}

	public AnnotationDao getAnnotationDao() {
		return new AnnotationDaoImpl(dbAccessConfig, dbConstantsConfig);
	}

	public DataSourceDao getDataSourceDao() {
		return new DataSourceDaoImpl(dbAccessConfig, dbConstantsConfig);
	}

	public MotifDao getMotifDao() {
		return new MotifDaoImpl(dbAccessConfig, dbConstantsConfig);
	}

	public HistogramDao getHistogramDao() {
		return new HistogramDaoImpl(dbAccessConfig, dbConstantsConfig);
	}

	public SiteEvidenceDao getSiteEvidenceDao() {
		return new SiteEvidenceDaoImpl(dbAccessConfig, dbConstantsConfig);
	}

	public IdentifierDao getIdentifierDao() {
		return new IdentifierDaoImpl(dbAccessConfig, dbConstantsConfig);
	}

	public LocalizationDao getLocalizationDao() {
		return new LocalizationDaoImpl(dbAccessConfig, dbConstantsConfig);
	}

	public GOTermDao getGOTermDao() {
		return new GOTermDaoImpl(dbAccessConfig, dbConstantsConfig);
	}
}
