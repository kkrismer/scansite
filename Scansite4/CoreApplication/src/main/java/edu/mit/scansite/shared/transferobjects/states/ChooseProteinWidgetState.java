package edu.mit.scansite.shared.transferobjects.states;

/**
 * @author Konstantin Krismer
 */
public class ChooseProteinWidgetState extends State {
	private DataSourceWidgetState dataSourceWidgetState;
	private boolean searchModeProteinIdentifierRadioButtonValue;
	private boolean searchModeInputSequenceRadioButtonValue;
	private String identifier;
	private String proteinName;
	private String proteinSequence;

	public ChooseProteinWidgetState() {

	}

	public ChooseProteinWidgetState(
			DataSourceWidgetState dataSourceWidgetState,
			boolean searchModeProteinIdentifierRadioButtonValue,
			boolean searchModeInputSequenceRadioButtonValue, String identifier,
			String proteinName, String proteinSequence) {
		super();
		this.dataSourceWidgetState = dataSourceWidgetState;
		this.searchModeProteinIdentifierRadioButtonValue = searchModeProteinIdentifierRadioButtonValue;
		this.searchModeInputSequenceRadioButtonValue = searchModeInputSequenceRadioButtonValue;
		this.identifier = identifier;
		this.proteinName = proteinName;
		this.proteinSequence = proteinSequence;
	}

	public DataSourceWidgetState getDataSourceWidgetState() {
		return dataSourceWidgetState;
	}

	public void setDataSourceWidgetState(
			DataSourceWidgetState dataSourceWidgetState) {
		this.dataSourceWidgetState = dataSourceWidgetState;
	}

	public boolean isSearchModeProteinIdentifierRadioButtonValue() {
		return searchModeProteinIdentifierRadioButtonValue;
	}

	public void setSearchModeProteinIdentifierRadioButtonValue(
			boolean searchModeProteinIdentifierRadioButtonValue) {
		this.searchModeProteinIdentifierRadioButtonValue = searchModeProteinIdentifierRadioButtonValue;
	}

	public boolean isSearchModeInputSequenceRadioButtonValue() {
		return searchModeInputSequenceRadioButtonValue;
	}

	public void setSearchModeInputSequenceRadioButtonValue(
			boolean searchModeInputSequenceRadioButtonValue) {
		this.searchModeInputSequenceRadioButtonValue = searchModeInputSequenceRadioButtonValue;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getProteinName() {
		return proteinName;
	}

	public void setProteinName(String proteinName) {
		this.proteinName = proteinName;
	}

	public String getProteinSequence() {
		return proteinSequence;
	}

	public void setProteinSequence(String proteinSequence) {
		this.proteinSequence = proteinSequence;
	}
}
