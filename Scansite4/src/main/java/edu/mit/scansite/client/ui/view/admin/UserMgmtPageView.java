package edu.mit.scansite.client.ui.view.admin;

import java.util.List;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Konstantin Krismer
 */
public abstract class UserMgmtPageView extends PageView {

	public interface Presenter {
		public void onAddButtonClicked(User entry);

		public void onUpdateButtonClicked(User entry);

		public void onDeleteButtonClicked(User entry);
	}

	public abstract void setPresenter(Presenter presenter);
	
	public abstract void displayUserList(List<User> news);
	
	public abstract void resetInputFields();
}
