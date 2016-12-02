package edu.mit.scansite.shared.transferobjects;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinSequenceMatch implements IsSerializable {
	private int[] nrOfSequenceMatches;
	private Protein protein;
	private List<ScanResultSite> phosphorylationSites;

	public static final ProvidesKey<ProteinSequenceMatch> KEY_PROVIDER = new ProvidesKey<ProteinSequenceMatch>() {
		public Object getKey(ProteinSequenceMatch item) {
			return item == null ? null : (item.getProtein() != null) ? item
					.getProtein().getIdentifier() : null;
		}
	};

	public ProteinSequenceMatch() {
	}

	public void setProtein(Protein p) {
		this.protein = p;
	}

	public Protein getProtein() {
		return protein;
	}

	public int[] getNrOfSequenceMatches() {
		return nrOfSequenceMatches;
	}

	public void setNrOfSequenceMatches(int[] nrOfSequenceMatches) {
		this.nrOfSequenceMatches = nrOfSequenceMatches;
	}

	public List<ScanResultSite> getPhosphorylationSites() {
		return phosphorylationSites;
	}

	public void setPhosphorylationSites(
			List<ScanResultSite> phosphorylationSites) {
		this.phosphorylationSites = phosphorylationSites;
	}
}
