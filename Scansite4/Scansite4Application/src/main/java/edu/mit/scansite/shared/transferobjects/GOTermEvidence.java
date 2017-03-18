package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Konstantin Krismer
 */
public class GOTermEvidence implements IsSerializable {
	private GOTerm goTerm;
	private EvidenceCode evidenceCode;

	public GOTermEvidence() {
		super();
	}

	public GOTermEvidence(GOTerm goTerm, EvidenceCode evidenceCode) {
		super();
		this.goTerm = goTerm;
		this.evidenceCode = evidenceCode;
	}

	public GOTerm getGoTerm() {
		return goTerm;
	}

	public void setGoTerm(GOTerm goTerm) {
		this.goTerm = goTerm;
	}

	public EvidenceCode getEvidenceCode() {
		return evidenceCode;
	}

	public void setEvidenceCode(EvidenceCode evidenceCode) {
		this.evidenceCode = evidenceCode;
	}
}
