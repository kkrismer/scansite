package edu.mit.scansite.client.ui.view.main;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.NoSelectionModel;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.news.NewsCell;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.transferobjects.NewsEntry;

/**
 * @author Konstantin Krismer
 */
public class NewsPageView extends PageView {
	interface NewsUiBinder extends UiBinder<Widget, NewsPageView> {
	}

	private static NewsUiBinder uiBinder = GWT.create(NewsUiBinder.class);

	@UiField(provided = true)
	final CellList<NewsEntry> newsEntries;

	public NewsPageView() {
		newsEntries = new CellList<NewsEntry>(new NewsCell());
		newsEntries.setSelectionModel(new NoSelectionModel<NewsEntry>(),
				DefaultSelectionEventManager
						.<NewsEntry> createWhitelistManager());
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getPageTitle() {
		return "News";
	}

	@Override
	public boolean isMajorNavigationPage() {
		return true;
	}

	@Override
	public String getMajorPageId() {
		return getPageId();
	}

	@Override
	public String getPageId() {
		return NavigationEvent.PageId.NEWS.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.NEWS;
	}

	public void setNewsEntries(List<NewsEntry> entries) {
		newsEntries.setRowData(entries);
	}
}
