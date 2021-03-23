package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DomainPositionsGetAction implements
		Action<DomainPositionsGetResult> {

	private String proteinSequence;

	public DomainPositionsGetAction() {
	}

	public DomainPositionsGetAction(String proteinSequence) {
		this.proteinSequence = proteinSequence;
	}

	public String getProteinSequence() {
		return proteinSequence;
	}

	public void setProteinSequence(String proteinSequence) {
		this.proteinSequence = proteinSequence;
	}
}
