package edu.mit.scansite.client.dispatchqueue;

import java.util.ArrayList;
import java.util.HashMap;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Sequentially orders commands (service calls) by initiating all requests, then
 * invoking callbacks in order.
 * 
 * @author David Chandler
 */
public class DispatchQueue {
  private DispatchAsync dispatch;
  private ArrayList<CallbackCommand> commands = new ArrayList<CallbackCommand>();
  private HashMap<Action<Result>, Object> outcomes = new HashMap<Action<Result>, Object>();
  private int numPending;

  /**
   * Constructor requires a dispatcher 
   * 
   * @param dispatch The dispatcher that will be used to handle requests
   */
  public DispatchQueue(CachingDispatchAsync dispatch)
  {
    this.dispatch = dispatch;
  }

  /**
   * Convenience method to queue a command with syntax identical to execute()
   * 
   * @param action
   * @param callback
   * @return
   */
  public <A extends Action<R>, R extends Result> DispatchQueue add(final A action,
    final AsyncCallback<R> callback)
  {
    CallbackCommand cmd = new CallbackCommand(action, callback);
    return add(cmd);
  }

  /**
   * Add a CallbackCommand to the queue. Works by wrapping the previous
   * command's callback with a new callback that calls this command in it
   * onSuccess() handler
   * 
   * @param cmd
   */
  public DispatchQueue add(final CallbackCommand cmd)
  {
    // Block new entries once flushing has begun
    // if (started)
    if (numPending > 0)
      throw new UnsupportedOperationException("Queue is already running");
    // Wrap with new callback that decrements the counter and saves the
    // outcome
    final AsyncCallback<Result> origCallback = cmd.getCallback();
    AsyncCallback<Result> wrapper = new AsyncCallback<Result>()
    {
      @Override
      public void onFailure(Throwable caught)
      {
        if (numPending > 0)
        {
          // Pass through to original and call next command
          saveOutcome(cmd.getAction(), caught);
          if (--numPending == 0)
            finish();
        }
        else
        {
          origCallback.onFailure(caught);
        }
      }

      @Override
      public void onSuccess(Result result)
      {
        if (numPending > 0)
        {
          // Pass through to original and call next command
          saveOutcome(cmd.getAction(), result);
          if (--numPending == 0)
            finish();
        }
        else
        {
          origCallback.onSuccess(result);
        }
      }
    };
    cmd.setCallback(wrapper);
    commands.add(cmd);
    return this;
  }

  /**
   * Execute queued commands and clear the queue upon completion.
   * queueCommand() has already wrapped each callback with a chaining
   * callback, so this method just has to call the first command.
   */
  public void flush()
  {
    // Prohibit new entries once started
    this.numPending = commands.size();
    // This starts them all asynchronously, and callback
    // wrappers save the outcomes
    for (CallbackCommand cmd : commands)
    {
      dispatch.execute(cmd.getAction(), cmd.getCallback());
    }
  }

  private void saveOutcome(Action<Result> action, Object outcome)
  {
    outcomes.put(action, outcome);
  }

  private void finish()
  {
    GWT.log("Entering DispatchQueue.finish()", null);
    // Call each callback in sequence
    for (CallbackCommand cmd : commands)
    {
      Action<Result> action = cmd.getAction();
      AsyncCallback<Result> callback = cmd.getCallback();
      Object outcome = outcomes.get(action);
      if (outcome instanceof Result)
      {
        callback.onSuccess((Result) outcome);
      }
      else
      {
        callback.onFailure((Throwable) outcome);
      }
    }
  }
}
