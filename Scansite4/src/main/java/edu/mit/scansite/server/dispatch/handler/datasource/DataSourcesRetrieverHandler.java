package edu.mit.scansite.server.dispatch.handler.datasource;

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
import edu.mit.scansite.server.dispatch.BootstrapListener;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverAction;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverResult;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DataSourcesRetrieverHandler implements
		ActionHandler<DataSourcesRetrieverAction, DataSourcesRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public DataSourcesRetrieverHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<DataSourcesRetrieverAction> getActionType() {
		return DataSourcesRetrieverAction.class;
	}

	@Override
	public DataSourcesRetrieverResult execute(
			DataSourcesRetrieverAction action, ExecutionContext context)
			throws DispatchException {
		try {
			return new DataSourcesRetrieverResult(
					ServiceLocator
							.getInstance()
							.getDaoFactory(
									BootstrapListener
											.getDbConnector(contextProvider
													.get()))
							.getDataSourceDao()
							.getAll(action.getType(),
									action.isPrimaryDataSourcesOnly()));
		} catch (DataAccessException e) {
			logger.error(e.toString());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(DataSourcesRetrieverAction action,
			DataSourcesRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
