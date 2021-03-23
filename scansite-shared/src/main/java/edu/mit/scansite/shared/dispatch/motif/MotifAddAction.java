package edu.mit.scansite.shared.dispatch.motif;

import java.util.ArrayList;

import edu.mit.scansite.shared.transferobjects.Histogram;
import edu.mit.scansite.shared.transferobjects.Motif;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifAddAction implements Action<LightWeightMotifRetrieverResult> {

	private Motif motif;
	private String userSessionId = "";
	private ArrayList<Histogram> histograms = new ArrayList<Histogram>();
	private boolean isUpdate = false;

	public MotifAddAction() {

	}

	public MotifAddAction(Motif motif, String userSessionId) {
		this.motif = motif;
		this.userSessionId = userSessionId;
	}

	public void setMotif(Motif motif) {
		this.motif = motif;
	}

	public Motif getMotif() {
		return motif;
	}

	public void addHistogram(Histogram histogram) {
		histograms.add(histogram);
	}

	public void setHistograms(ArrayList<Histogram> histograms) {
		this.histograms = histograms;
	}

	public ArrayList<Histogram> getHistograms() {
		return histograms;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getUserSessionId() {
		return userSessionId;
	}

	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}
}
