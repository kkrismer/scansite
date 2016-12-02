package edu.mit.scansite.shared.dispatch.features;

import java.util.List;

import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Ortholog;
import edu.mit.scansite.shared.transferobjects.Parameter;
import edu.mit.scansite.shared.transferobjects.SequenceAlignment;

/**
 * @author Konstantin Krismer
 */
public class OrthologScanMotifResult extends OrthologScanResult {
	private LightWeightMotifGroup motifGroup;
	private int sitePosition;

	public OrthologScanMotifResult() {
		super();
	}

	public OrthologScanMotifResult(OrthologScanResult other) {
		super(other);
	}

	public OrthologScanMotifResult(DataSource orthologySource,
			LightWeightProtein protein, HistogramStringency stringency,
			int alignmentRadius, int nrOfConservedPhosphoSites,
			String siteSequence, List<Ortholog> orthologs,
			SequenceAlignment sequenceAlignment,
			LightWeightMotifGroup motifGroup, int sitePosition) {
		super(orthologySource, protein, stringency, alignmentRadius,
				nrOfConservedPhosphoSites, siteSequence, orthologs,
				sequenceAlignment);
		this.motifGroup = motifGroup;
		this.sitePosition = sitePosition;
	}
	
	public OrthologScanMotifResult(String failureMessage) {
		super(failureMessage);
	}

	public LightWeightMotifGroup getMotifGroup() {
		return motifGroup;
	}

	public void setMotifGroup(LightWeightMotifGroup motifGroup) {
		this.motifGroup = motifGroup;
	}

	public int getSitePosition() {
		return sitePosition;
	}

	public void setSitePosition(int sitePosition) {
		this.sitePosition = sitePosition;
	}

	public List<Parameter> getInputParameters() {
		List<Parameter> inputParams = super.getInputParameters();

		inputParams.add(new Parameter("Motif group", motifGroup.getDisplayName()));
		inputParams.add(new Parameter("Site position in query protein",
				Integer.toString(sitePosition)));

		return inputParams;
	}
}
