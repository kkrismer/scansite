package edu.mit.scansite.shared.dispatch.features;

import java.util.List;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class GetNewProteinPlotAction implements Action<GetNewProteinPlotResult> {

	private LightWeightProtein protein;
	private List<ScanResultSite> predictedSites;

	public GetNewProteinPlotAction() {
	}

	public GetNewProteinPlotAction(LightWeightProtein protein,
			List<ScanResultSite> predictedSites) {
		this.protein = protein;
		this.predictedSites = predictedSites;
	}

	public LightWeightProtein getProtein() {
		return protein;
	}

	public void setProtein(LightWeightProtein protein) {
		this.protein = protein;
	}

	public List<ScanResultSite> getPredictedSites() {
		return predictedSites;
	}

	public void setPredictedSites(List<ScanResultSite> predictedSites) {
		this.predictedSites = predictedSites;
	}
}
