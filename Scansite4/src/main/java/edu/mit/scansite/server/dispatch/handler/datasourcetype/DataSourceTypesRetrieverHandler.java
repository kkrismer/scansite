package edu.mit.scansite.server.dispatch.handler.datasourcetype;

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
import edu.mit.scansite.shared.dispatch.datasource.DataSourceTypesRetrieverAction;
import edu.mit.scansite.shared.dispatch.datasource.DataSourceTypesRetrieverResult;

/**
 * @author Konstantin Krismer
 */
public class DataSourceTypesRetrieverHandler
		implements
		ActionHandler<DataSourceTypesRetrieverAction, DataSourceTypesRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public DataSourceTypesRetrieverHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<DataSourceTypesRetrieverAction> getActionType() {
		return DataSourceTypesRetrieverAction.class;
	}

	@Override
	public DataSourceTypesRetrieverResult execute(
			DataSourceTypesRetrieverAction action, ExecutionContext context)
			throws DispatchException {
		try {
			return new DataSourceTypesRetrieverResult(ServiceLocator.getDaoFactory()
					.getDataSourceDao().getDataSourceTypes());
		} catch (DataAccessException e) {
			logger.error(e.toString());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(DataSourceTypesRetrieverAction action,
			DataSourceTypesRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
