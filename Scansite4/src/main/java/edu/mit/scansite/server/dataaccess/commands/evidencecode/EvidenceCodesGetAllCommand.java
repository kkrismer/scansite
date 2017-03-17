package edu.mit.scansite.server.dataaccess.commands.evidencecode;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.EvidenceCode;

/**
 * @author Konstantin Krismer
 */
public class EvidenceCodesGetAllCommand extends
		DbQueryCommand<List<EvidenceCode>> {

	public EvidenceCodesGetAllCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	@Override
	protected List<EvidenceCode> doProcessResults(ResultSet result)
			throws DataAccessException {
		List<EvidenceCode> evidenceCodes = new LinkedList<EvidenceCode>();
		try {
			while (result.next()) {
				EvidenceCode evidenceCode = new EvidenceCode();
				evidenceCode.setId(result.getInt(c.getcEvidenceCodesId()));
				evidenceCode
						.setCode(result.getString(c.getcEvidenceCodesCode()));
				evidenceCode
						.setName(result.getString(c.getcEvidenceCodesName()));
				evidenceCodes.add(evidenceCode);
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return evidenceCodes;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcEvidenceCodesId())
				.append(CommandConstants.COMMA)
				.append(c.getcEvidenceCodesCode())
				.append(CommandConstants.COMMA)
				.append(c.getcEvidenceCodesName())
				.append(CommandConstants.FROM).append(c.gettEvidenceCodes());
		return sql.toString();
	}
}
