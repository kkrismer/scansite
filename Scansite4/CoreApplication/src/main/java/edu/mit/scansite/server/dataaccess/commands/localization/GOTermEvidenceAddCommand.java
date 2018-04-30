package edu.mit.scansite.server.dataaccess.commands.localization;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.GOTermEvidence;
import edu.mit.scansite.shared.transferobjects.Localization;

/**
 * @author Konstantin Krismer
 */
public class GOTermEvidenceAddCommand extends DbInsertCommand {
	private DataSource localizationDataSource;
	private Localization localization;
	private GOTermEvidence goTermEvidence;

	public GOTermEvidenceAddCommand(Properties dbAccessConfig, Properties dbConstantsConfig,
			DataSource localizationDataSource, Localization localization, GOTermEvidence goTermEvidence) {
		super(dbAccessConfig, dbConstantsConfig);
		this.localizationDataSource = localizationDataSource;
		this.localization = localization;
		this.goTermEvidence = goTermEvidence;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettLocalizationGOTerms(localizationDataSource);
	}

	@Override
	protected String getIdColumnName() {
		return c.getcLocalizationGOTermsId();
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();

		if (goTermEvidence.getEvidenceCode() != null) {
			sql.append(CommandConstants.INSERTINTO).append(c.gettLocalizationGOTerms(localizationDataSource))
					.append('(').append(c.getcLocalizationId()).append(CommandConstants.COMMA).append(c.getcGOTermsId())
					.append(CommandConstants.COMMA).append(c.getcEvidenceCodesId()).append(')')
					.append(CommandConstants.VALUES).append('(').append(localization.getId())
					.append(CommandConstants.COMMA).append(CommandConstants.enquote(goTermEvidence.getGoTerm().getId()))
					.append(CommandConstants.COMMA).append(goTermEvidence.getEvidenceCode().getId()).append(')');
		} else {
			sql.append(CommandConstants.INSERTINTO).append(c.gettLocalizationGOTerms(localizationDataSource))
					.append('(').append(c.getcLocalizationId()).append(CommandConstants.COMMA).append(c.getcGOTermsId())
					.append(')').append(CommandConstants.VALUES).append('(').append(localization.getId())
					.append(CommandConstants.COMMA).append(CommandConstants.enquote(goTermEvidence.getGoTerm().getId()))
					.append(')');
		}

		return sql.toString();
	}
}
