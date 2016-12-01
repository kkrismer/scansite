package edu.mit.scansite.shared.transferobjects;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DatabaseSearchScanResultSite implements
		Comparable<DatabaseSearchScanResultSite>, IsSerializable {

	private boolean isMultiple = false;
	private List<ScanResultSite> sites;
	private int nrOfMotifs = 0;
	private double combinedScore = -1;
	private Protein protein;
	private GapPenalty gapPenalty = null;

	public static final ProvidesKey<DatabaseSearchScanResultSite> KEY_PROVIDER = new ProvidesKey<DatabaseSearchScanResultSite>() {
		public Object getKey(DatabaseSearchScanResultSite item) {
			return item == null ? null : item.isMultiple()
					+ item.getProtein().getIdentifier()
					+ item.getNumberOfSites() + item.getScore();
		}
	};

	public DatabaseSearchScanResultSite() { // for serialization purposes only
	}

	public DatabaseSearchScanResultSite(ScanResultSite site, Protein protein) {
		this.sites = new LinkedList<>();
		this.sites.add(site);
		this.isMultiple = false;
		this.protein = protein;
		this.combinedScore = site.getScore();
	}

	public DatabaseSearchScanResultSite(List<ScanResultSite> sites,
			int nMotifs, Protein protein) {
		this.isMultiple = true;
		this.sites = sites;
		this.nrOfMotifs = nMotifs;
		this.setProtein(protein);
	}

	public DatabaseSearchScanResultSite(List<ScanResultSite> sites,
			int nMotifs, Protein protein, GapPenalty gapPenalty) {
		this(sites, nMotifs, protein);
		this.isMultiple = true;
		this.gapPenalty = gapPenalty;
	}

	public double getCombinedScore() {
		if (combinedScore == -1) {
			calculateCombinedScore();
		}
		return combinedScore;
	}

	public double getScore() {
		return getCombinedScore();
	}

	public ScanResultSite getSite() {
		if (sites != null && sites.size() > 0) {
			return sites.get(0);
		} else {
			return null;
		}
	}

	private void calculateCombinedScore() {
		double sum = 0;
		int maxPos = Integer.MIN_VALUE;
		int minPos = Integer.MAX_VALUE;
		for (ScanResultSite site : sites) {
			sum += site.getScore();
			if (site.getPosition() <= minPos) {
				minPos = site.getPosition();
			}
			if (site.getPosition() >= maxPos) {
				maxPos = site.getPosition();
			}
		}
		combinedScore = sum / (double) nrOfMotifs;
		if (gapPenalty != null) {
			combinedScore += (maxPos - minPos) * gapPenalty.getValue();
		}
	}

	public List<ScanResultSite> getSites() {
		return sites;
	}

	public boolean isMultiple() {
		return isMultiple;
	}

	public Protein getProtein() {
		return protein;
	}

	public void setProtein(Protein protein) {
		this.protein = protein;
	}

	public int getNumberOfSites() {
		if (sites != null) {
			return sites.size();
		} else {
			return 0;
		}
	}

	@Override
	public int compareTo(DatabaseSearchScanResultSite o) {
		if (o != null) {
			double scoreMe = getCombinedScore();
			double scoreOther = o.getCombinedScore();
			return (scoreMe < scoreOther) ? -1 : (Double.compare(scoreMe, scoreOther) == 0) ? 0
					: 1;
		}
		return -1;
	}

	@Override
	public String toString() {
		return getCombinedScore() + " - " + protein.getIdentifier();
	}
}
