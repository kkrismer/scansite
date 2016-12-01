package edu.mit.scansite.server.dataaccess.commands.evidencecode;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.EvidenceCode;

/**
 * @author Konstantin Krismer
 */
public class EvidenceCodeAddCommand extends DbInsertCommand {
	private EvidenceCode evidenceCode;

	public EvidenceCodeAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			EvidenceCode evidenceCode) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.evidenceCode = evidenceCode;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettEvidenceCodes();
	}

	@Override
	protected String getIdColumnName() {
		return c.getcEvidenceCodesId();
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();

		sql.append(CommandConstants.INSERTINTO).append(c.gettEvidenceCodes())
				.append('(').append(c.getcEvidenceCodesCode())
				.append(CommandConstants.COMMA)
				.append(c.getcEvidenceCodesName()).append(')')
				.append(CommandConstants.VALUES).append('(')
				.append(CommandConstants.enquote(evidenceCode.getCode()))
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(evidenceCode.getName()))
				.append(')');
		return sql.toString();
	}
}
