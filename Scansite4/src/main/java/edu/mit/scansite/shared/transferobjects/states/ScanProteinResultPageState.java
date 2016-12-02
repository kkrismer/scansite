package edu.mit.scansite.shared.transferobjects.states;

import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;

/**
 * @author Konstantin Krismer
 */
public class ScanProteinResultPageState extends State {
	private ProteinScanResult proteinScanResult;

	public ScanProteinResultPageState() {

	}

	public ScanProteinResultPageState(ProteinScanResult proteinScanResult) {
		this.proteinScanResult = proteinScanResult;
	}

	public ProteinScanResult getProteinScanResult() {
		return proteinScanResult;
	}

	public void setProteinScanResult(ProteinScanResult proteinScanResult) {
		this.proteinScanResult = proteinScanResult;
	}
}
