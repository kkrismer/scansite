package edu.mit.scansite.server.dataaccess.commands.identifiertype;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.IdentifierType;

/**
 * @author Konstantin Krismer
 */
public class IdentifierTypeUpdateCommand extends DbUpdateCommand {
	private IdentifierType identifierType;

	public IdentifierTypeUpdateCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, IdentifierType identifierType) {
		super(dbAccessConfig, dbConstantsConfig);
		this.identifierType = identifierType;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.UPDATE).append(c.gettIdentifierTypes());
		sql.append(CommandConstants.SET);
		sql.append(c.getcIdentifierTypesName()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(identifierType.getName()));
		sql.append(CommandConstants.WHERE);
		sql.append(c.getcIdentifierTypesId()).append(CommandConstants.EQ)
				.append(identifierType.getId());
		return sql.toString();
	}
}
