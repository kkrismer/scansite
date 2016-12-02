package edu.mit.scansite.client.ui.view.admin;

import java.util.List;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;

/**
 * @author Konstantin Krismer
 */
public abstract class MotifGroupMgmtPageView extends PageView {

	public interface Presenter {
		public void onAddButtonClicked(LightWeightMotifGroup motifGroup);

		public void onUpdateButtonClicked(LightWeightMotifGroup motifGroup);

		public void onDeleteButtonClicked(LightWeightMotifGroup motifGroup);
	}

	public abstract void setPresenter(Presenter presenter);

	public abstract void displayMotifGroupList(List<LightWeightMotifGroup> motifGroups);

	public abstract void resetInputFields();
}
