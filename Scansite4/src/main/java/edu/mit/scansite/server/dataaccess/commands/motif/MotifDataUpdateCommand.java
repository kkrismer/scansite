package edu.mit.scansite.server.dataaccess.commands.motif;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.AminoAcid;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifDataUpdateCommand extends DbUpdateCommand {

	private Motif motif;
	private int position;

	public MotifDataUpdateCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, Motif motif,
			int position) {
		super(dbAccessConfig, dbConstantsConfig);
		this.motif = motif;
		this.position = position;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		AminoAcid aas[] = AminoAcid.values();

		sql.append(CommandConstants.UPDATE).append(c.gettMotifMatrixData());
		sql.append(CommandConstants.SET);
		for (AminoAcid aa : aas) {
			if (!aa.equals(AminoAcid._C) && !aa.equals(AminoAcid._N)) {
				sql.append(c.getcMatrixDataScorePrefix())
						.append(aa.getOneLetterCode())
						.append(CommandConstants.EQ)
						.append(motif.getValue(aa, position))
						.append(CommandConstants.COMMA);
			}
		}
		sql.append(c.getcMatrixDataScoreEnd()).append(CommandConstants.EQ)
				.append(motif.getValue(AminoAcid._C, position))
				.append(CommandConstants.COMMA);
		sql.append(c.getcMatrixDataScoreStart()).append(CommandConstants.EQ)
				.append(motif.getValue(AminoAcid._N, position));
		sql.append(CommandConstants.WHERE);
		sql.append(c.getcMotifsId()).append(CommandConstants.EQ)
				.append(motif.getId()).append(CommandConstants.AND);
		sql.append(c.getcMatrixDataPosition()).append(CommandConstants.EQ)
				.append(position);
		return sql.toString();
	}

}
