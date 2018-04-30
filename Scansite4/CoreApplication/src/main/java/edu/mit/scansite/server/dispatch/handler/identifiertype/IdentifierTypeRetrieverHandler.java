package edu.mit.scansite.server.dispatch.handler.identifiertype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.features.IdentifierTypeRetrieverAction;
import edu.mit.scansite.shared.dispatch.features.IdentifierTypeRetrieverResult;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Konstantin Krismer
 */
public class IdentifierTypeRetrieverHandler
		implements ActionHandler<IdentifierTypeRetrieverAction, IdentifierTypeRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Class<IdentifierTypeRetrieverAction> getActionType() {
		return IdentifierTypeRetrieverAction.class;
	}

	@Override
	public IdentifierTypeRetrieverResult execute(IdentifierTypeRetrieverAction action, ExecutionContext context)
			throws DispatchException {
		try {
			return new IdentifierTypeRetrieverResult(
					ServiceLocator.getDaoFactory().getIdentifierDao().getAllIdentifierTypes());
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(IdentifierTypeRetrieverAction action, IdentifierTypeRetrieverResult result,
			ExecutionContext context) throws DispatchException {
	}
}
