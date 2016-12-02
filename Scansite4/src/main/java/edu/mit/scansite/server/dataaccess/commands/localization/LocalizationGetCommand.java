package edu.mit.scansite.server.dataaccess.commands.localization;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
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
public class LocalizationGetCommand extends DbQueryCommand<Localization> {
	private DataSource localizationDataSource;
	private LightWeightProtein protein;

	public LocalizationGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			DataSource localizationDataSource, LightWeightProtein protein) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.localizationDataSource = localizationDataSource;
		this.protein = protein;
	}

	@Override
	protected Localization doProcessResults(ResultSet result)
			throws DataAccessException {
		Localization localization = null;
		LocalizationType type = null;
		List<GOTermEvidence> goTerms = null;

		try {
			while (result.next()) {
				if (localization == null) {
					type = new LocalizationType(result.getInt(c
							.getcLocalizationTypesId()), result.getString(c
							.getcLocalizationTypesName()));
					goTerms = new LinkedList<>();
					localization = new Localization();
					localization.setId(result.getInt(c.getcLocalizationId()));
					localization.setType(type);
					localization.setScore(result.getInt(c
							.getcLocalizationScore()));
					localization.setGoTerms(goTerms);
				}
				GOTermEvidence goTermEvidenge = new GOTermEvidence();
				goTermEvidenge.setGoTerm(new GOTerm(result.getString(c
						.getcGOTermsId()),
						result.getString(c.getcGOTermsName())));
				goTermEvidenge.setEvidenceCode(new EvidenceCode(result.getInt(c
						.getcEvidenceCodesId()), result.getString(c
						.getcEvidenceCodesCode()), result.getString(c
						.getcEvidenceCodesName())));
				goTerms.add(goTermEvidenge);
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return localization;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append('*')
				.append(CommandConstants.FROM)
				.append(c.gettLocalization(localizationDataSource))
				.append(CommandConstants.LEFTJOIN)
				.append(c.gettLocalizationGOTerms(localizationDataSource))
				.append(CommandConstants.USING).append(CommandConstants.LPAR)
				.append(c.getcLocalizationId()).append(CommandConstants.RPAR)
				.append(CommandConstants.LEFTJOIN).append(c.gettGOTerms())
				.append(CommandConstants.USING).append(CommandConstants.LPAR)
				.append(c.getcGOTermsId()).append(CommandConstants.RPAR)
				.append(CommandConstants.LEFTJOIN)
				.append(c.gettEvidenceCodes()).append(CommandConstants.USING)
				.append(CommandConstants.LPAR).append(c.getcEvidenceCodesId())
				.append(CommandConstants.RPAR)
				.append(CommandConstants.INNERJOIN)
				.append(c.gettLocalizationTypes())
				.append(CommandConstants.USING).append(CommandConstants.LPAR)
				.append(c.getcLocalizationTypesId())
				.append(CommandConstants.RPAR).append(CommandConstants.WHERE);
		sql.append(c.getcProteinsIdentifier()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(protein.getIdentifier()));
		return sql.toString();
	}
}
