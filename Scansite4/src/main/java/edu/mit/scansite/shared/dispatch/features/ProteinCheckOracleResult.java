package edu.mit.scansite.shared.dispatch.features;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinCheckOracleResult implements Result {
	private boolean isSuccess = true;
	private String errorMessage = "";

	private List<String> proteinIdentifiers;

	public ProteinCheckOracleResult() {
	}

	public ProteinCheckOracleResult(String errorMessage) {
		isSuccess = false;
		this.errorMessage = errorMessage;
	}

	public ProteinCheckOracleResult(List<String> proteinIdentifiers) {
		this.proteinIdentifiers = proteinIdentifiers;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<String> getProteinIdentifiers() {
		return proteinIdentifiers;
	}

	public void setProteinIdentifiers(List<String> proteinIdentifiers) {
		this.proteinIdentifiers = proteinIdentifiers;
	}
}
