package edu.mit.scansite.shared.transferobjects.states;

/**
 * @author Konstantin Krismer
 */
public class ScanOrthologsPageState extends State {
	private ChooseOrthologyProteinWidgetState chooseOrthologyProteinWidgetState;
	private boolean searchMethodSequencePatternRadioButtonValue;
	private boolean searchMethodMotifGroupsRadioButtonValue;
	private SeqPatMethodWidgetState seqPatMethodWidgetState;
	private int motifGroupListBoxSelectedIndex;
	private int sitePosition;
	private StringencyLevelWidgetState stringencyLevelWidgetState;
	private int alignmentRadius;

	public ScanOrthologsPageState() {

	}

	public ScanOrthologsPageState(
			ChooseOrthologyProteinWidgetState chooseOrthologyProteinWidgetState,
			boolean searchMethodSequencePatternRadioButtonValue,
			boolean searchMethodMotifGroupsRadioButtonValue,
			SeqPatMethodWidgetState seqPatMethodWidgetState,
			int motifGroupListBoxSelectedIndex, int sitePosition,
			StringencyLevelWidgetState stringencyLevelWidgetState,
			int alignmentRadius) {
		super();
		this.chooseOrthologyProteinWidgetState = chooseOrthologyProteinWidgetState;
		this.searchMethodSequencePatternRadioButtonValue = searchMethodSequencePatternRadioButtonValue;
		this.searchMethodMotifGroupsRadioButtonValue = searchMethodMotifGroupsRadioButtonValue;
		this.seqPatMethodWidgetState = seqPatMethodWidgetState;
		this.motifGroupListBoxSelectedIndex = motifGroupListBoxSelectedIndex;
		this.sitePosition = sitePosition;
		this.stringencyLevelWidgetState = stringencyLevelWidgetState;
		this.alignmentRadius = alignmentRadius;
	}

	public ChooseOrthologyProteinWidgetState getChooseOrthologyProteinWidgetState() {
		return chooseOrthologyProteinWidgetState;
	}

	public void setChooseOrthologyProteinWidgetState(
			ChooseOrthologyProteinWidgetState chooseOrthologyProteinWidgetState) {
		this.chooseOrthologyProteinWidgetState = chooseOrthologyProteinWidgetState;
	}

	public boolean isSearchMethodSequencePatternRadioButtonValue() {
		return searchMethodSequencePatternRadioButtonValue;
	}

	public void setSearchMethodSequencePatternRadioButtonValue(
			boolean searchMethodSequencePatternRadioButtonValue) {
		this.searchMethodSequencePatternRadioButtonValue = searchMethodSequencePatternRadioButtonValue;
	}

	public boolean isSearchMethodMotifGroupsRadioButtonValue() {
		return searchMethodMotifGroupsRadioButtonValue;
	}

	public void setSearchMethodMotifGroupsRadioButtonValue(
			boolean searchMethodMotifGroupsRadioButtonValue) {
		this.searchMethodMotifGroupsRadioButtonValue = searchMethodMotifGroupsRadioButtonValue;
	}

	public SeqPatMethodWidgetState getSeqPatMethodWidgetState() {
		return seqPatMethodWidgetState;
	}

	public void setSeqPatMethodWidgetState(
			SeqPatMethodWidgetState seqPatMethodWidgetState) {
		this.seqPatMethodWidgetState = seqPatMethodWidgetState;
	}

	public int getMotifGroupListBoxSelectedIndex() {
		return motifGroupListBoxSelectedIndex;
	}

	public void setMotifGroupListBoxSelectedIndex(
			int motifGroupListBoxSelectedIndex) {
		this.motifGroupListBoxSelectedIndex = motifGroupListBoxSelectedIndex;
	}

	public int getSitePosition() {
		return sitePosition;
	}

	public void setSitePosition(int sitePosition) {
		this.sitePosition = sitePosition;
	}

	public StringencyLevelWidgetState getStringencyLevelWidgetState() {
		return stringencyLevelWidgetState;
	}

	public void setStringencyLevelWidgetState(
			StringencyLevelWidgetState stringencyLevelWidgetState) {
		this.stringencyLevelWidgetState = stringencyLevelWidgetState;
	}

	public int getAlignmentRadius() {
		return alignmentRadius;
	}

	public void setAlignmentRadius(
			int alignmentRadius) {
		this.alignmentRadius = alignmentRadius;
	}
}
