package edu.mit.scansite.server.dispatch.handler.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverAction;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverResult;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DataSourcesRetrieverHandler
		implements ActionHandler<DataSourcesRetrieverAction, DataSourcesRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Class<DataSourcesRetrieverAction> getActionType() {
		return DataSourcesRetrieverAction.class;
	}

	@Override
	public DataSourcesRetrieverResult execute(DataSourcesRetrieverAction action, ExecutionContext context)
			throws DispatchException {
		try {
			return new DataSourcesRetrieverResult(ServiceLocator.getDaoFactory().getDataSourceDao()
					.getAll(action.getType(), action.isPrimaryDataSourcesOnly()));
		} catch (DataAccessException e) {
			logger.error(e.toString());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(DataSourcesRetrieverAction action, DataSourcesRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
