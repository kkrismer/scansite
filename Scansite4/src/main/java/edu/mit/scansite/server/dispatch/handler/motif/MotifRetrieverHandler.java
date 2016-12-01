package edu.mit.scansite.server.dispatch.handler.motif;

import java.util.ArrayList;

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
import edu.mit.scansite.shared.dispatch.motif.MotifRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.MotifRetrieverResult;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifRetrieverHandler implements
		ActionHandler<MotifRetrieverAction, MotifRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public MotifRetrieverHandler(final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<MotifRetrieverAction> getActionType() {
		return MotifRetrieverAction.class;
	}

	@Override
	public MotifRetrieverResult execute(MotifRetrieverAction action,
			ExecutionContext context) throws DispatchException {
		try {
			if (action.getMotifShortName() == null
					|| action.getMotifShortName().isEmpty()) {
				return new MotifRetrieverResult(new ArrayList<Motif>(
						ServiceLocator
								.getInstance()
								.getDaoFactory(
										BootstrapListener
												.getDbConnector(contextProvider
														.get()))
								.getMotifDao()
								.getAll(action.getMotifClass(),
										!action.isUserLoggedIn())));
			} else {
				Motif m = ServiceLocator
						.getInstance()
						.getDaoFactory(
								BootstrapListener
										.getDbConnector(contextProvider.get()))
						.getMotifDao()
						.getByShortName(action.getMotifShortName());
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
	public void rollback(MotifRetrieverAction action,
			MotifRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
