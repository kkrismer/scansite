package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinRetrieverAction implements Action<ProteinRetrieverResult> {
	private String proteinIdentifier;
	private DataSource dataSource;

	public ProteinRetrieverAction() {
		
	}

	public ProteinRetrieverAction(String proteinIdentifier,
			DataSource dataSource) {
		this.proteinIdentifier = proteinIdentifier;
		this.dataSource = dataSource;
	}

	public String getProteinIdentifier() {
		return proteinIdentifier;
	}

	public void setProteinIdentifier(String proteinIdentifier) {
		this.proteinIdentifier = proteinIdentifier;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
