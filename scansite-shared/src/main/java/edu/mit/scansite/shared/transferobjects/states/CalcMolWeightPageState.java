package edu.mit.scansite.shared.transferobjects.states;

/**
 * @author Konstantin Krismer
 */
public class CalcMolWeightPageState extends State {
	private ChooseProteinWidgetState chooseProteinWidgetState;
	private int maxNumberPhosphorylationSites;

	public CalcMolWeightPageState() {

	}

	public CalcMolWeightPageState(
			ChooseProteinWidgetState chooseProteinWidgetState,
			int maxNumberPhosphorylationSites) {
		super();
		this.chooseProteinWidgetState = chooseProteinWidgetState;
		this.maxNumberPhosphorylationSites = maxNumberPhosphorylationSites;
	}

	public ChooseProteinWidgetState getChooseProteinWidgetState() {
		return chooseProteinWidgetState;
	}

	public void setChooseProteinWidgetState(
			ChooseProteinWidgetState chooseProteinWidgetState) {
		this.chooseProteinWidgetState = chooseProteinWidgetState;
	}

	public int getMaxNumberPhosphorylationSites() {
		return maxNumberPhosphorylationSites;
	}

	public void setMaxNumberPhosphorylationSites(
			int maxNumberPhosphorylationSites) {
		this.maxNumberPhosphorylationSites = maxNumberPhosphorylationSites;
	}
}
