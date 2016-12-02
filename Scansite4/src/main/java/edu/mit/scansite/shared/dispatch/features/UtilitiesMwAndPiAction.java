package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UtilitiesMwAndPiAction implements Action<UtilitiesMwAndPiResult> {
	private LightWeightProtein protein;
	private int maxSites;

	public UtilitiesMwAndPiAction() {
	}

	public UtilitiesMwAndPiAction(LightWeightProtein protein, int maxSites) {
		this.protein = protein;
		this.maxSites = maxSites;
	}

	public LightWeightProtein getProtein() {
		return protein;
	}

	public void setProtein(LightWeightProtein protein) {
		this.protein = protein;
	}

	public int getMaxSites() {
		return maxSites;
	}

	public void setMaxSites(int maxSites) {
		this.maxSites = maxSites;
	}
}
