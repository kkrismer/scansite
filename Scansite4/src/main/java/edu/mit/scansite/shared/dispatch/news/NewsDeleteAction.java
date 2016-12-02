package edu.mit.scansite.shared.dispatch.news;

import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsDeleteAction implements Action<NewsRetrieverResult> {
  private int id;
  
  public NewsDeleteAction() {
  }
  
  public NewsDeleteAction(int id) {
    this.id = id;
  }
  
  public int getId() {
    return id;
  }
}
