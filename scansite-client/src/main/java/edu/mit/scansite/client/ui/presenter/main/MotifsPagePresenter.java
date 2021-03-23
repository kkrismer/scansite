package edu.mit.scansite.client.ui.presenter.main;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.main.MotifsPageView;
import edu.mit.scansite.shared.dispatch.datasource.DataSourceSizesRetrieverAction;
import edu.mit.scansite.shared.dispatch.datasource.DataSourceSizesRetrieverResult;
import edu.mit.scansite.shared.event.EventBus;
import edu.mit.scansite.shared.event.MessageEvent;
import edu.mit.scansite.shared.event.MessageEvent.MessageEventPriority;

/**
 * @author Konstantin Krismer
 */
public class MotifsPagePresenter extends Presenter {
	private final MotifsPageView view;

	public MotifsPagePresenter(MotifsPageView view) {
		this.view = view;
	}

	private void initDataSourceInfo() {
		dispatch.execute(new DataSourceSizesRetrieverAction(),
				new AsyncCallback<DataSourceSizesRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										"Server-side error", this.getClass()
												.toString(), caught));
					}

					@Override
					public void onSuccess(DataSourceSizesRetrieverResult result) {
						view.hideMessage();
						view.setDataSourceInfo(result.getDataSourceSizes());
					}
				});
	}

	@Override
	public void bind() {
	}

	@Override
	public void go(HasWidgets container) {
		initDataSourceInfo();
		container.clear();
		container.add(view.asWidget());
	}
}