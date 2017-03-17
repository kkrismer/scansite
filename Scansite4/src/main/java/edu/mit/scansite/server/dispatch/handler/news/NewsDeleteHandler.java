package edu.mit.scansite.server.dispatch.handler.news;

import javax.servlet.ServletContext;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.NewsDao;
import edu.mit.scansite.server.dispatch.BootstrapListener;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.news.NewsDeleteAction;
import edu.mit.scansite.shared.dispatch.news.NewsRetrieverResult;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsDeleteHandler implements
		ActionHandler<NewsDeleteAction, NewsRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public NewsDeleteHandler(final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<NewsDeleteAction> getActionType() {
		return NewsDeleteAction.class;
	}

	@Override
	public NewsRetrieverResult execute(NewsDeleteAction action,
			ExecutionContext context) throws DispatchException {
		try {
			NewsDao newsDao = ServiceLocator.getDaoFactory().getNewsDao();
			newsDao.delete(action.getId());
			return new NewsRetrieverResult(newsDao.getAll(0));
		} catch (DataAccessException exc) {
			logger.error("Error deleting news entry: " + exc.toString());
			throw new ActionException(exc);
		}
	}

	@Override
	public void rollback(NewsDeleteAction action, NewsRetrieverResult result,
			ExecutionContext context) throws DispatchException {
	}
}
