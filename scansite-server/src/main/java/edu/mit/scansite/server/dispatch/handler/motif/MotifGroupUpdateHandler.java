package edu.mit.scansite.server.dispatch.handler.motif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.MotifGroupsDao;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifGroupRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupUpdateAction;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupUpdateHandler
		implements ActionHandler<MotifGroupUpdateAction, LightWeightMotifGroupRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Class<MotifGroupUpdateAction> getActionType() {
		return MotifGroupUpdateAction.class;
	}

	@Override
	public LightWeightMotifGroupRetrieverResult execute(MotifGroupUpdateAction action, ExecutionContext context)
			throws DispatchException {
		try {
			MotifGroupsDao groupDao = ServiceLocator.getDaoFactory().getGroupsDao();
			boolean success = groupDao.update(action.getMotifGroup());
			if (!success) {
				// logging
				throw new ActionException("Error updating group");
			}
			return new LightWeightMotifGroupRetrieverResult(groupDao.getAllLightWeight());
		} catch (DataAccessException e) {
			logger.error("Error updating group: " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(MotifGroupUpdateAction action, LightWeightMotifGroupRetrieverResult result,
			ExecutionContext context) throws DispatchException {
	}
}
