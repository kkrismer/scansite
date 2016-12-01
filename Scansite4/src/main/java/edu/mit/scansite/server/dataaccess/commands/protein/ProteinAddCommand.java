package edu.mit.scansite.server.dataaccess.commands.protein;

import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbInsertCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Protein;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinAddCommand extends DbInsertCommand {

	private Protein p = null;
	private int taxonId = -1;
	private DataSource dataSource;

	public ProteinAddCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, DbConnector dbConnector, Protein p,
			int taxonId, boolean useTempTablesForUpdate, DataSource dataSource) {
		super(dbAccessConfig, dbConstantsConfig, dbConnector);
		this.p = p;
		this.taxonId = taxonId;
		setUseOfTempTables(useTempTablesForUpdate);
		this.dataSource = dataSource;
	}

	@Override
	protected String getTableName() throws DataAccessException {
		return null; // no numeric id, so getting the latest id will not work
						// anyway!
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.INSERTINTO)
				.append(c.gettProteins(dataSource)).append('(');
		sql.append(c.getcProteinsIdentifier()).append(CommandConstants.COMMA)
				.append(c.getcProteinsClass()).append(CommandConstants.COMMA)
				.append(c.getcProteinsMolWeight())
				.append(CommandConstants.COMMA).append(c.getcProteinsPI())
				.append(CommandConstants.COMMA).append(c.getcProteinsPIPhos1())
				.append(CommandConstants.COMMA).append(c.getcProteinsPIPhos2())
				.append(CommandConstants.COMMA).append(c.getcProteinsPIPhos3())
				.append(CommandConstants.COMMA)
				.append(c.getcProteinsSequence())
				.append(CommandConstants.COMMA).append(c.getcTaxaId());
		sql.append(')').append(CommandConstants.VALUES).append('(');
		sql.append(CommandConstants.enquote(p.getIdentifier()))
				.append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(p.getOrganismClass()
						.getShortName())).append(CommandConstants.COMMA)
				.append(p.getMolecularWeight()).append(CommandConstants.COMMA)
				.append(p.getpI()).append(CommandConstants.COMMA)
				.append(p.getpIPhos1()).append(CommandConstants.COMMA)
				.append(p.getpIPhos2()).append(CommandConstants.COMMA)
				.append(p.getpIPhos3()).append(CommandConstants.COMMA)
				.append(CommandConstants.enquote(p.getSequence()))
				.append(CommandConstants.COMMA).append(taxonId);
		sql.append(')');
		return sql.toString();
	}

	@Override
	protected String getIdColumnName() {
		return null;
	}
}
