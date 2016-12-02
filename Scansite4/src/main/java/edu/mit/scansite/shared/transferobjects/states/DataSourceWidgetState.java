package edu.mit.scansite.shared.transferobjects.states;

import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Konstantin Krismer
 */
public class DataSourceWidgetState extends State {
	private DataSource dataSource;

	public DataSourceWidgetState() {

	}

	public DataSourceWidgetState(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
