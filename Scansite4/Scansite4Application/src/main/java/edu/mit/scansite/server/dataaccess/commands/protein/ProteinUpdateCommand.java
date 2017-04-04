package edu.mit.scansite.server.dataaccess.commands.protein;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Protein;

import java.util.Properties;

/**
 * Created by Thomas on 3/25/2017.
 */
public class ProteinUpdateCommand extends DbUpdateCommand {
    private Protein protein;
    private DataSource dataSource; //swissprot

    public ProteinUpdateCommand(Properties dbAccessConfig, Properties dbConstantsConfig, DataSource dataSource, Protein protein) {
        super(dbAccessConfig, dbConstantsConfig);
        this.protein = protein;
        this.dataSource = dataSource;
    }

    @Override
    protected String doGetSqlStatement() throws DataAccessException {
        StringBuilder sql = new StringBuilder(); // identifier, taxaID and proteinClass are not supposed to change
        sql.append(c.UPDATE).append(c.getProteins(dataSource)).append(c.SET)
                .append(c.getcProteinsMolWeight()).append(c.EQ).append(c.enquote(
                        String.valueOf(protein.getMolecularWeight()))).append(c.COMMA)
                .append(c.getcProteinsPI()).append(c.EQ).append(c.enquote(
                        String.valueOf(protein.getpI()))).append(c.COMMA)
                .append(c.getcProteinsPIPhos1()).append(c.EQ).append(c.enquote(
                        String.valueOf(protein.getpIPhos1()))).append(c.COMMA)
                .append(c.getcProteinsPIPhos2()).append(c.EQ).append(c.enquote(
                        String.valueOf(protein.getpIPhos2()))).append(c.COMMA)
                .append(c.getcProteinsPIPhos3()).append(c.EQ).append(c.enquote(
                        String.valueOf(protein.getpIPhos3()))).append(c.COMMA)
                .append(c.getcProteinsSequence()).append(c.EQ).append(c.enquote(
                        protein.getSequence())); //primarily changed element

                sql.append(c.WHERE).append(c.getcProteinsIdentifier()).append(c.EQ).append(c.enquote(protein.getIdentifier()));
        return sql.toString();
    }
}
