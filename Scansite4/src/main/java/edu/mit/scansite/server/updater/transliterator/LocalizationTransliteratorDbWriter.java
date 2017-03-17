package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedWriter;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.LocalizationDao;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.updater.DataSourceMetaInfo;
import edu.mit.scansite.server.updater.ScansiteUpdaterException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.EvidenceCode;
import edu.mit.scansite.shared.transferobjects.GOTerm;
import edu.mit.scansite.shared.transferobjects.GOTermEvidence;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Localization;
import edu.mit.scansite.shared.transferobjects.LocalizationType;

/**
 * @author Konstantin Krismer
 */
public class LocalizationTransliteratorDbWriter implements
		LocalizationTransliteratorWriter {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private BufferedWriter errorWriter;
	private LocalizationDao localizationDao;
	private DataSourceMetaInfo dataSourceMetaInfo;

	public LocalizationTransliteratorDbWriter(BufferedWriter errorWriter,
			DataSourceMetaInfo dataSourceMetaInfo)
			throws ScansiteUpdaterException {
		this.errorWriter = errorWriter;
		this.dataSourceMetaInfo = dataSourceMetaInfo;
		try {
			DaoFactory factory = ServiceLocator.getDaoFactory();
			localizationDao = factory.getLocalizationDao();
			localizationDao.setUseTempTablesForUpdate(true);
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
	public void saveEntry(String proteinIdentifier, int score,
			String localization, List<String> goTerms)
			throws ScansiteUpdaterException {
		try {
			Localization loc = new Localization();
			loc.setScore(score);
			loc.setType(new LocalizationType(localization));
			List<GOTermEvidence> goTermEvidences = new LinkedList<>();
			for (String goTerm : goTerms) {
				String[] cells = goTerm
						.split(LocalizationTransliteratorFileWriter.LEVEL2_SEPARATOR_REGEX);
				if (cells.length == 3) {
					EvidenceCode evidenceCode = new EvidenceCode();
					evidenceCode.setCode(cells[2]);
					goTermEvidences.add(new GOTermEvidence(new GOTerm(cells[0],
							cells[1]), evidenceCode));
				} else if (cells.length == 2) {
					goTermEvidences.add(new GOTermEvidence(new GOTerm(cells[0],
							cells[1]), null));
				} else {
					logger.error("wrong GO term format: " + goTerm);
				}
			}
			loc.setGoTerms(goTermEvidences);
			localizationDao
					.addLocalization(dataSourceMetaInfo.getDataSource(),
							new LightWeightProtein(proteinIdentifier,
									new DataSource()), loc);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			saveInvalidEntry(proteinIdentifier + "\t" + score + "\t"
					+ localization + "\t" + e.getMessage());
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
