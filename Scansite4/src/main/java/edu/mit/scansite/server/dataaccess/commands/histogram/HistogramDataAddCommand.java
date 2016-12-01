package edu.mit.scansite.server.dataaccess.commands.histogram;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramDataAddCommand extends DbInsertCommand {

	private double score;
	private int freq;
	private ServerHistogram h;

	public HistogramDataAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector,
			ServerHistogram histogram, double score, int absFrequency) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.h = histogram;
		this.score = score;
		this.freq = absFrequency;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return null;
	}

	@Override
	protected String getIdColumnName() {
		return null;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();

		sql.append(CommandConstants.INSERTINTO).append(c.gettHistogramData())
				.append('(').append(c.getcMotifsId())
				.append(CommandConstants.COMMA).append(c.getcDataSourcesId())
				.append(CommandConstants.COMMA)
				.append(c.getcHistogramsTaxonId())
				.append(CommandConstants.COMMA)
				.append(c.getcHistogramDataScore())
				.append(CommandConstants.COMMA)
				.append(c.getcHistogramDataAbsFreq()).append(')')
				.append(CommandConstants.VALUES).append('(')
				.append(h.getMotif().getId()).append(CommandConstants.COMMA)
				.append(h.getDataSource().getId())
				.append(CommandConstants.COMMA).append(h.getTaxon().getId())
				.append(CommandConstants.COMMA).append(score)
				.append(CommandConstants.COMMA).append(freq).append(')');
		return sql.toString();
	}
}
