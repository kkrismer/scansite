package edu.mit.scansite.server.dataaccess.commands.histogram;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramDataDeleteCommand extends DbUpdateCommand {

	private int taxonId = -1;
	private int datasourceId = -1;
	private int motifId = -1;

	public HistogramDataDeleteCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, int motifId,
			int datasourceId, int taxonId) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.taxonId = taxonId;
		this.datasourceId = datasourceId;
		this.motifId = motifId;
	}

	public HistogramDataDeleteCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, int motifId) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.motifId = motifId;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();

		sql.append(CommandConstants.DELETEFROM).append(c.gettHistogramData())
				.append(CommandConstants.WHERE).append(c.getcMotifsId())
				.append(CommandConstants.EQ).append(motifId);
		if (datasourceId > 0) {
			sql.append(CommandConstants.AND).append(c.getcDataSourcesId())
					.append(CommandConstants.EQ).append(datasourceId);
		}
		if (taxonId > 0) {
			sql.append(CommandConstants.AND).append(c.getcHistogramsTaxonId())
					.append(CommandConstants.EQ).append(taxonId);
		}
		return sql.toString();
	}
}
