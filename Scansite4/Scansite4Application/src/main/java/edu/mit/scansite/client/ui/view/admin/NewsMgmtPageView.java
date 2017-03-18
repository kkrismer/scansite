package edu.mit.scansite.client.ui.view.admin;

import java.util.List;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.shared.transferobjects.NewsEntry;

/**
 * @author Konstantin Krismer
 */
public abstract class NewsMgmtPageView extends PageView {

	public interface Presenter {
		public void onAddButtonClicked(NewsEntry entry);

		public void onUpdateButtonClicked(NewsEntry entry);

		public void onDeleteButtonClicked(NewsEntry entry);
	}

	public abstract void setPresenter(Presenter presenter);
	
	public abstract void displayNewsList(List<NewsEntry> news);
	
	public abstract void resetInputFields();
}
