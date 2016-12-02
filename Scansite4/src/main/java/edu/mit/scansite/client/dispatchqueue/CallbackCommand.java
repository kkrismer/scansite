package edu.mit.scansite.client.dispatchqueue;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Wraps an action and callback so that they can be added to a DispatchQueue
 * 
 * @author David Chandler
 */
public class CallbackCommand {
  private Action<Result> action;
  private AsyncCallback<Result> callback;

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public CallbackCommand(final Action action, final AsyncCallback callback) {
    this.action = action;
    this.callback = callback;
  }

  public Action<Result> getAction() {
    return action;
  }

  public AsyncCallback<Result> getCallback() {
    return callback;
  }

  public void setCallback(AsyncCallback<Result> callback) {
    this.callback = callback;
  }
}
