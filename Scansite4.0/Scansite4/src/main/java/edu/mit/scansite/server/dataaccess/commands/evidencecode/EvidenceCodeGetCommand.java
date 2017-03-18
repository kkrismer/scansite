package edu.mit.scansite.server.dataaccess.commands.evidencecode;

import java.sql.ResultSet;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.EvidenceCode;

/**
 * @author Konstantin Krismer
 */
public class EvidenceCodeGetCommand extends DbQueryCommand<EvidenceCode> {
	private String code;

	public EvidenceCodeGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, String code) {
		super(dbAccessConfig, dbConstantsConfig);
		this.code = code;
	}

	@Override
	protected EvidenceCode doProcessResults(ResultSet result)
			throws DataAccessException {
		try {
			EvidenceCode evidenceCode = null;
			if (result.next()) {
				evidenceCode = new EvidenceCode(result.getInt(c
						.getcEvidenceCodesId()), result.getString(c
						.getcEvidenceCodesCode()), result.getString(c
						.getcEvidenceCodesName()));
			}
			return evidenceCode;
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcEvidenceCodesId())
				.append(CommandConstants.COMMA)
				.append(c.getcEvidenceCodesCode())
				.append(CommandConstants.COMMA)
				.append(c.getcEvidenceCodesName())
				.append(CommandConstants.FROM).append(c.gettEvidenceCodes())
				.append(CommandConstants.WHERE)
				.append(c.getcEvidenceCodesCode()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(code));
		return sql.toString();
	}
}
