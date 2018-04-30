package edu.mit.scansite.server.dispatch.handler.motif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.MotifGroupsDao;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifGroupRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupDeleteAction;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupDeleteHandler
		implements ActionHandler<MotifGroupDeleteAction, LightWeightMotifGroupRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Class<MotifGroupDeleteAction> getActionType() {
		return MotifGroupDeleteAction.class;
	}

	@Override
	public LightWeightMotifGroupRetrieverResult execute(MotifGroupDeleteAction action, ExecutionContext context)
			throws DispatchException {
		try {
			MotifGroupsDao groupDao = ServiceLocator.getDaoFactory().getGroupsDao();
			groupDao.delete(action.getId());
			return new LightWeightMotifGroupRetrieverResult(groupDao.getAllLightWeight());
		} catch (DataAccessException e) {
			logger.error("Error deleting group from database: " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(MotifGroupDeleteAction action, LightWeightMotifGroupRetrieverResult result,
			ExecutionContext context) throws DispatchException {
	}
}
