package edu.mit.scansite.shared.dispatch.news;

import edu.mit.scansite.shared.transferobjects.NewsEntry;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsAddAction implements Action<NewsRetrieverResult> {
  private NewsEntry newsEntry;
  
  public NewsAddAction() {
  }
  
  public NewsAddAction(NewsEntry newsEntry) {
    this.newsEntry = newsEntry;
  }
  
  public NewsEntry getNewsEntry() {
    return newsEntry;
  }
}
