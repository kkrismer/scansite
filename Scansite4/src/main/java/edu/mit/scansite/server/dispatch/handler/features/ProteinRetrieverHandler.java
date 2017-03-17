package edu.mit.scansite.server.dispatch.handler.features;

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
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.features.ProteinRetrieverAction;
import edu.mit.scansite.shared.dispatch.features.ProteinRetrieverResult;
import edu.mit.scansite.shared.transferobjects.Protein;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinRetrieverHandler implements
		ActionHandler<ProteinRetrieverAction, ProteinRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public ProteinRetrieverHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<ProteinRetrieverAction> getActionType() {
		return ProteinRetrieverAction.class;
	}

	@Override
	public ProteinRetrieverResult execute(ProteinRetrieverAction action,
			ExecutionContext context) throws DispatchException {
		try {
			if (action.getProteinIdentifier() != null
					&& action.getDataSource() != null) {
				Protein protein = ServiceLocator.getDaoFactory()
					.getProteinDao().get(action.getProteinIdentifier(), action.getDataSource());
				if (protein != null) {
					return new ProteinRetrieverResult(protein);
				} else {
					return new ProteinRetrieverResult(
							"No protein with protein identifier "
									+ action.getProteinIdentifier()
									+ " and data source "
									+ action.getDataSource().getDisplayName()
									+ " found");
				}
			} else {
				return new ProteinRetrieverResult(
						"Requested protein not found in database");
			}
		} catch (DataAccessException e) {
			logger.error("Error getting protein from database: " + e.toString());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(ProteinRetrieverAction action,
			ProteinRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
