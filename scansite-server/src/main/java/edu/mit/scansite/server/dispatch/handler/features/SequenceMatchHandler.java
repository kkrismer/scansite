package edu.mit.scansite.server.dispatch.handler.features;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.dispatch.handler.user.LoginHandler;
import edu.mit.scansite.server.features.SequenceMatchFeature;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.features.SequenceMatchAction;
import edu.mit.scansite.shared.dispatch.features.SequenceMatchResult;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SequenceMatchHandler implements ActionHandler<SequenceMatchAction, SequenceMatchResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;
	private final LoginHandler loginHandler;

	@Inject
	public SequenceMatchHandler(final Provider<ServletContext> contextProvider,
			final Provider<LoginHandler> loginHandler) {
		this.contextProvider = contextProvider;
		this.loginHandler = loginHandler.get();
	}

	@Override
	public Class<SequenceMatchAction> getActionType() {
		return SequenceMatchAction.class;
	}

	@Override
	public SequenceMatchResult execute(SequenceMatchAction action, ExecutionContext context) throws DispatchException {
		try {
			SequenceMatchFeature feature = new SequenceMatchFeature();
			return feature.doSequenceMatch(action.getSequencePatterns(), action.getDataSource(),
					action.getRestrictionProperties(), action.isLimitResultsToPhosphorylatedProteins(), true,
					loginHandler.getUserBySessionId(action.getUserSessionId()), contextProvider.get().getRealPath("/"));
		} catch (DataAccessException e) {
			logger.error("Error running sequence match: " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(SequenceMatchAction action, SequenceMatchResult result, ExecutionContext context)
			throws DispatchException {
	}
}
