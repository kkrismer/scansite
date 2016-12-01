package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.OrthologyDao;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.updater.DataSourceMetaInfo;
import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Konstantin Krismer
 */
public class OrthologyTransliteratorDbWriter implements
		OrthologyTransliteratorWriter {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private BufferedWriter errorWriter;
	private OrthologyDao orthologyDao;
	private DataSourceMetaInfo dataSourceMetaInfo;

	public OrthologyTransliteratorDbWriter(BufferedWriter errorWriter,
			DataSourceMetaInfo dataSourceMetaInfo, DbConnector dbConnector)
			throws ScansiteUpdaterException {
		this.errorWriter = errorWriter;
		this.dataSourceMetaInfo = dataSourceMetaInfo;
		try {
			DaoFactory factory = ServiceLocator.getInstance().getDaoFactory(
					dbConnector);
			orthologyDao = factory.getOrthologDao();
			orthologyDao.setUseTempTablesForUpdate(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ScansiteUpdaterException(e.getMessage(), e);
		}
	}

	@Override
	public void saveInvalidEntry(String line) throws ScansiteUpdaterException {
		try {
			errorWriter.write(line + "\n");
			errorWriter.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ScansiteUpdaterException(
					"Error writing invalid entry to file", e);
		}
	}

	@Override
	public void saveEntry(int orthologsGroupId, String orthologsIdentifier)
			throws ScansiteUpdaterException {
		try {
			orthologyDao.addOrthologyEntry(dataSourceMetaInfo.getDataSource(),
					orthologsGroupId, orthologsIdentifier);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			saveInvalidEntry(orthologsGroupId + "\t" + orthologsIdentifier
					+ "\t" + e.getMessage());
		}
	}

	@Override
	public void close() throws ScansiteUpdaterException {
		try {
			errorWriter.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ScansiteUpdaterException(e.getMessage(), e);
		}
	}
}
