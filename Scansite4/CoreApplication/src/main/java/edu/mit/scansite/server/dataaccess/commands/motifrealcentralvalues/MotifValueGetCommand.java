package edu.mit.scansite.server.dataaccess.commands.motifrealcentralvalues;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.shared.DataAccessException;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Created by Thomas on 4/12/2017.
 * Retrieves the actual motif central position values that are used in the logo representation
 * for newer motifs (for which those values are known)
 */
public class MotifValueGetCommand extends DbQueryCommand<Double[]> {
    private int motifId;
    // sorry for using this inconsistent and non-elegant approach, running out of time
    private static final String tableName = "actualCentralPositionPSSMValues";
    private static final String sColumn = "motifMatrixDataScoreS";
    private static final String tColumn = "motifMatrixDataScoreT";
    private static final String yColumn = "motifMatrixDataScoreY";

    public MotifValueGetCommand(Properties dbAccessConfig, Properties dbConstantsConfig, int motifId) {
        super(dbAccessConfig, dbConstantsConfig);
        this.motifId = motifId;
    }

    @Override
    protected Double[] doProcessResults(ResultSet result) throws DataAccessException {
        try {
            Double[] values = {0.0, 0.0, 0.0};
            while (result.next()) {
                if (motifId == result.getInt(c.getcMotifsId())) {

                    values[0] = result.getDouble(sColumn);
                    values[1] = result.getDouble(tColumn);
                    values[2] = result.getDouble(yColumn);

                }
            }
            return values;
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    protected String doGetSqlStatement() throws DataAccessException {
        StringBuilder sql = new StringBuilder();

        sql.append(CommandConstants.SELECT).append(c.getcMotifsId())
                .append(CommandConstants.COMMA)
                .append(sColumn)
                .append(CommandConstants.COMMA)
                .append(tColumn)
                .append(CommandConstants.COMMA)
                .append(yColumn)
                .append(CommandConstants.FROM)
                .append(tableName)
                .append(CommandConstants.WHERE)
                .append(c.getcMotifsId())
                .append(CommandConstants.EQ)
                .append(motifId);

        return sql.toString();
    }
}
