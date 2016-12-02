package edu.mit.scansite.shared.transferobjects.states;

/**
 * @author Konstantin Krismer
 */
public class ChooseUserFileMotifWidgetState extends State {
	private String motifName;

	public ChooseUserFileMotifWidgetState() {

	}

	public ChooseUserFileMotifWidgetState(String motifName) {
		this.motifName = motifName;
	}

	public String getMotifName() {
		return motifName;
	}

	public void setMotifName(String motifName) {
		this.motifName = motifName;
	}
}
