package edu.mit.scansite.server.dataaccess.commands.protein;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;

import java.sql.ResultSet;
import java.util.Properties;

public class CountProteinsCommand extends DbQueryCommand<Integer> {

    private DataSource dataSource;

    public CountProteinsCommand(Properties dbAccessConfig, Properties dbConstantsConfig, DataSource dataSource) {
        super(dbAccessConfig, dbConstantsConfig);
        this.dataSource = dataSource;
    }

    @Override
    protected Integer doProcessResults(ResultSet result) throws DataAccessException {
        try {
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected String doGetSqlStatement() throws DataAccessException {
        setUseOfTempTables(false);
        StringBuilder sql = new StringBuilder();
        sql.append(CommandConstants.SELECT).append("COUNT(*)")
                .append(CommandConstants.FROM).append(c.getProteins(dataSource));

        return sql.toString();
    }
}
