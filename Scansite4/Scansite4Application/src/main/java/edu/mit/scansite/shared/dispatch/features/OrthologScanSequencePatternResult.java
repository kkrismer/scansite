package edu.mit.scansite.shared.dispatch.features;

import java.util.List;

import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Ortholog;
import edu.mit.scansite.shared.transferobjects.Parameter;
import edu.mit.scansite.shared.transferobjects.SequenceAlignment;
import edu.mit.scansite.shared.transferobjects.SequencePattern;

/**
 * @author Konstantin Krismer
 */
public class OrthologScanSequencePatternResult extends OrthologScanResult {
	private SequencePattern sequencePattern;

	public OrthologScanSequencePatternResult() {
		super();
	}

	public OrthologScanSequencePatternResult(OrthologScanResult other) {
		super(other);
	}

	public OrthologScanSequencePatternResult(DataSource orthologySource,
			LightWeightProtein protein, HistogramStringency stringency,
			int alignmentRadius, int nrOfConservedPhosphoSites,
			String siteSequence, List<Ortholog> orthologs,
			SequenceAlignment sequenceAlignment, SequencePattern sequencePattern) {
		super(orthologySource, protein, stringency, alignmentRadius,
				nrOfConservedPhosphoSites, siteSequence, orthologs,
				sequenceAlignment);
		this.sequencePattern = sequencePattern;
	}
	
	public OrthologScanSequencePatternResult(String failureMessage) {
		super(failureMessage);
	}

	public SequencePattern getSequencePattern() {
		return sequencePattern;
	}

	public void setSequencePattern(SequencePattern sequencePattern) {
		this.sequencePattern = sequencePattern;
	}

	public List<Parameter> getInputParameters() {
		List<Parameter> inputParams = super.getInputParameters();

		inputParams.add(new Parameter("Sequence pattern", sequencePattern
				.getHtmlFormattedRegEx()));

		return inputParams;
	}
}
