package edu.mit.scansite.shared.dispatch.motif;

import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Motif;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramCreateAction implements Action<HistogramRetrieverResult> {
	private int histogramNr;
	private Motif motif;
	private String taxonName;
	private DataSource dataSource;

	public HistogramCreateAction() {
	}

	public void setHistogramNr(int histNr) {
		this.histogramNr = histNr;
	}

	public void setMotif(Motif motif) {
		this.motif = motif;
	}

	public void setTaxonName(String taxonName) {
		this.taxonName = taxonName;
	}

	public Motif getMotif() {
		return motif;
	}

	public int getHistogramNr() {
		return histogramNr;
	}

	public String getTaxonName() {
		return taxonName;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
