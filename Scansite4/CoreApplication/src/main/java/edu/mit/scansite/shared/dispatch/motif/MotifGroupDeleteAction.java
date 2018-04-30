package edu.mit.scansite.shared.dispatch.motif;

import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupDeleteAction implements
    Action<LightWeightMotifGroupRetrieverResult> {
  private int id;

  public MotifGroupDeleteAction() {
  }

  public MotifGroupDeleteAction(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}