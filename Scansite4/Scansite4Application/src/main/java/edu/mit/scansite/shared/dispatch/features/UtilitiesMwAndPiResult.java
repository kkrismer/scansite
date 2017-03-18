package edu.mit.scansite.shared.dispatch.features;

import java.util.ArrayList;

import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UtilitiesMwAndPiResult implements Result {
	private boolean isSuccess = true;
	private String errorMessage = "";

	private LightWeightProtein protein;
	private int maxSites = 0;

	private ArrayList<Double> molecularWeights;
	private ArrayList<Double> isoelectricPoints;

	public UtilitiesMwAndPiResult() {
	}

	public UtilitiesMwAndPiResult(ArrayList<Double> molecularWeights,
			ArrayList<Double> isoelectricPoints, LightWeightProtein protein,
			int maxSites) {
		this.molecularWeights = molecularWeights;
		this.isoelectricPoints = isoelectricPoints;
		this.maxSites = maxSites;
		this.protein = protein;
	}

	public UtilitiesMwAndPiResult(boolean isSuccess, String errorMessage) {
		this.isSuccess = isSuccess;
		this.errorMessage = errorMessage;
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

	public ArrayList<Double> getMolecularWeights() {
		return molecularWeights;
	}

	public void setMolecularWeights(ArrayList<Double> molecularWeights) {
		this.molecularWeights = molecularWeights;
	}

	public ArrayList<Double> getIsoelectricPoints() {
		return isoelectricPoints;
	}

	public void setIsoelectricPoints(ArrayList<Double> isoelectricPoints) {
		this.isoelectricPoints = isoelectricPoints;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
