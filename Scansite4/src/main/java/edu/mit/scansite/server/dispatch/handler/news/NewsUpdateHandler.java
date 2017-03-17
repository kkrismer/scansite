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
import edu.mit.scansite.shared.dispatch.news.NewsRetrieverResult;
import edu.mit.scansite.shared.dispatch.news.NewsUpdateAction;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsUpdateHandler implements
		ActionHandler<NewsUpdateAction, NewsRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public NewsUpdateHandler(final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<NewsUpdateAction> getActionType() {
		return NewsUpdateAction.class;
	}

	@Override
	public NewsRetrieverResult execute(NewsUpdateAction action,
			ExecutionContext context) throws DispatchException {
		try {
			NewsDao newsDao = ServiceLocator.getDaoFactory().getNewsDao();
			newsDao.update(action.getNewsEntry());
			return new NewsRetrieverResult(newsDao.getAll(0));
		} catch (DataAccessException exc) {
			logger.error("Error updating news entry: " + exc.toString());
			throw new ActionException(exc);
		}
	}

	@Override
	public void rollback(NewsUpdateAction action, NewsRetrieverResult result,
			ExecutionContext context) throws DispatchException {
	}
}
