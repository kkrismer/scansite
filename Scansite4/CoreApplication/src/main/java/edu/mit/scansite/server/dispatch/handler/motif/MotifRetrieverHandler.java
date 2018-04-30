package edu.mit.scansite.server.dispatch.handler.motif;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
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

	@Override
	public Class<MotifRetrieverAction> getActionType() {
		return MotifRetrieverAction.class;
	}

	@Override
	public MotifRetrieverResult execute(MotifRetrieverAction action, ExecutionContext context)
			throws DispatchException {
		try {
			if (action.getMotifShortName() == null || action.getMotifShortName().isEmpty()) {
				return new MotifRetrieverResult(new ArrayList<Motif>(ServiceLocator.getDaoFactory().getMotifDao()
						.getAll(action.getMotifClass(), !action.isUserLoggedIn())));
			} else {
				Motif m = ServiceLocator.getDaoFactory().getMotifDao().getByShortName(action.getMotifShortName());
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
