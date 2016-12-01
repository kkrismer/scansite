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
import edu.mit.scansite.server.dataaccess.MotifGroupsDao;
import edu.mit.scansite.server.dispatch.BootstrapListener;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifGroupRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupDeleteAction;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupDeleteHandler
		implements
		ActionHandler<MotifGroupDeleteAction, LightWeightMotifGroupRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public MotifGroupDeleteHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<MotifGroupDeleteAction> getActionType() {
		return MotifGroupDeleteAction.class;
	}

	@Override
	public LightWeightMotifGroupRetrieverResult execute(
			MotifGroupDeleteAction action, ExecutionContext context)
			throws DispatchException {
		try {
			MotifGroupsDao groupDao = ServiceLocator
					.getInstance()
					.getDaoFactory(
							BootstrapListener.getDbConnector(contextProvider
									.get())).getGroupsDao();
			groupDao.delete(action.getId());
			return new LightWeightMotifGroupRetrieverResult(
					groupDao.getAllLightWeight());
		} catch (DataAccessException e) {
			logger.error("Error deleting group from database: " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(MotifGroupDeleteAction action,
			LightWeightMotifGroupRetrieverResult result,
			ExecutionContext context) throws DispatchException {
	}
}
