package edu.mit.scansite.shared.dispatch.features;

import edu.mit.scansite.shared.transferobjects.DataSource;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinCheckOracleAction implements
		Action<ProteinCheckOracleResult> {

	private DataSource dataSource;
	private String identifier;

	public ProteinCheckOracleAction() {
	}

	public ProteinCheckOracleAction(DataSource dataSource, String identifier) {
		this.dataSource = dataSource;
		this.identifier = identifier;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
