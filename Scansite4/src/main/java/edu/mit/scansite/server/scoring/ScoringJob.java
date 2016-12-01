package edu.mit.scansite.server.scoring;

import java.util.Observable;

/**
 * @author Tobieh
 */
public abstract class ScoringJob extends Observable implements Runnable {

  public ScoringJob() {
  }

  @Override
  public final void run() {
    doRun();
    setChanged();
    notifyObservers();
  }

  /**
   * Runs the job.
   */
  protected abstract void doRun();
}
