package edu.mit.scansite.shared.dispatch.features;

import java.util.ArrayList;

import net.customware.gwt.dispatch.shared.Action;
import edu.mit.scansite.shared.transferobjects.AminoAcid;
import edu.mit.scansite.shared.transferobjects.DomainPosition;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DomainPlotGetAction implements Action<DomainPlotGetResult> {
	private LightWeightProtein protein;
	private ArrayList<DomainPosition> domainPositions;
	private AminoAcid centerAminoAcid = AminoAcid.S;
	private AminoAcid relativeAminoAcid = AminoAcid.S;
	private int position = 0;

	public DomainPlotGetAction() {
	}

	/**
	 * @param protein
	 *            The protein to be displayed.
	 * @param domainPositions
	 *            Domains within the protein, or NULL if there was an error
	 *            finding domains.
	 * @param centerAa
	 *            the center amino acid to be marked.
	 * @param relativeAa
	 *            the amino acid relative to the centerAa at the given position.
	 * @param position
	 *            a position around the center amino acid ranging [-7,+7].
	 */
	public DomainPlotGetAction(LightWeightProtein protein,
			ArrayList<DomainPosition> domainPositions, AminoAcid centerAa,
			AminoAcid relativeAa, int position) {
		this.protein = protein;
		this.domainPositions = domainPositions;
		this.centerAminoAcid = centerAa;
		this.relativeAminoAcid = relativeAa;
		this.position = position;
	}

	public LightWeightProtein getProtein() {
		return protein;
	}

	public void setProtein(LightWeightProtein protein) {
		this.protein = protein;
	}

	public ArrayList<DomainPosition> getDomainPositions() {
		return domainPositions;
	}

	public void setDomainPositions(ArrayList<DomainPosition> domainPositions) {
		this.domainPositions = domainPositions;
	}

	public AminoAcid getCenterAminoAcid() {
		return centerAminoAcid;
	}

	public void setCenterAminoAcid(AminoAcid centerAminoAcid) {
		this.centerAminoAcid = centerAminoAcid;
	}

	public AminoAcid getRelativeAminoAcid() {
		return relativeAminoAcid;
	}

	public void setRelativeAminoAcid(AminoAcid relativeAminoAcid) {
		this.relativeAminoAcid = relativeAminoAcid;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
