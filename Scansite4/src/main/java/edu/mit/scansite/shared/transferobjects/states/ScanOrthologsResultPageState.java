package edu.mit.scansite.shared.transferobjects.states;

import edu.mit.scansite.shared.dispatch.features.OrthologScanResult;

/**
 * @author Konstantin Krismer
 */
public class ScanOrthologsResultPageState extends State {
	private OrthologScanResult orthologScanResult;

	public ScanOrthologsResultPageState() {

	}

	public ScanOrthologsResultPageState(OrthologScanResult orthologScanResult) {
		this.orthologScanResult = orthologScanResult;
	}

	public OrthologScanResult getOrthologScanResult() {
		return orthologScanResult;
	}

	public void setOrthologScanResult(OrthologScanResult orthologScanResult) {
		this.orthologScanResult = orthologScanResult;
	}
}
