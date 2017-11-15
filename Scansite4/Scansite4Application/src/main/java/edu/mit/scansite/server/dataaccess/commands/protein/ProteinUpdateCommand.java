package edu.mit.scansite.server.dataaccess.commands.protein;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbUpdateCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Protein;

/**
 * @author Thomas Bernwinkler
 */
public class ProteinUpdateCommand extends DbUpdateCommand {
	private Protein protein;
	private DataSource dataSource; // swissprot

	public ProteinUpdateCommand(Properties dbAccessConfig, Properties dbConstantsConfig, DataSource dataSource,
			Protein protein) {
		super(dbAccessConfig, dbConstantsConfig);
		this.protein = protein;
		this.dataSource = dataSource;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder(); // identifier, taxaID and proteinClass are not supposed to change
		sql.append(CommandConstants.UPDATE).append(c.getProteins(dataSource)).append(CommandConstants.SET)
				.append(c.getcProteinsMolWeight()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(String.valueOf(protein.getMolecularWeight())))
				.append(CommandConstants.COMMA).append(c.getcProteinsPI()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(String.valueOf(protein.getpI()))).append(CommandConstants.COMMA)
				.append(c.getcProteinsPIPhos1()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(String.valueOf(protein.getpIPhos1()))).append(CommandConstants.COMMA)
				.append(c.getcProteinsPIPhos2()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(String.valueOf(protein.getpIPhos2()))).append(CommandConstants.COMMA)
				.append(c.getcProteinsPIPhos3()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(String.valueOf(protein.getpIPhos3()))).append(CommandConstants.COMMA)
				.append(c.getcProteinsSequence()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(protein.getSequence()));
		// primarily changed element

		sql.append(CommandConstants.WHERE).append(c.getcProteinsIdentifier()).append(CommandConstants.EQ)
				.append(CommandConstants.enquote(protein.getIdentifier()));
		return sql.toString();
	}
}
