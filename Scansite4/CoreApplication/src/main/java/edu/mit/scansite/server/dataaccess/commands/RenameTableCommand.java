package edu.mit.scansite.server.dataaccess.commands;

import java.util.Properties;

import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 */
public class RenameTableCommand extends DbUpdateCommand {

	private String fromTableName;
	private String toTableName;

	public RenameTableCommand(Properties dbAccessConfig, Properties dbConstantsConfig, String fromTableName,
			String toTableName) {
		super(dbAccessConfig, dbConstantsConfig);
		this.fromTableName = fromTableName;
		this.toTableName = toTableName;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		// The existing system is too complex to edit the statement in a good way
		// However, it should be sufficient to skip the TABLE checking on Linux
		// as the PLSQL code only works on Windows XAMPP but not on a Linux MySQL
		// database
		if (System.getProperty("os.name").equals("Linux")) {
			return "ALTER TABLE " + fromTableName + " RENAME " + toTableName;
		}
		return "IF EXISTS(SELECT table_name" + " FROM INFORMATION_SCHEMA.TABLES " + "WHERE table_schema = 'scansite4' "
				+ "AND table_name LIKE '" + fromTableName + "') THEN ALTER TABLE " + fromTableName + " RENAME "
				+ toTableName + "; END IF";
	}

	public void setFromTableName(String fromTableName) {
		this.fromTableName = fromTableName;
	}

	public void setToTableName(String toTableName) {
		this.toTableName = toTableName;
	}
}
