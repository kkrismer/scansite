package edu.mit.scansite.server.dispatch.handler.motif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.MotifGroupsDao;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifGroupRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupAddAction;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupAddHandler implements ActionHandler<MotifGroupAddAction, LightWeightMotifGroupRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Class<MotifGroupAddAction> getActionType() {
		return MotifGroupAddAction.class;
	}

	@Override
	public LightWeightMotifGroupRetrieverResult execute(MotifGroupAddAction action, ExecutionContext context)
			throws DispatchException {
		try {
			MotifGroupsDao groupDao = ServiceLocator.getDaoFactory().getGroupsDao();
			groupDao.add(action.getMotifGroup());
			return new LightWeightMotifGroupRetrieverResult(groupDao.getAllLightWeight());
		} catch (DataAccessException e) {
			logger.error("Error adding group to database: " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(MotifGroupAddAction action, LightWeightMotifGroupRetrieverResult result,
			ExecutionContext context) throws DispatchException {
	}
}