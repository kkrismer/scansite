package edu.mit.scansite.shared.dispatch.motif;

import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramRetrieverAction implements
		Action<HistogramRetrieverResult> {

	private int motifId;
	private String taxonName;
	private DataSource dataSource;
	private ScanResultSite site;
	private int topPosition = 0;
	private int leftPosition = 0;

	public HistogramRetrieverAction() {
	}

	public HistogramRetrieverAction(int motifId, String taxonName,
			DataSource dataSource, ScanResultSite site) {
		this.motifId = motifId;
		this.taxonName = taxonName;
		this.dataSource = dataSource;
		this.site = site;
	}

	public HistogramRetrieverAction(int motifId, String taxonName,
			DataSource dataSource, ScanResultSite site, int topPosition,
			int leftPosition) {
		this.motifId = motifId;
		this.taxonName = taxonName;
		this.dataSource = dataSource;
		this.site = site;
		this.topPosition = topPosition;
		this.leftPosition = leftPosition;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public int getMotifId() {
		return motifId;
	}

	public void setMotifId(int motifId) {
		this.motifId = motifId;
	}

	public String getTaxonName() {
		return taxonName;
	}

	public void setTaxonName(String taxonName) {
		this.taxonName = taxonName;
	}

	public ScanResultSite getSite() {
		return site;
	}

	public void setSite(ScanResultSite site) {
		this.site = site;
	}

	public int getTopPosition() {
		return topPosition;
	}

	public void setTopPosition(int topPosition) {
		this.topPosition = topPosition;
	}

	public int getLeftPosition() {
		return leftPosition;
	}

	public void setLeftPosition(int leftPosition) {
		this.leftPosition = leftPosition;
	}
}
