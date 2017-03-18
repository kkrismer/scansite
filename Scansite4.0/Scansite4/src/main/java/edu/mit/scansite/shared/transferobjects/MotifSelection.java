package edu.mit.scansite.shared.transferobjects;

import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifSelection implements IsSerializable {
	private MotifClass motifClass = null;
	private Set<String> motifShortNames = null;
	private Motif userMotif = null;

	public MotifSelection() {

	}

	public MotifSelection(MotifClass motifClass) {
		this.motifClass = motifClass;
	}

	public MotifSelection(MotifClass motifClass, Set<String> motifShortNames) {
		this.motifClass = motifClass;
		if (motifShortNames != null && !motifShortNames.isEmpty()) {
			this.motifShortNames = motifShortNames;
		}
	}

	public MotifSelection(Motif userMotif) {
		this.userMotif = userMotif;
	}

	public MotifClass getMotifClass() {
		return motifClass;
	}

	public void setMotifClass(MotifClass motifClass) {
		this.motifClass = motifClass;
	}

	public Set<String> getMotifShortNames() {
		return motifShortNames;
	}

	public void setMotifShortNames(Set<String> motifShortNames) {
		this.motifShortNames = motifShortNames;
	}

	public Motif getUserMotif() {
		return userMotif;
	}

	public void setUserMotif(Motif userMotif) {
		this.userMotif = userMotif;
	}
	
	public boolean isDefault() {
		if(userMotif != null) {
			return false;
		}
		if(motifShortNames != null && !motifShortNames.isEmpty()) {
			return false;
		}
		if(motifClass != null && !motifClass.equals(MotifClass.MAMMALIAN)) {
			return false;
		}
		return true;
	}
}
