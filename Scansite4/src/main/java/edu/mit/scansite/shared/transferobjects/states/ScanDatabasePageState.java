package edu.mit.scansite.shared.transferobjects.states;

/**
 * @author Konstantin Krismer
 */
public class ScanDatabasePageState extends State {
	private boolean searchMethodDatabaseMotifRadioButtonValue;
	private boolean searchMethodDatabaseMotifsRadioButtonValue;
	private boolean searchMethodUserDefinedMotifsRadioButton;
	private boolean searchMethodQuickMotifsRadioButton;
	private ChooseSelectedMotifsWidgetState chooseSelectedMotifWidgetState;
	private ChooseSelectedMotifsWidgetState chooseSelectedMotifsWidgetState;
	private ChooseUserFileMotifWidgetState chooseUserFileMotifWidgetState;
	private ChooseQuickMotifWidgetState chooseQuickMotifWidgetState;
	private DataSourceWidgetState dataSourceWidgetState;
	private DbRestrictionWidgetState dbRestrictionWidgetState;
	private int outputListSizeListBoxSelectedIndex;

	public ScanDatabasePageState() {

	}

	public ScanDatabasePageState(
			boolean searchMethodDatabaseMotifRadioButtonValue,
			boolean searchMethodDatabaseMotifsRadioButtonValue,
			boolean searchMethodUserDefinedMotifsRadioButton,
			boolean searchMethodQuickMotifsRadioButton,
			ChooseSelectedMotifsWidgetState chooseSelectedMotifWidgetState,
			ChooseSelectedMotifsWidgetState chooseSelectedMotifsWidgetState,
			ChooseUserFileMotifWidgetState chooseUserFileMotifWidgetState,
			ChooseQuickMotifWidgetState chooseQuickMotifWidgetState,
			DataSourceWidgetState dataSourceWidgetState,
			DbRestrictionWidgetState dbRestrictionWidgetState,
			int outputListSizeListBoxSelectedIndex) {
		super();
		this.searchMethodDatabaseMotifRadioButtonValue = searchMethodDatabaseMotifRadioButtonValue;
		this.searchMethodDatabaseMotifsRadioButtonValue = searchMethodDatabaseMotifsRadioButtonValue;
		this.searchMethodUserDefinedMotifsRadioButton = searchMethodUserDefinedMotifsRadioButton;
		this.searchMethodQuickMotifsRadioButton = searchMethodQuickMotifsRadioButton;
		this.chooseSelectedMotifWidgetState = chooseSelectedMotifWidgetState;
		this.chooseSelectedMotifsWidgetState = chooseSelectedMotifsWidgetState;
		this.chooseUserFileMotifWidgetState = chooseUserFileMotifWidgetState;
		this.chooseQuickMotifWidgetState = chooseQuickMotifWidgetState;
		this.dataSourceWidgetState = dataSourceWidgetState;
		this.dbRestrictionWidgetState = dbRestrictionWidgetState;
		this.outputListSizeListBoxSelectedIndex = outputListSizeListBoxSelectedIndex;
	}

	public boolean isSearchMethodDatabaseMotifRadioButtonValue() {
		return searchMethodDatabaseMotifRadioButtonValue;
	}

	public void setSearchMethodDatabaseMotifRadioButtonValue(
			boolean searchMethodDatabaseMotifRadioButtonValue) {
		this.searchMethodDatabaseMotifRadioButtonValue = searchMethodDatabaseMotifRadioButtonValue;
	}

	public boolean isSearchMethodDatabaseMotifsRadioButtonValue() {
		return searchMethodDatabaseMotifsRadioButtonValue;
	}

	public void setSearchMethodDatabaseMotifsRadioButtonValue(
			boolean searchMethodDatabaseMotifsRadioButtonValue) {
		this.searchMethodDatabaseMotifsRadioButtonValue = searchMethodDatabaseMotifsRadioButtonValue;
	}

	public boolean isSearchMethodUserDefinedMotifsRadioButton() {
		return searchMethodUserDefinedMotifsRadioButton;
	}

	public void setSearchMethodUserDefinedMotifsRadioButton(
			boolean searchMethodUserDefinedMotifsRadioButton) {
		this.searchMethodUserDefinedMotifsRadioButton = searchMethodUserDefinedMotifsRadioButton;
	}

	public boolean isSearchMethodQuickMotifsRadioButton() {
		return searchMethodQuickMotifsRadioButton;
	}

	public void setSearchMethodQuickMotifsRadioButton(
			boolean searchMethodQuickMotifsRadioButton) {
		this.searchMethodQuickMotifsRadioButton = searchMethodQuickMotifsRadioButton;
	}

	public ChooseSelectedMotifsWidgetState getChooseSelectedMotifWidgetState() {
		return chooseSelectedMotifWidgetState;
	}

	public void setChooseSelectedMotifWidgetState(
			ChooseSelectedMotifsWidgetState chooseSelectedMotifWidgetState) {
		this.chooseSelectedMotifWidgetState = chooseSelectedMotifWidgetState;
	}

	public ChooseSelectedMotifsWidgetState getChooseSelectedMotifsWidgetState() {
		return chooseSelectedMotifsWidgetState;
	}

	public void setChooseSelectedMotifsWidgetState(
			ChooseSelectedMotifsWidgetState chooseSelectedMotifsWidgetState) {
		this.chooseSelectedMotifsWidgetState = chooseSelectedMotifsWidgetState;
	}

	public ChooseUserFileMotifWidgetState getChooseUserFileMotifWidgetState() {
		return chooseUserFileMotifWidgetState;
	}

	public void setChooseUserFileMotifWidgetState(
			ChooseUserFileMotifWidgetState chooseUserFileMotifWidgetState) {
		this.chooseUserFileMotifWidgetState = chooseUserFileMotifWidgetState;
	}

	public ChooseQuickMotifWidgetState getChooseQuickMotifWidgetState() {
		return chooseQuickMotifWidgetState;
	}

	public void setChooseQuickMotifWidgetState(
			ChooseQuickMotifWidgetState chooseQuickMotifWidgetState) {
		this.chooseQuickMotifWidgetState = chooseQuickMotifWidgetState;
	}

	public DataSourceWidgetState getDataSourceWidgetState() {
		return dataSourceWidgetState;
	}

	public void setDataSourceWidgetState(
			DataSourceWidgetState dataSourceWidgetState) {
		this.dataSourceWidgetState = dataSourceWidgetState;
	}

	public DbRestrictionWidgetState getDbRestrictionWidgetState() {
		return dbRestrictionWidgetState;
	}

	public void setDbRestrictionWidgetState(
			DbRestrictionWidgetState dbRestrictionWidgetState) {
		this.dbRestrictionWidgetState = dbRestrictionWidgetState;
	}

	public int getOutputListSizeListBoxSelectedIndex() {
		return outputListSizeListBoxSelectedIndex;
	}

	public void setOutputListSizeListBoxSelectedIndex(
			int outputListSizeListBoxSelectedIndex) {
		this.outputListSizeListBoxSelectedIndex = outputListSizeListBoxSelectedIndex;
	}
}
