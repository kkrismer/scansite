package edu.mit.scansite.server.dataaccess.commands.motifidentifier;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.IdentifierType;

/**
 * @author Konstantin Krismer
 */
public class MotifIdentifierGetAllCommand extends
		DbQueryCommand<Map<Integer, List<Identifier>>> {
	public MotifIdentifierGetAllCommand(Properties dbAccessConfig, Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	@Override
	protected Map<Integer, List<Identifier>> doProcessResults(ResultSet result)
			throws DataAccessException {
		Map<Integer, List<Identifier>> motifIdentifiers = new HashMap<>();

		try {
			while (result.next()) {
				int motifId = result.getInt(c.getcMotifsId());
				if (!motifIdentifiers.containsKey(motifId)) {
					motifIdentifiers.put(motifId, new LinkedList<Identifier>());
				}
				motifIdentifiers
						.get(motifId)
						.add(new Identifier(
								result.getString(c
										.getcMotifIdentifierMappingIdentifier()),
								new IdentifierType(result.getInt(c
										.getcIdentifierTypesId()), result
										.getString(c.getcIdentifierTypesName()))));
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return motifIdentifiers;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcMotifsId())
				.append(CommandConstants.COMMA)
				.append(c.getcMotifIdentifierMappingIdentifier())
				.append(CommandConstants.COMMA)
				.append(c.getcIdentifierTypesId())
				.append(CommandConstants.COMMA)
				.append(c.getcIdentifierTypesName())
				.append(CommandConstants.FROM)
				.append(c.gettMotifIdentifierMapping())
				.append(CommandConstants.INNERJOIN)
				.append(c.gettIdentifierTypes()).append(CommandConstants.USING)
				.append(CommandConstants.LPAR)
				.append(c.getcIdentifierTypesId())
				.append(CommandConstants.RPAR);
		return sql.toString();
	}
}
