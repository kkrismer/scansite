package edu.mit.scansite.shared.transferobjects.states;

/**
 * @author Konstantin Krismer
 */
public class ScanProteinPageState extends State {
	private ChooseProteinWidgetState chooseProteinWidgetState;
	private boolean motifSpaceAllRadioButtonValue;
	private boolean motifSpaceSelectedRadioButtonValue;
	private boolean motifSpaceUserDefinedRadioButton;
	private MotifClassWidgetState ChooseAllMotifsWidgetState;
	private ChooseSelectedMotifsWidgetState chooseSelectedMotifsWidgetState;
	private ChooseUserFileMotifWidgetState chooseUserFileMotifWidgetState;
	private StringencyLevelWidgetState stringencyLevelWidgetState;
	private boolean showDomains;
	boolean previouslyMappedSitesOnly;
	private HistogramSelectionWidgetState histogramSelectionWidgetState;
	private DataSourceWidgetState localizationDataSourceWidgetState;

	public ScanProteinPageState() {

	}

	public ScanProteinPageState(
			ChooseProteinWidgetState chooseProteinWidgetState,
			boolean motifSpaceAllRadioButtonValue,
			boolean motifSpaceSelectedRadioButtonValue,
			boolean motifSpaceUserDefinedRadioButton,
			MotifClassWidgetState chooseAllMotifsWidgetState,
			ChooseSelectedMotifsWidgetState chooseSelectedMotifsWidgetState,
			ChooseUserFileMotifWidgetState chooseUserFileMotifWidgetState,
			StringencyLevelWidgetState stringencyLevelWidgetState,
			boolean showDomains,
			boolean previouslyMappedSitesOnly,
			HistogramSelectionWidgetState histogramSelectionWidgetState,
			DataSourceWidgetState localizationDataSourceWidgetState) {
		super();
		this.chooseProteinWidgetState = chooseProteinWidgetState;
		this.motifSpaceAllRadioButtonValue = motifSpaceAllRadioButtonValue;
		this.motifSpaceSelectedRadioButtonValue = motifSpaceSelectedRadioButtonValue;
		this.motifSpaceUserDefinedRadioButton = motifSpaceUserDefinedRadioButton;
		ChooseAllMotifsWidgetState = chooseAllMotifsWidgetState;
		this.chooseSelectedMotifsWidgetState = chooseSelectedMotifsWidgetState;
		this.chooseUserFileMotifWidgetState = chooseUserFileMotifWidgetState;
		this.stringencyLevelWidgetState = stringencyLevelWidgetState;
		this.showDomains = showDomains;
		this.previouslyMappedSitesOnly = previouslyMappedSitesOnly;
		this.histogramSelectionWidgetState = histogramSelectionWidgetState;
		this.localizationDataSourceWidgetState = localizationDataSourceWidgetState;
	}

	public ChooseProteinWidgetState getChooseProteinWidgetState() {
		return chooseProteinWidgetState;
	}

	public void setChooseProteinWidgetState(
			ChooseProteinWidgetState chooseProteinWidgetState) {
		this.chooseProteinWidgetState = chooseProteinWidgetState;
	}

	public boolean isMotifSpaceAllRadioButtonValue() {
		return motifSpaceAllRadioButtonValue;
	}

	public void setMotifSpaceAllRadioButtonValue(
			boolean motifSpaceAllRadioButtonValue) {
		this.motifSpaceAllRadioButtonValue = motifSpaceAllRadioButtonValue;
	}

	public boolean isMotifSpaceSelectedRadioButtonValue() {
		return motifSpaceSelectedRadioButtonValue;
	}

	public void setMotifSpaceSelectedRadioButtonValue(
			boolean motifSpaceSelectedRadioButtonValue) {
		this.motifSpaceSelectedRadioButtonValue = motifSpaceSelectedRadioButtonValue;
	}

	public boolean isMotifSpaceUserDefinedRadioButton() {
		return motifSpaceUserDefinedRadioButton;
	}

	public void setMotifSpaceUserDefinedRadioButton(
			boolean motifSpaceUserDefinedRadioButton) {
		this.motifSpaceUserDefinedRadioButton = motifSpaceUserDefinedRadioButton;
	}

	public MotifClassWidgetState getChooseAllMotifsWidgetState() {
		return ChooseAllMotifsWidgetState;
	}

	public void setChooseAllMotifsWidgetState(
			MotifClassWidgetState chooseAllMotifsWidgetState) {
		ChooseAllMotifsWidgetState = chooseAllMotifsWidgetState;
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

	public StringencyLevelWidgetState getStringencyLevelWidgetState() {
		return stringencyLevelWidgetState;
	}

	public void setStringencyLevelWidgetState(
			StringencyLevelWidgetState stringencyLevelWidgetState) {
		this.stringencyLevelWidgetState = stringencyLevelWidgetState;
	}

	public boolean isShowDomains() {
		return showDomains;
	}

	public void setShowDomains(boolean showDomains) {
		this.showDomains = showDomains;
	}

	public boolean isPreviouslyMappedSitesOnly() {
		return previouslyMappedSitesOnly;
	}

	public void setPreviouslyMappedSitesOnly(boolean previouslyMappedSitesOnly) {
		this.previouslyMappedSitesOnly = previouslyMappedSitesOnly;
	}

	public HistogramSelectionWidgetState getHistogramSelectionWidgetState() {
		return histogramSelectionWidgetState;
	}

	public void setHistogramSelectionWidgetState(
			HistogramSelectionWidgetState histogramSelectionWidgetState) {
		this.histogramSelectionWidgetState = histogramSelectionWidgetState;
	}

	public DataSourceWidgetState getLocalizationDataSourceWidgetState() {
		return localizationDataSourceWidgetState;
	}

	public void setLocalizationDataSourceWidgetState(
			DataSourceWidgetState localizationDataSourceWidgetState) {
		this.localizationDataSourceWidgetState = localizationDataSourceWidgetState;
	}
}
