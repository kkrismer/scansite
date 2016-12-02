package edu.mit.scansite.server.dataaccess.commands.motif;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.AminoAcid;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifDataAddCommand extends DbInsertCommand {

	private Motif m;
	private int position;

	public MotifDataAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, Motif motif,
			int position) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.m = motif;
		this.position = position;
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
		AminoAcid aas[] = AminoAcid.values();

		sql.append(CommandConstants.INSERTINTO).append(c.gettMotifMatrixData())
				.append('(').append(c.getcMotifsId())
				.append(CommandConstants.COMMA)
				.append(c.getcMatrixDataPosition())
				.append(CommandConstants.COMMA)
				.append(c.getcMatrixDataScoreEnd())
				.append(CommandConstants.COMMA)
				.append(c.getcMatrixDataScoreStart());
		for (AminoAcid aa : aas) {
			if (!aa.equals(AminoAcid._C) && !aa.equals(AminoAcid._N)) {
				sql.append(CommandConstants.COMMA)
						.append(c.getcMatrixDataScorePrefix())
						.append(aa.getOneLetterCode());
			}
		}
		sql.append(')').append(CommandConstants.VALUES).append('(')
				.append(m.getId()).append(CommandConstants.COMMA)
				.append(position).append(CommandConstants.COMMA)
				.append(m.getValue(AminoAcid._C, position))
				.append(CommandConstants.COMMA)
				.append(m.getValue(AminoAcid._N, position));
		for (AminoAcid aa : aas) {
			if (!aa.equals(AminoAcid._C) && !aa.equals(AminoAcid._N)) {
				sql.append(CommandConstants.COMMA).append(
						m.getValue(aa, position));
			}
		}
		sql.append(')');
		return sql.toString();
	}
}
