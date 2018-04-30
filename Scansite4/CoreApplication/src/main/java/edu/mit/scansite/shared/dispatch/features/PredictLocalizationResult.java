package edu.mit.scansite.shared.dispatch.features;

import net.customware.gwt.dispatch.shared.Result;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Konstantin Krismer
 */
public abstract class PredictLocalizationResult implements Result {
	private DataSource localizationDataSource;
	private int totalProteinLocalizations;
	private boolean isSuccess = true;
	private String errorMessage;

	public PredictLocalizationResult() {

	}

	public PredictLocalizationResult(DataSource localizationDataSource,
			int totalProteinLocalizations) {
		super();
		this.localizationDataSource = localizationDataSource;
		this.totalProteinLocalizations = totalProteinLocalizations;
	}

	public PredictLocalizationResult(String errorMessage) {
		super();
		this.isSuccess = false;
		this.errorMessage = errorMessage;
	}

	public DataSource getLocalizationDataSource() {
		return localizationDataSource;
	}

	public void setLocalizationDataSource(DataSource localizationDataSource) {
		this.localizationDataSource = localizationDataSource;
	}

	public int getTotalProteinLocalizations() {
		return totalProteinLocalizations;
	}

	public void setTotalProteinLocalizations(int totalProteinLocalizations) {
		this.totalProteinLocalizations = totalProteinLocalizations;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
