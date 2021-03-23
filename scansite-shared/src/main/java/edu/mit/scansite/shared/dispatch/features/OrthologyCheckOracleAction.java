package edu.mit.scansite.shared.dispatch.features;

import edu.mit.scansite.shared.transferobjects.DataSource;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Konstantin Krismer
 */
public class OrthologyCheckOracleAction implements
		Action<OrthologyCheckOracleResult> {

	private DataSource orthologyDataSource;
	private String identifier;

	public OrthologyCheckOracleAction() {

	}

	public OrthologyCheckOracleAction(DataSource orthologyDataSource,
			String identifier) {
		this.orthologyDataSource = orthologyDataSource;
		this.identifier = identifier;
	}

	public DataSource getOrthologyDataSource() {
		return orthologyDataSource;
	}

	public void setOrthologyDataSource(DataSource orthologyDataSource) {
		this.orthologyDataSource = orthologyDataSource;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
