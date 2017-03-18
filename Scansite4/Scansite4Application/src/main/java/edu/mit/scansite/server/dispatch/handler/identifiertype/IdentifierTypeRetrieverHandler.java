package edu.mit.scansite.server.dispatch.handler.identifiertype;

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
import edu.mit.scansite.shared.dispatch.features.IdentifierTypeRetrieverAction;
import edu.mit.scansite.shared.dispatch.features.IdentifierTypeRetrieverResult;

/**
 * @author Konstantin Krismer
 */
public class IdentifierTypeRetrieverHandler
		implements
		ActionHandler<IdentifierTypeRetrieverAction, IdentifierTypeRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public IdentifierTypeRetrieverHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<IdentifierTypeRetrieverAction> getActionType() {
		return IdentifierTypeRetrieverAction.class;
	}

	@Override
	public IdentifierTypeRetrieverResult execute(
			IdentifierTypeRetrieverAction action, ExecutionContext context)
			throws DispatchException {
		try {
			return new IdentifierTypeRetrieverResult(ServiceLocator
					.getDaoFactory().getIdentifierDao().getAllIdentifierTypes());
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(IdentifierTypeRetrieverAction action,
			IdentifierTypeRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
