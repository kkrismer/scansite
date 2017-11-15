package edu.mit.scansite.server.dispatch.handler.motif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.MotifDao;
import edu.mit.scansite.server.dispatch.handler.user.LoginHandler;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.motif.MotifNumbersRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.MotifNumbersRetrieverResult;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifNumbersRetrieverHandler
		implements ActionHandler<MotifNumbersRetrieverAction, MotifNumbersRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final LoginHandler loginHandler;

	@Inject
	public MotifNumbersRetrieverHandler(final Provider<LoginHandler> loginHandler) {
		this.loginHandler = loginHandler.get();
	}

	@Override
	public MotifNumbersRetrieverResult execute(MotifNumbersRetrieverAction action, ExecutionContext context)
			throws DispatchException {
		try {
			boolean onlyPublic = !loginHandler.isSessionValidLogin(action.getUserSessionId());
			MotifDao motifDao = ServiceLocator.getDaoFactory().getMotifDao();
			return new MotifNumbersRetrieverResult(motifDao.getNumberOfMotifs(MotifClass.MAMMALIAN, onlyPublic),
					motifDao.getNumberOfMotifs(MotifClass.YEAST, onlyPublic),
					motifDao.getNumberOfMotifs(MotifClass.OTHER, onlyPublic));
		} catch (DataAccessException e) {
			logger.error("Error retrieving motif counts: " + e.getMessage());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public Class<MotifNumbersRetrieverAction> getActionType() {
		return MotifNumbersRetrieverAction.class;
	}

	@Override
	public void rollback(MotifNumbersRetrieverAction arg0, MotifNumbersRetrieverResult arg1, ExecutionContext arg2)
			throws DispatchException {
	}
}