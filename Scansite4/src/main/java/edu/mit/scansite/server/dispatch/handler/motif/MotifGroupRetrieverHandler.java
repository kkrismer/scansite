package edu.mit.scansite.server.dispatch.handler.motif;

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
import edu.mit.scansite.server.dispatch.handler.user.LoginHandler;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupRetrieverResult;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupRetrieverHandler implements
		ActionHandler<MotifGroupRetrieverAction, MotifGroupRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;
	private final LoginHandler loginHandler;

	@Inject
	public MotifGroupRetrieverHandler(
			final Provider<ServletContext> contextProvider,
			final Provider<LoginHandler> loginHandler) {
		this.contextProvider = contextProvider;
		this.loginHandler = loginHandler.get();
	}

	@Override
	public Class<MotifGroupRetrieverAction> getActionType() {
		return MotifGroupRetrieverAction.class;
	}

	@Override
	public MotifGroupRetrieverResult execute(MotifGroupRetrieverAction action,
			ExecutionContext context) throws DispatchException {
		try {
			return new MotifGroupRetrieverResult(ServiceLocator
					.getInstance()
					.getDaoFactory(
							BootstrapListener.getDbConnector(contextProvider
									.get()))
					.getGroupsDao()
					.getAll(action.getMotifClass(),
							!loginHandler.isSessionValidLogin(action
									.getUserSessionId())));
		} catch (DataAccessException e) {
			logger.error(e.toString());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(MotifGroupRetrieverAction action,
			MotifGroupRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
