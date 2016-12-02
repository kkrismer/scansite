package edu.mit.scansite.shared.transferobjects.states;

import edu.mit.scansite.shared.transferobjects.HistogramStringency;

/**
 * @author Konstantin Krismer
 */
public class StringencyLevelWidgetState extends State {
	private HistogramStringency stringency;

	public StringencyLevelWidgetState() {

	}

	public StringencyLevelWidgetState(HistogramStringency stringency) {
		this.stringency = stringency;
	}

	public HistogramStringency getStringency() {
		return stringency;
	}

	public void setStringency(HistogramStringency stringency) {
		this.stringency = stringency;
	}
}
