package edu.mit.scansite.server.dataaccess.commands.histogram;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramAddCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ServerHistogram h;
	private CommandConstants c = CommandConstants.instance();

	public HistogramAddCommand(Properties dbConstantsConfig, ServerHistogram hist) {
		if (c == null) {
			c = CommandConstants.instance(dbConstantsConfig);
		}
		h = hist;
	}

	public void execute() throws DataAccessException {
		String query = "";
		Connection connection = null;
		File f = new File(h.getImageFilePath());
		try {
			connection = DbConnector.getInstance().getConnection();
			query = getSqlStatement();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setBlob(1, new FileInputStream(f), f.length());
			statement.executeUpdate();
			DbConnector.getInstance().close(statement);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		} finally {

		}
	}

	private String getSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();

		sql.append(CommandConstants.INSERTINTO).append(c.gettHistograms())
				.append('(').append(c.getcMotifsId())
				.append(CommandConstants.COMMA).append(c.getcDataSourcesId())
				.append(CommandConstants.COMMA)
				.append(c.getcHistogramsTaxonId())
				.append(CommandConstants.COMMA)
				.append(c.getcHistogramsThreshHigh())
				.append(CommandConstants.COMMA)
				.append(c.getcHistogramsThreshMed())
				.append(CommandConstants.COMMA)
				.append(c.getcHistogramsThreshLow())
				.append(CommandConstants.COMMA)
				.append(c.getcHistogramsMedian())
				.append(CommandConstants.COMMA)
				.append(c.getcHistogramsMedianAbsDev())
				.append(CommandConstants.COMMA)
				.append(c.getcHistogramsSitesScored())
				.append(CommandConstants.COMMA)
				.append(c.getcHistogramsProteinsScored())
				.append(CommandConstants.COMMA)
				.append(c.getcHistogramsPlotImage()).append(')')
				.append(CommandConstants.VALUES).append('(')
				.append(h.getMotif().getId()).append(CommandConstants.COMMA)
				.append(h.getDataSource().getId())
				.append(CommandConstants.COMMA).append(h.getTaxon().getId())
				.append(CommandConstants.COMMA).append(h.getThresholdHigh())
				.append(CommandConstants.COMMA).append(h.getThresholdMedium())
				.append(CommandConstants.COMMA).append(h.getThresholdLow())
				.append(CommandConstants.COMMA).append(h.getMedian())
				.append(CommandConstants.COMMA).append(h.getMedianAbsDev())
				.append(CommandConstants.COMMA).append(h.getSitesScored())
				.append(CommandConstants.COMMA).append(h.getProteinsScored())
				.append(CommandConstants.COMMA).append('?').append(')');
		return sql.toString();
	}
}
