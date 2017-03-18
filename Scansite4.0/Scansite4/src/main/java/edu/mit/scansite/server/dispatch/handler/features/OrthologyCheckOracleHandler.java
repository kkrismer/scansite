package edu.mit.scansite.server.dispatch.handler.features;

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
import edu.mit.scansite.shared.dispatch.features.OrthologyCheckOracleAction;
import edu.mit.scansite.shared.dispatch.features.OrthologyCheckOracleResult;

/**
 * @author Konstantin Krismer
 */
public class OrthologyCheckOracleHandler implements
		ActionHandler<OrthologyCheckOracleAction, OrthologyCheckOracleResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public OrthologyCheckOracleHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<OrthologyCheckOracleAction> getActionType() {
		return OrthologyCheckOracleAction.class;
	}

	@Override
	public OrthologyCheckOracleResult execute(
			OrthologyCheckOracleAction action, ExecutionContext context)
			throws DispatchException {
		try {
			return new OrthologyCheckOracleResult(ServiceLocator.getDaoFactory()
					.getOrthologDao().getOrthologsCountByIdentifier(
							action.getOrthologyDataSource(),
							action.getIdentifier()));
		} catch (DataAccessException e) {
			logger.error(e.toString());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(OrthologyCheckOracleAction action,
			OrthologyCheckOracleResult result, ExecutionContext context)
			throws DispatchException {
	}
}
