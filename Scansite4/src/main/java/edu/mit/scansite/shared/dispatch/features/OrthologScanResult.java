package edu.mit.scansite.shared.dispatch.features;

import java.util.LinkedList;
import java.util.List;

import net.customware.gwt.dispatch.shared.Result;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Ortholog;
import edu.mit.scansite.shared.transferobjects.Parameter;
import edu.mit.scansite.shared.transferobjects.SequenceAlignment;

/**
 * @author Konstantin Krismer
 */
public class OrthologScanResult implements Result {
	private DataSource orthologyDataSource;
	private LightWeightProtein protein;
	private HistogramStringency stringency;
	private int alignmentRadius;
	private int nrOfConservedPhosphoSites = 0;
	private List<Ortholog> orthologs;
	private SequenceAlignment sequenceAlignment;
	private boolean success = true;
	private String failureMessage = null;

	public OrthologScanResult() {
		super();
	}

	public OrthologScanResult(OrthologScanResult other) {
		super();
		this.orthologyDataSource = other.orthologyDataSource;
		this.protein = other.protein;
		this.stringency = other.stringency;
		this.alignmentRadius = other.alignmentRadius;
		this.nrOfConservedPhosphoSites = other.nrOfConservedPhosphoSites;
		this.orthologs = other.orthologs;
		this.sequenceAlignment = other.sequenceAlignment;
	}

	public OrthologScanResult(DataSource orthologyDataSource,
			LightWeightProtein protein, HistogramStringency stringency,
			int alignmentRadius) {
		super();
		this.orthologyDataSource = orthologyDataSource;
		this.protein = protein;
		this.stringency = stringency;
		this.alignmentRadius = alignmentRadius;
	}

	public OrthologScanResult(DataSource orthologyDataSource,
			LightWeightProtein protein, HistogramStringency stringency,
			int alignmentRadius, int nrOfConservedPhosphoSites,
			String siteSequence, List<Ortholog> orthologs,
			SequenceAlignment sequenceAlignment) {
		super();
		this.orthologyDataSource = orthologyDataSource;
		this.protein = protein;
		this.stringency = stringency;
		this.alignmentRadius = alignmentRadius;
		this.nrOfConservedPhosphoSites = nrOfConservedPhosphoSites;
		this.orthologs = orthologs;
		this.sequenceAlignment = sequenceAlignment;
	}

	public OrthologScanResult(String failureMessage) {
		success = false;
		this.failureMessage = failureMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getFailureMessage() {
		return failureMessage;
	}

	public DataSource getOrthologyDataSource() {
		return orthologyDataSource;
	}

	public void setOrthologyDataSource(DataSource orthologyDataSource) {
		this.orthologyDataSource = orthologyDataSource;
	}

	public LightWeightProtein getProtein() {
		return protein;
	}

	public void setProtein(LightWeightProtein protein) {
		this.protein = protein;
	}

	public HistogramStringency getStringency() {
		return stringency;
	}

	public void setStringency(HistogramStringency stringency) {
		this.stringency = stringency;
	}

	public int getAlignmentRadius() {
		return alignmentRadius;
	}

	public void setAlignmentRadius(int alignmentRadius) {
		this.alignmentRadius = alignmentRadius;
	}

	public int getNrOfConservedPhosphoSites() {
		return nrOfConservedPhosphoSites;
	}

	public void setNrOfConservedPhosphoSites(int nrOfConservedPhosphoSites) {
		this.nrOfConservedPhosphoSites = nrOfConservedPhosphoSites;
	}

	public List<Ortholog> getOrthologs() {
		return orthologs;
	}

	public void setOrthologs(List<Ortholog> orthologs) {
		this.orthologs = orthologs;
	}

	public SequenceAlignment getSequenceAlignment() {
		return sequenceAlignment;
	}

	public void setSequenceAlignment(SequenceAlignment sequenceAlignment) {
		this.sequenceAlignment = sequenceAlignment;
	}

	public List<Parameter> getInputParameters() {
		List<Parameter> inputParams = new LinkedList<Parameter>();

		inputParams.add(new Parameter("Orthology database", orthologyDataSource
				.getDisplayName()));
		inputParams.add(new Parameter("Protein database", protein
				.getDataSource().getDisplayName()));
		inputParams.add(new Parameter("Protein / gene identifier", protein
				.getIdentifier()));
		inputParams.add(new Parameter("Site prediction stringency", stringency
				.getName()));
		inputParams.add(new Parameter("Sequence alignment radius", Integer
				.toString(alignmentRadius)));

		return inputParams;
	}
}
