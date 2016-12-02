package edu.mit.scansite.shared.transferobjects;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DatabaseSearchMultipleRestriction implements IsSerializable {

	private List<Motif> userMotifs;
	private List<String> dbMotifNicks;
	private List<Motif> dbMotifs;
	private boolean isGapRestrictionSearch = true;

	private GapPenalty gapPenalty = GapPenalty.HIGH;

	private ArrayList<Integer> idxsMotifLeft = new ArrayList<Integer>();
	private ArrayList<Boolean> idxsMotifLeftIsDbMotif = new ArrayList<Boolean>();
	private ArrayList<Integer> idxsMotifRight = new ArrayList<Integer>();
	private ArrayList<Boolean> idxsMotifRightIsDbMotif = new ArrayList<Boolean>();
	private ArrayList<Integer> residueRestrictions = new ArrayList<Integer>();
	private ArrayList<DistanceRestrictionRelation> restrictionRelations = new ArrayList<DistanceRestrictionRelation>();

	public DatabaseSearchMultipleRestriction() {
	}

	public DatabaseSearchMultipleRestriction(List<Motif> userMotifs,
			List<String> dbMotifNicks, boolean isGapRestrictionSearch) {
		super();
		this.userMotifs = userMotifs;
		this.dbMotifNicks = dbMotifNicks;
		this.isGapRestrictionSearch = isGapRestrictionSearch;
	}

	public void addRestriction(int motifIndexLeft, boolean isUserMotifLeft,
			int motifIndexRight, boolean isUserMotifRight, int nResidues,
			DistanceRestrictionRelation relation) {
		if (motifIndexLeft >= 0 && motifIndexRight >= 0 && nResidues >= 0
				&& relation != null) {
			idxsMotifLeft.add(motifIndexLeft);
			idxsMotifLeftIsDbMotif.add(isUserMotifLeft);
			idxsMotifRight.add(motifIndexRight);
			idxsMotifRightIsDbMotif.add(isUserMotifRight);
			residueRestrictions.add(nResidues);
			restrictionRelations.add(relation);
		}
	}

	public List<Motif> getUserMotifs() {
		return userMotifs;
	}

	public void setUserMotifs(List<Motif> userMotifs) {
		this.userMotifs = userMotifs;
	}

	public List<String> getDbMotifNicks() {
		return dbMotifNicks;
	}

	public void setDbMotifNicks(List<String> dbMotifNicks) {
		this.dbMotifNicks = dbMotifNicks;
	}

	public void setDbMotifs(List<Motif> motifs) {
		this.dbMotifs = motifs;
	}

	public boolean isGapRestrictionSearch() {
		return isGapRestrictionSearch;
	}

	public void setGapRestrictionSearch(boolean isGapRestrictionSearch) {
		this.isGapRestrictionSearch = isGapRestrictionSearch;
	}

	public GapPenalty getGapPenalty() {
		return gapPenalty;
	}

	public void setGapPenalty(GapPenalty gapPenalty) {
		this.gapPenalty = gapPenalty;
	}

	public ArrayList<Integer> getIdxsMotifLeft() {
		return idxsMotifLeft;
	}

	public void setIdxsMotifLeft(ArrayList<Integer> idxsMotifLeft) {
		this.idxsMotifLeft = idxsMotifLeft;
	}

	public ArrayList<Boolean> getIdxsMotifLeftIsDbMotif() {
		return idxsMotifLeftIsDbMotif;
	}

	public void setIdxsMotifLeftIsDbMotif(
			ArrayList<Boolean> idxsMotifLeftIsDbMotif) {
		this.idxsMotifLeftIsDbMotif = idxsMotifLeftIsDbMotif;
	}

	public ArrayList<Integer> getIdxsMotifRight() {
		return idxsMotifRight;
	}

	public void setIdxsMotifRight(ArrayList<Integer> idxsMotifRight) {
		this.idxsMotifRight = idxsMotifRight;
	}

	public ArrayList<Boolean> getIdxsMotifRightIsDbMotif() {
		return idxsMotifRightIsDbMotif;
	}

	public void setIdxsMotifRightIsDbMotif(
			ArrayList<Boolean> idxsMotifRightIsDbMotif) {
		this.idxsMotifRightIsDbMotif = idxsMotifRightIsDbMotif;
	}

	public ArrayList<Integer> getResidueRestrictions() {
		return residueRestrictions;
	}

	public void setResidueRestrictions(ArrayList<Integer> residueRestrictions) {
		this.residueRestrictions = residueRestrictions;
	}

	public ArrayList<DistanceRestrictionRelation> getRestrictionRelations() {
		return restrictionRelations;
	}

	public void setRestrictionRelations(
			ArrayList<DistanceRestrictionRelation> restrictionRelations) {
		this.restrictionRelations = restrictionRelations;
	}

	/**
	 * Make sure the database motifs are set as motifs before calling this
	 * method.
	 * 
	 * @param idx
	 *            The index as returned by getIdxsMotif[Left/Right].
	 * @param isDbMotif
	 *            A boolean value as returned by
	 *            getIdxsMotif[Left/Right]IsDbMotif.
	 * @return
	 */
	public Motif getMotif(int idx, boolean isDbMotif) {
		if (idx >= 0) {
			if (!isDbMotif && userMotifs.size() > idx) { // userMotif
				return userMotifs.get(idx);
			} else if (dbMotifs.size() > idx) { // dbMotif
				return dbMotifs.get(idx);
			}
		}
		return null;
	}
}
