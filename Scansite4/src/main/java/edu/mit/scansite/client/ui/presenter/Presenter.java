package edu.mit.scansite.client.ui.presenter;

import java.util.HashMap;
import java.util.Map;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.HistoryToken;
import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.shared.dispatch.history.StoreHistoryStateAction;
import edu.mit.scansite.shared.dispatch.history.StoreHistoryStateResult;
import edu.mit.scansite.shared.transferobjects.states.State;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class Presenter {
	protected final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	public abstract void bind();

	public abstract void go(final HasWidgets container);

	protected void fireHistoryEvent(State state, final String pageId) {
		dispatch.execute(new StoreHistoryStateAction(state),
				new AsyncCallback<StoreHistoryStateResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										"History-Event error", this.getClass()
												.toString(), caught));
					}

					@Override
					public void onSuccess(StoreHistoryStateResult result) {
						if (result.isSuccess()) {
							Map<String, String> sessionId = new HashMap<>();
							sessionId.put("s", result.getSessionId());
							History.newItem(HistoryToken.buildToken(null,
									pageId, sessionId), false);
						} else {
						}
					}
				});
	}
}
