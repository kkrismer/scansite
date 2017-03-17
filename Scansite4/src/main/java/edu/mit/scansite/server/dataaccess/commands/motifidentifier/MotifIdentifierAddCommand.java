package edu.mit.scansite.server.dataaccess.commands.motifidentifier;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Konstantin Krismer
 */
public class MotifIdentifierAddCommand extends DbInsertCommand {

	private Motif motif;
	private Identifier identifier;

	public MotifIdentifierAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, Motif motif, Identifier identifier) {
		super(dbAccessConfig, dbConstantsConfig);
		this.motif = motif;
		this.identifier = identifier;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return c.gettMotifIdentifierMapping();
	}

	@Override
	protected String getIdColumnName() {
		return c.getcMotifIdentifierMappingId();
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();

		sql.append(CommandConstants.INSERTINTO)
				.append(c.gettMotifIdentifierMapping()).append('(')
				.append(c.getcMotifsId()).append(CommandConstants.COMMA)
				.append(c.getcIdentifierTypesId())
				.append(CommandConstants.COMMA)
				.append(c.getcMotifIdentifierMappingIdentifier()).append(')')
				.append(CommandConstants.VALUES).append('(')
				.append(motif.getId()).append(CommandConstants.COMMA)
				.append(identifier.getType().getId())
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(identifier.getValue()))
				.append(')');
		return sql.toString();
	}
}
