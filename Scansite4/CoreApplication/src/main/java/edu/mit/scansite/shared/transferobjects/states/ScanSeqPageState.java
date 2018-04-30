package edu.mit.scansite.shared.transferobjects.states;

/**
 * @author Konstantin Krismer
 */
public class ScanSeqPageState extends State {
	private boolean searchMethodSeqPatternRadioButtonValue;
	private boolean searchMethodRegExRadioButtonValue;
	private SeqPatMethodWidgetState seqPatMethodWidgetState;
	private RegExMethodWidgetState regExMethodWidgetState;
	private DataSourceWidgetState dataSourceWidgetState;
	private DbRestrictionWidgetState dbRestrictionWidgetState;

	public ScanSeqPageState() {

	}

	public ScanSeqPageState(boolean searchMethodSeqPatternRadioButtonValue,
			boolean searchMethodRegExRadioButtonValue,
			SeqPatMethodWidgetState seqPatMethodWidgetState,
			RegExMethodWidgetState regExMethodWidgetState,
			DataSourceWidgetState dataSourceWidgetState,
			DbRestrictionWidgetState dbRestrictionWidgetState) {
		super();
		this.searchMethodSeqPatternRadioButtonValue = searchMethodSeqPatternRadioButtonValue;
		this.searchMethodRegExRadioButtonValue = searchMethodRegExRadioButtonValue;
		this.seqPatMethodWidgetState = seqPatMethodWidgetState;
		this.regExMethodWidgetState = regExMethodWidgetState;
		this.dataSourceWidgetState = dataSourceWidgetState;
		this.dbRestrictionWidgetState = dbRestrictionWidgetState;
	}

	public boolean isSearchMethodSeqPatternRadioButtonValue() {
		return searchMethodSeqPatternRadioButtonValue;
	}

	public void setSearchMethodSeqPatternRadioButtonValue(
			boolean searchMethodSeqPatternRadioButtonValue) {
		this.searchMethodSeqPatternRadioButtonValue = searchMethodSeqPatternRadioButtonValue;
	}

	public boolean isSearchMethodRegExRadioButtonValue() {
		return searchMethodRegExRadioButtonValue;
	}

	public void setSearchMethodRegExRadioButtonValue(
			boolean searchMethodRegExRadioButtonValue) {
		this.searchMethodRegExRadioButtonValue = searchMethodRegExRadioButtonValue;
	}

	public SeqPatMethodWidgetState getSeqPatMethodWidgetState() {
		return seqPatMethodWidgetState;
	}

	public void setSeqPatMethodWidgetState(
			SeqPatMethodWidgetState seqPatMethodWidgetState) {
		this.seqPatMethodWidgetState = seqPatMethodWidgetState;
	}

	public RegExMethodWidgetState getRegExMethodWidgetState() {
		return regExMethodWidgetState;
	}

	public void setRegExMethodWidgetState(
			RegExMethodWidgetState regExMethodWidgetState) {
		this.regExMethodWidgetState = regExMethodWidgetState;
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
}
