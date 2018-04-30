package edu.mit.scansite.client.ui.presenter.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.admin.NewsMgmtPageView;
import edu.mit.scansite.shared.dispatch.news.NewsAddAction;
import edu.mit.scansite.shared.dispatch.news.NewsDeleteAction;
import edu.mit.scansite.shared.dispatch.news.NewsRetrieverAction;
import edu.mit.scansite.shared.dispatch.news.NewsRetrieverResult;
import edu.mit.scansite.shared.dispatch.news.NewsUpdateAction;
import edu.mit.scansite.shared.transferobjects.NewsEntry;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsMgmtPagePresenter extends Presenter implements
		NewsMgmtPageView.Presenter {
	private NewsMgmtPageView view;

	public NewsMgmtPagePresenter(NewsMgmtPageView view) {
		this.view = view;
	}

	@Override
	public void bind() {
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		retrieveNews();
	}

	private void retrieveNews() {
		dispatch.execute(new NewsRetrieverAction(),
				new AsyncCallback<NewsRetrieverResult>() {
					public void onFailure(Throwable caught) {
						EventBus.instance()
								.fireEvent(
										new MessageEvent(
												MessageEventPriority.ERROR,
												"Unable to retrieve news from database",
												this.getClass().toString(),
												caught));
					}

					public void onSuccess(NewsRetrieverResult result) {
						view.displayNewsList(result.getNews());
					}
				});
	}

	@Override
	public void onAddButtonClicked(NewsEntry entry) {
		dispatch.execute(new NewsAddAction(entry),
				new AsyncCallback<NewsRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										caught.getMessage(), this.getClass()
												.toString(), caught));
					}

					@Override
					public void onSuccess(NewsRetrieverResult result) {
						view.displayNewsList(result.getNews());
						view.resetInputFields();
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.INFO,
										"News entry successfully added", this
												.getClass().toString(), null));
					}
				});
	}

	@Override
	public void onUpdateButtonClicked(NewsEntry entry) {
		dispatch.execute(new NewsUpdateAction(entry),
				new AsyncCallback<NewsRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										caught.getMessage(), this.getClass()
												.toString(), caught));
					}

					@Override
					public void onSuccess(NewsRetrieverResult result) {
						view.displayNewsList(result.getNews());
						view.resetInputFields();
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.INFO,
										"News entry successfully updated", this
												.getClass().toString(), null));
					}
				});
	}

	@Override
	public void onDeleteButtonClicked(NewsEntry entry) {
		dispatch.execute(new NewsDeleteAction(entry.getId()),
				new AsyncCallback<NewsRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										caught.getMessage(), this.getClass()
												.toString(), caught));
					}

					@Override
					public void onSuccess(NewsRetrieverResult result) {
						view.displayNewsList(result.getNews());
						view.resetInputFields();
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.INFO,
										"News entry successfully deleted", this
												.getClass().toString(), null));
					}
				});
	}
}
