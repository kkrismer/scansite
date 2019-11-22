package edu.mit.scansite.server.dispatch.handler.motif;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dispatch.handler.user.LoginHandler;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.MotifDeleteAction;
import edu.mit.scansite.shared.transferobjects.LightWeightMotif;
import edu.mit.scansite.shared.transferobjects.Motif;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifDeleteHandler implements ActionHandler<MotifDeleteAction, LightWeightMotifRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final LoginHandler loginHandler;

	@Inject
	public MotifDeleteHandler(final Provider<LoginHandler> loginHandler) {
		this.loginHandler = loginHandler.get();
	}

	@Override
	public Class<MotifDeleteAction> getActionType() {
		return MotifDeleteAction.class;
	}

	@Override
	public LightWeightMotifRetrieverResult execute(MotifDeleteAction action, ExecutionContext context)
			throws DispatchException {
		try {
			ServiceLocator.getDaoFactory().getMotifDao().deleteMotif(action.getMotifId());
			List<Motif> motifs = ServiceLocator.getDaoFactory().getMotifDao().getAll(action.getMotifClass(),
					loginHandler.getUserBySessionId(action.getUserSessionId()));
			List<LightWeightMotif> lightWeightMotifs = new LinkedList<LightWeightMotif>();
			for (Motif motif : motifs) {
				lightWeightMotifs
						.add(new LightWeightMotif(motif.getId(), motif.getDisplayName(), motif.getShortName()));
			}
			return new LightWeightMotifRetrieverResult(lightWeightMotifs);
		} catch (Exception e) {
			logger.error("Error deleting motif: " + e.getMessage());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(MotifDeleteAction action, LightWeightMotifRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
