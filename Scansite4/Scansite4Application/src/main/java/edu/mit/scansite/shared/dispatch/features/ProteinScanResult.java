package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Result;
import edu.mit.scansite.shared.transferobjects.ScanResults;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinScanResult implements Result {
	private boolean success = true;
	private String failureMessage = null;

	private ScanResults results;

	public ProteinScanResult() {

	}

	public ProteinScanResult(String failureMessage) {
		success = false;
		this.failureMessage = failureMessage;
	}

	public ProteinScanResult(ScanResults results) {
		this.results = results;
	}

	public boolean isSuccess() {
		return success;
	}

	public ScanResults getResults() {
		return results;
	}

	public String getFailureMessage() {
		return failureMessage;
	}
}
