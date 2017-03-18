package edu.mit.scansite.shared.transferobjects.states;

/**
 * @author Konstantin Krismer
 */
public class PredictLocalizationPageState extends State {
	private DataSourceWidgetState dataSourceWidgetState;
	private ChooseProteinWidgetState chooseProteinWidgetState;

	public PredictLocalizationPageState() {

	}

	public PredictLocalizationPageState(
			DataSourceWidgetState dataSourceWidgetState,
			ChooseProteinWidgetState chooseProteinWidgetState) {
		super();
		this.dataSourceWidgetState = dataSourceWidgetState;
		this.chooseProteinWidgetState = chooseProteinWidgetState;
	}

	public DataSourceWidgetState getDataSourceWidgetState() {
		return dataSourceWidgetState;
	}

	public void setDataSourceWidgetState(
			DataSourceWidgetState dataSourceWidgetState) {
		this.dataSourceWidgetState = dataSourceWidgetState;
	}

	public ChooseProteinWidgetState getChooseProteinWidgetState() {
		return chooseProteinWidgetState;
	}

	public void setChooseProteinWidgetState(
			ChooseProteinWidgetState chooseProteinWidgetState) {
		this.chooseProteinWidgetState = chooseProteinWidgetState;
	}
}
