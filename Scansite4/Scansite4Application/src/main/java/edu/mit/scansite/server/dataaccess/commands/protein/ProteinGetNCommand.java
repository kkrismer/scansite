package edu.mit.scansite.server.dataaccess.commands.protein;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.OrganismClass;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.Taxon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class ProteinGetNCommand extends DbQueryCommand<ArrayList<Protein>> {

    private DataSource dataSource;
    private int startIdx;
    private int count;
    private HashMap<String, OrganismClass> classMap = new HashMap<String, OrganismClass>();


    public ProteinGetNCommand(Properties dbAccessConfig, Properties dbConstantsConfig,
                              DataSource dataSource, int startIdx, int count) {
        super(dbAccessConfig, dbConstantsConfig);
        this.dataSource = dataSource;
        this.startIdx = startIdx;
        this.count = count;
        initClassMap();
    }

    private void initClassMap() {
        for (OrganismClass oClass : OrganismClass.values()) {
            classMap.put(oClass.getShortName(), oClass);
        }
    }


    /**
     * The same as for "ProteinGetAllCommand"
     */
    @Override
    protected ArrayList<Protein> doProcessResults(ResultSet result) throws DataAccessException {
        ArrayList<Protein> proteins = new ArrayList<Protein>();
        try {
            while (result.next()) {
                Taxon taxon = new Taxon(result.getInt(c.getcTaxaId()));
                Protein p = new Protein(result.getString(c
                        .getcProteinsIdentifier()), dataSource,
                        result.getString(c.getcProteinsSequence()), taxon,
                        result.getDouble(c.getcProteinsMolWeight()),
                        result.getDouble(c.getcProteinsPI()));
                p.setOrganismClass(classMap.get(result.getString(c
                        .getcProteinsClass())));
                proteins.add(p);
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error fetching proteins from database!", e);
        }
        return proteins;
    }

    @Override
    protected String doGetSqlStatement() throws DataAccessException {
        StringBuilder sql = new StringBuilder();
        sql.append(CommandConstants.SELECT)
                .append(c.getcProteinsIdentifier()).append(CommandConstants.COMMA)
                .append(c.getcTaxaId()).append(CommandConstants.COMMA)
                .append(c.getcProteinsClass()).append(CommandConstants.COMMA)
                .append(c.getcProteinsMolWeight()).append(CommandConstants.COMMA)
                .append(c.getcProteinsPI()).append(CommandConstants.COMMA)
                .append(c.getcProteinsSequence());
        sql.append(CommandConstants.FROM).append(c.getProteins(dataSource));

        if (startIdx == 0) { // retrieve first $count values
            sql.append(" LIMIT " + count);
        } else { // retrieve $count values starting at position $startIdx + 1
            sql.append(" LIMIT " + startIdx).append(CommandConstants.COMMA).append(count);
        }

        return sql.toString();
    }
}
