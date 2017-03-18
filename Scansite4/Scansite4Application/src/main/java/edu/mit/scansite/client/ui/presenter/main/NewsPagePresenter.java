package edu.mit.scansite.client.ui.presenter.main;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.main.NewsPageView;
import edu.mit.scansite.shared.dispatch.news.NewsRetrieverAction;
import edu.mit.scansite.shared.dispatch.news.NewsRetrieverResult;
import edu.mit.scansite.shared.transferobjects.NewsEntry;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsPagePresenter extends Presenter {
	private final NewsPageView view;

	private List<NewsEntry> news = new ArrayList<NewsEntry>();

	public NewsPagePresenter(NewsPageView view) {
		this.view = view;
	}

	private void initNewsEntries() {
		dispatch.execute(new NewsRetrieverAction(),
				new AsyncCallback<NewsRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										"Server-side error", this.getClass()
												.toString(), caught));
					}

					@Override
					public void onSuccess(NewsRetrieverResult result) {
						news = result.getNews();
						view.setNewsEntries(news);
						view.hideMessage();
					}
				});
	}

	@Override
	public void bind() {

	}

	@Override
	public void go(HasWidgets container) {
		initNewsEntries();
		container.clear();
		container.add(view.asWidget());
	}
}
