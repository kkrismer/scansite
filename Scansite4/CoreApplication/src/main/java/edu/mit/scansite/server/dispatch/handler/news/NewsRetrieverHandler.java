package edu.mit.scansite.server.dispatch.handler.news;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.news.NewsRetrieverAction;
import edu.mit.scansite.shared.dispatch.news.NewsRetrieverResult;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsRetrieverHandler implements ActionHandler<NewsRetrieverAction, NewsRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public Class<NewsRetrieverAction> getActionType() {
		return NewsRetrieverAction.class;
	}

	public synchronized NewsRetrieverResult execute(NewsRetrieverAction action, ExecutionContext context)
			throws ActionException {
		try {
			return new NewsRetrieverResult(ServiceLocator.getDaoFactory().getNewsDao().getAll(0));
		} catch (DataAccessException e) {
			logger.error("Error getting news entries: " + e.toString());
			throw new ActionException(e);
		}
	}

	public synchronized void rollback(NewsRetrieverAction action, NewsRetrieverResult result, ExecutionContext context)
			throws ActionException {
	}
}
