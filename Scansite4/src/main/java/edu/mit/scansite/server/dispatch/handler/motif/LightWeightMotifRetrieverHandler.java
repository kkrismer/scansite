package edu.mit.scansite.server.dispatch.handler.motif;

import java.util.LinkedList;
import java.util.List;

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
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifRetrieverResult;
import edu.mit.scansite.shared.transferobjects.LightWeightMotif;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class LightWeightMotifRetrieverHandler
		implements
		ActionHandler<LightWeightMotifRetrieverAction, LightWeightMotifRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;
	private final LoginHandler loginHandler;

	@Inject
	public LightWeightMotifRetrieverHandler(
			final Provider<ServletContext> contextProvider,
			final Provider<LoginHandler> loginHandler) {
		this.contextProvider = contextProvider;
		this.loginHandler = loginHandler.get();
	}

	@Override
	public Class<LightWeightMotifRetrieverAction> getActionType() {
		return LightWeightMotifRetrieverAction.class;
	}

	@Override
	public LightWeightMotifRetrieverResult execute(
			LightWeightMotifRetrieverAction action, ExecutionContext context)
			throws DispatchException {
		try {
			List<Motif> motifs = ServiceLocator.getDaoFactory()
					.getMotifDao().getAll(action.getMotifClass(),
							!loginHandler.isSessionValidLogin(action.getUserSessionId()));
			List<LightWeightMotif> lightWeightMotifs = new LinkedList<LightWeightMotif>();
			for (Motif motif : motifs) {
				lightWeightMotifs.add(new LightWeightMotif(motif.getId(), motif
						.getDisplayName(), motif.getShortName()));
			}
			return new LightWeightMotifRetrieverResult(lightWeightMotifs);
		} catch (DataAccessException e) {
			logger.error("Error retrieving motif names: " + e.getMessage());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(LightWeightMotifRetrieverAction action,
			LightWeightMotifRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
