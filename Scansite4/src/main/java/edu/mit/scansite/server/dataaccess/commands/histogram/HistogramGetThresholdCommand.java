package edu.mit.scansite.server.dataaccess.commands.histogram;

import java.sql.ResultSet;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramGetThresholdCommand extends DbQueryCommand<Double> {
	private int motifId;
	private int taxonId;
	private DataSource dataSource;
	private HistogramStringency stringency;

	public HistogramGetThresholdCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, int motifId,
			int taxonId, DataSource dataSource, HistogramStringency stringency) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.motifId = motifId;
		this.taxonId = taxonId;
		this.dataSource = dataSource;
		this.stringency = stringency;
	}

	@Override
	protected Double doProcessResults(ResultSet result)
			throws DataAccessException {
		try {
			if (result.next()) {
				return result.getDouble(1);
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT);
		if (stringency.equals(HistogramStringency.STRINGENCY_MEDIUM)) {
			sql.append(c.getcHistogramsThreshMed());
		} else if (stringency.equals(HistogramStringency.STRINGENCY_LOW)) {
			sql.append(c.getcHistogramsThreshLow());
		} else { // (stringency.equals(HistogramStringency.STRINGENCY_HIGH)) {
			sql.append(c.getcHistogramsThreshHigh());
		}
		sql.append(CommandConstants.FROM);
		sql.append(c.gettHistograms());
		sql.append(CommandConstants.WHERE);
		sql.append(c.getcMotifsId()).append(CommandConstants.EQ)
				.append(motifId);
		sql.append(CommandConstants.AND);
		sql.append(c.getcHistogramsTaxonId()).append(CommandConstants.EQ)
				.append(taxonId);
		sql.append(CommandConstants.AND);
		sql.append(c.getcDataSourcesId()).append(CommandConstants.EQ)
				.append(dataSource.getId());

		return sql.toString();
	}
}
