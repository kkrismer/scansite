package edu.mit.scansite.shared.dispatch.news;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;
import edu.mit.scansite.shared.transferobjects.NewsEntry;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsRetrieverResult implements Result {
  private List<NewsEntry> news;

  /** For serialization only. */
  NewsRetrieverResult() {
  }

  public NewsRetrieverResult(List<NewsEntry> news) {
    this.news = news;
  }

  public List<NewsEntry> getNews() {
    return news;
  }
}
