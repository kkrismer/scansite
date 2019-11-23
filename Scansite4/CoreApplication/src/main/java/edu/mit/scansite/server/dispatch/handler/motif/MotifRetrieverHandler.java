package edu.mit.scansite.server.dispatch.handler.motif;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dispatch.handler.user.LoginHandler;
import edu.mit.scansite.shared.dispatch.motif.MotifRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.MotifRetrieverResult;
import edu.mit.scansite.shared.transferobjects.Motif;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifRetrieverHandler implements ActionHandler<MotifRetrieverAction, MotifRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final LoginHandler loginHandler;

	@Inject
	public MotifRetrieverHandler(final Provider<LoginHandler> loginHandler) {
		this.loginHandler = loginHandler.get();
	}

	@Override
	public Class<MotifRetrieverAction> getActionType() {
		return MotifRetrieverAction.class;
	}

	@Override
	public MotifRetrieverResult execute(MotifRetrieverAction action, ExecutionContext context)
			throws DispatchException {
		try {
			if (action.getMotifShortName() == null || action.getMotifShortName().isEmpty()) {
				logger.info("short name is null");
				logger.info(String.valueOf(ServiceLocator.getDaoFactory().getMotifDao()
						.getAll(action.getMotifClass(), loginHandler.getUserBySessionId(action.getUserSessionId())) == null));
				return new MotifRetrieverResult(new ArrayList<Motif>(ServiceLocator.getDaoFactory().getMotifDao()
						.getAll(action.getMotifClass(), loginHandler.getUserBySessionId(action.getUserSessionId()))));
			} else {
				logger.info("short name is not null");
				logger.info(action.getMotifShortName());
				Motif m = ServiceLocator.getDaoFactory().getMotifDao().getByShortName(action.getMotifShortName());
				logger.info(String.valueOf(m == null));
				ArrayList<Motif> ms = new ArrayList<Motif>();
				ms.add(m);
				return new MotifRetrieverResult(ms);
			}
		} catch (Exception e) {
			logger.error("Error retrieving motifs: " + e.getMessage());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(MotifRetrieverAction action, MotifRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
