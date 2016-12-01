package edu.mit.scansite.shared.transferobjects.states;

/**
 * @author Konstantin Krismer
 */
public class RegExMethodWidgetState extends State {
	private String regEx;

	public RegExMethodWidgetState() {

	}

	public RegExMethodWidgetState(String regEx) {
		super();
		this.regEx = regEx;
	}

	public String getRegEx() {
		return regEx;
	}

	public void setRegEx(String regEx) {
		this.regEx = regEx;
	}
}
