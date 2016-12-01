package edu.mit.scansite.server.dispatch.handler.features;

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

import edu.mit.scansite.server.domains.DomainLocator;
import edu.mit.scansite.server.domains.DomainLocatorException;
import edu.mit.scansite.server.domains.DomainLocatorFactory;
import edu.mit.scansite.shared.dispatch.features.DomainPositionsGetAction;
import edu.mit.scansite.shared.dispatch.features.DomainPositionsGetResult;
import edu.mit.scansite.shared.transferobjects.DomainPosition;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DomainPositionsGetHandler implements
		ActionHandler<DomainPositionsGetAction, DomainPositionsGetResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public DomainPositionsGetHandler(final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<DomainPositionsGetAction> getActionType() {
		return DomainPositionsGetAction.class;
	}

	@Override
	public DomainPositionsGetResult execute(DomainPositionsGetAction action,
			ExecutionContext context) throws DispatchException {
		try {
			String sequence = action.getProteinSequence();
			if (sequence != null && !sequence.isEmpty()) {
				ArrayList<DomainPosition> domainPositions = null;
				try {
					DomainLocator domainLocator = DomainLocatorFactory
							.getDomainLocator();
					domainLocator.init();
					domainPositions = domainLocator
							.getDomainPositions(contextProvider.get()
									.getRealPath("/"), sequence);
				} catch (DomainLocatorException e) {
					domainPositions = null;
				}
				return new DomainPositionsGetResult(domainPositions);
			} else {
				return new DomainPositionsGetResult(
						"No protein / protein sequence given.");
			}
		} catch (Exception e) {
			logger.error("Error running creating new protein plot: "
					+ e.toString());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(DomainPositionsGetAction action,
			DomainPositionsGetResult result, ExecutionContext context)
			throws DispatchException {
	}

}
