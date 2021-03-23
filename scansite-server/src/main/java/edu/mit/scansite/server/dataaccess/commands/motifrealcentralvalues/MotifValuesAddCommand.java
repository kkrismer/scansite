package edu.mit.scansite.server.dataaccess.commands.motifrealcentralvalues;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.shared.DataAccessException;

import java.util.Properties;

/**
 * Created by Thomas on 4/12/2017.
 * Inserts the actual motif values into the database (if given, i.e. for Nek6)
 * Those values will be used for the logo representation
 */
public class MotifValuesAddCommand extends DbInsertCommand {
    private static final String tableName = "actualCentralPositionPSSMValues";
    private Double[] values;
    private int motifId;

    public MotifValuesAddCommand(Properties dbAccessConfig,
                           Properties dbConstantsConfig, Double[] values, int motifId) {
        super(dbAccessConfig, dbConstantsConfig);
        this.motifId = motifId;
        this. values = values;
    }

    @Override
    protected String getTableName() throws DataAccessException {
        return tableName;
    }

    @Override
    protected String getIdColumnName() {
        return c.getcMotifsId();
    }

    @Override
    protected String doGetSqlStatement() throws DataAccessException {
        StringBuilder sql = new StringBuilder();

        double valueS = values[0];
        double valueT = values[1];
        double valueY = values[2];

        sql.append(CommandConstants.INSERTINTO)
                .append(tableName)
                .append('(')
                .append(c.getcMotifsId())
                .append(CommandConstants.COMMA)
                .append("motifMatrixDataScoreS")
                .append(CommandConstants.COMMA)
                .append("motifMatrixDataScoreT")
                .append(CommandConstants.COMMA)
                .append("motifMatrixDataScoreY")
                .append(')')
                .append(CommandConstants.VALUES)
                .append('(')
                .append(CommandConstants.enquote(String.valueOf(motifId)))
                .append(CommandConstants.COMMA)
                .append(CommandConstants.enquote(String.valueOf(valueS)))
                .append(CommandConstants.COMMA)
                .append(CommandConstants.enquote(String.valueOf(valueT)))
                .append(CommandConstants.COMMA)
                .append(CommandConstants.enquote(String.valueOf(valueY)))
                .append(')');
        return sql.toString();
    }
}
