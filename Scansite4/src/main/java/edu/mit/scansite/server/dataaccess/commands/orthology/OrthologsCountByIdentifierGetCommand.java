package edu.mit.scansite.server.dataaccess.commands.orthology;

import java.sql.ResultSet;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Konstantin Krismer
 */
public class OrthologsCountByIdentifierGetCommand extends
		DbQueryCommand<Integer> {

	protected DataSource orthologyDataSource;
	protected String identifier;

	public OrthologsCountByIdentifierGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			boolean useTempTablesForUpdate, DataSource orthologyDataSource,
			String identifier) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		setUseOfTempTables(useTempTablesForUpdate);
		this.orthologyDataSource = orthologyDataSource;
		this.identifier = identifier;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		// SELECT COUNT(id)
		// FROM `orthologs_homologene`
		// WHERE groupId = (
		// SELECT groupId
		// FROM `orthologs_homologene`
		// WHERE identifier = "NP_001120800")

		sql.append(CommandConstants.SELECT).append("COUNT(")
				.append(c.getcOrthologsId()).append(')');
		sql.append(CommandConstants.FROM).append(
				c.getOrthologsTableName(orthologyDataSource));
		sql.append(CommandConstants.WHERE);
		sql.append(c.getcOrthologsGroupId()).append(CommandConstants.EQ)
				.append(CommandConstants.LPAR);
		sql.append(CommandConstants.SELECT).append(c.getcOrthologsGroupId())
				.append(CommandConstants.FROM)
				.append(c.getOrthologsTableName(orthologyDataSource))
				.append(CommandConstants.WHERE)
				.append(c.getcOrthologsIdentifier())
				.append(CommandConstants.EQ)
				.append(CommandConstants.enquote(identifier))
				.append(CommandConstants.RPAR);
		return sql.toString();
	}

	@Override
	protected Integer doProcessResults(ResultSet result)
			throws DataAccessException {
		try {
			if (result.next()) {
				return result.getInt(1);
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return 0;
	}
}
