package edu.mit.scansite.server.dataaccess.commands.histogram;

import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramDataPoint;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramGetAllCommand extends DbQueryCommand<List<ServerHistogram>> {

	private int m = -1;
	private DataSource dataSource = null;
	private int t = -1;
	private int mIds[] = null;
	private Map<Integer, Motif> motifMap = null;
	private GetHistogramMode mode = GetHistogramMode.ALL;

	public enum GetHistogramMode {
		ALL, DATA_ONLY, GENERAL_INFO_ONLY;

		private GetHistogramMode() {
		}
	}

	public HistogramGetAllCommand(Properties dbAccessConfig, Properties dbConstantsConfig, int motifId,
			DataSource dataSource, int taxonId) {
		super(dbAccessConfig, dbConstantsConfig);
		m = motifId;
		this.dataSource = dataSource;
		t = taxonId;
	}

	public HistogramGetAllCommand(Properties dbAccessConfig, Properties dbConstantsConfig, List<Motif> motifs,
			DataSource dataSource, int taxonId, GetHistogramMode mode) {
		super(dbAccessConfig, dbConstantsConfig);
		this.dataSource = dataSource;
		t = taxonId;
		if (motifs == null || motifs.isEmpty()) {
			mIds = null;
			m = -1;
		} else {
			motifMap = new HashMap<Integer, Motif>();
			mIds = new int[motifs.size()];
			int i = 0;
			for (Motif m : motifs) {
				mIds[i++] = m.getId();
				motifMap.put(m.getId(), m);
			}
		}
		if (mode != null) {
			this.mode = mode;
		}
	}

	public HistogramGetAllCommand(Properties dbAccessConfig, Properties dbConstantsConfig, List<Motif> motifs,
			DataSource dataSource, int taxonId) {
		this(dbAccessConfig, dbConstantsConfig, motifs, dataSource, taxonId, GetHistogramMode.ALL);
	}

	@Override
	protected List<ServerHistogram> doProcessResults(ResultSet result) throws DataAccessException {
		List<ServerHistogram> hists = new ArrayList<ServerHistogram>();
		try {
			ServerHistogram h = null;
			int lastMotifId = -1;
			int lastTaxonId = -1;
			int lastDatasourceId = -1;
			while (result.next()) {
				int motifId = result.getInt(c.getcMotifsId());
				int taxonId = result.getInt(c.getcHistogramsTaxonId());
				int datasourceId = result.getInt(c.getcDataSourcesId());

				if (motifId != lastMotifId || taxonId != lastTaxonId || datasourceId != lastDatasourceId) {
					if (h != null) {
						hists.add(h);
					}
					Motif m = null;
					if (motifMap != null) {
						m = motifMap.get(motifId);
					} else {
						m = new Motif();
						m.setId(motifId);
					}
					Taxon t = new Taxon();
					t.setId(taxonId);
					DataSource dataSource = new DataSource();
					dataSource.setId(datasourceId);

					if (mode == GetHistogramMode.ALL || mode == GetHistogramMode.GENERAL_INFO_ONLY) {
						InputStream is = result.getBinaryStream(c.getcHistogramsPlotImage());
						h = new ServerHistogram(m, dataSource, t, result.getInt(c.getcHistogramsSitesScored()),
								result.getInt(c.getcHistogramsProteinsScored()), ImageIO.read(is));
						is.close();

						h.setMedian(result.getDouble(c.getcHistogramsMedian()));
						h.setMedianAbsDev(result.getDouble(c.getcHistogramsMedianAbsDev()));
						h.setThresholdHigh(result.getDouble(c.getcHistogramsThreshHigh()));
						h.setThresholdMedium(result.getDouble(c.getcHistogramsThreshMed()));
						h.setThresholdLow(result.getDouble(c.getcHistogramsThreshLow()));
					} else if (mode == GetHistogramMode.DATA_ONLY) {
						h = new ServerHistogram(m, dataSource, t);
					}
				}
				if (mode == GetHistogramMode.ALL || mode == GetHistogramMode.DATA_ONLY) {
					h.addDataPoint(new HistogramDataPoint(result.getDouble(c.getcHistogramDataScore()),
							result.getInt(c.getcHistogramDataAbsFreq())));
				}
				lastMotifId = motifId;
				lastTaxonId = taxonId;
				lastDatasourceId = datasourceId;
			}
			if (lastMotifId > 0 && lastTaxonId > 0 && lastDatasourceId > 0) {
				hists.add(h);
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage());
		}

		return hists;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();

		sql.append(CommandConstants.SELECT);
		sql.append(c.getcMotifsId()).append(CommandConstants.COMMA).append(c.getcDataSourcesId())
				.append(CommandConstants.COMMA).append(c.getcHistogramsTaxonId());
		if (mode == GetHistogramMode.GENERAL_INFO_ONLY || mode == GetHistogramMode.ALL) {
			sql.append(CommandConstants.COMMA).append(c.getcHistogramsThreshHigh()).append(CommandConstants.COMMA)
					.append(c.getcHistogramsThreshMed()).append(CommandConstants.COMMA)
					.append(c.getcHistogramsThreshLow()).append(CommandConstants.COMMA).append(c.getcHistogramsMedian())
					.append(CommandConstants.COMMA).append(c.getcHistogramsMedianAbsDev())
					.append(CommandConstants.COMMA).append(c.getcHistogramsSitesScored()).append(CommandConstants.COMMA)
					.append(c.getcHistogramsPlotImage()).append(CommandConstants.COMMA)
					.append(c.getcHistogramsProteinsScored());
		}
		if (mode == GetHistogramMode.DATA_ONLY || mode == GetHistogramMode.ALL) {
			sql.append(CommandConstants.COMMA);
			sql.append(c.getcHistogramDataScore()).append(CommandConstants.COMMA).append(c.getcHistogramDataAbsFreq());
		}
		sql.append(CommandConstants.FROM);
		if (mode == GetHistogramMode.GENERAL_INFO_ONLY || mode == GetHistogramMode.ALL) {
			sql.append(c.gettHistograms());
		}
		if (mode == GetHistogramMode.ALL) {
			sql.append(CommandConstants.JOIN).append(c.gettHistogramData()).append(CommandConstants.USING).append('(')
					.append(c.getcMotifsId()).append(CommandConstants.COMMA).append(c.getcDataSourcesId())
					.append(CommandConstants.COMMA).append(c.getcHistogramsTaxonId()).append(')');
		} else if (mode == GetHistogramMode.DATA_ONLY) {
			sql.append(c.gettHistogramData());
		}
		if (m > 0 || (t > 0 && dataSource != null)) {
			String where = CommandConstants.WHERE;
			if (m > 0) {
				sql.append(where).append(c.getcMotifsId()).append(CommandConstants.EQ).append(m);
				where = CommandConstants.AND;
			} else if (mIds != null && mIds.length > 0) {
				sql.append(where).append(" ( ");
				where = CommandConstants.OR;
				for (int i = 0; i < mIds.length; ++i) {
					if (i > 0) {
						sql.append(where);
					}
					sql.append(c.getcMotifsId()).append(CommandConstants.EQ).append(mIds[i]);
				}
				sql.append(" ) ");
				where = CommandConstants.AND;
			}
			if (dataSource != null) {
				sql.append(where).append(c.getcDataSourcesId()).append(CommandConstants.EQ).append(dataSource.getId());
				if (t > 0) {
					sql.append(CommandConstants.AND).append(c.getcHistogramsTaxonId()).append(CommandConstants.EQ)
							.append(t);
				}
			}
		}
		sql.append(CommandConstants.ORDERBY).append(c.getcMotifsId());
		sql.append(CommandConstants.COMMA).append(c.getcDataSourcesId());
		sql.append(CommandConstants.COMMA).append(c.getcHistogramsTaxonId());
		if (mode == GetHistogramMode.DATA_ONLY || mode == GetHistogramMode.ALL) {
			sql.append(CommandConstants.COMMA).append(c.getcHistogramDataScore());
		}
		return sql.toString();
	}
}
