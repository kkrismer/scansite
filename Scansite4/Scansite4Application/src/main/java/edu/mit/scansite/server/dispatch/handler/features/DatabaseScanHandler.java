package edu.mit.scansite.server.dispatch.handler.features;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.dispatch.handler.user.LoginHandler;
import edu.mit.scansite.server.features.DatabaseScanFeature;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanAction;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanResult;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 * @author Thomas Bernwinkler
 */
public class DatabaseScanHandler implements ActionHandler<DatabaseScanAction, DatabaseScanResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;
	private final LoginHandler loginHandler;

	@Inject
	public DatabaseScanHandler(final Provider<ServletContext> contextProvider,
			final Provider<LoginHandler> loginHandler) {
		this.contextProvider = contextProvider;
		this.loginHandler = loginHandler.get();
	}

	@Override
	public Class<DatabaseScanAction> getActionType() {
		return DatabaseScanAction.class;
	}

	@Override
	public DatabaseScanResult execute(DatabaseScanAction action, ExecutionContext context) throws DispatchException {
		try {
			DatabaseScanFeature feature = new DatabaseScanFeature();
			DatabaseScanResult result = feature.doDatabaseSearch(action.getMotifSelection(), action.getDataSource(),
					action.getRestrictionProperties(), action.getOutputListSize(), true,
					!loginHandler.isSessionValidLogin(action.getUserSessionId()),
					contextProvider.get().getRealPath("/"), action.isPreviouslyMappedSitesOnly());

			// DaoFactory factory = ServiceLocator.getDaoFactory();
			// check if the sites are PhosphoSiteSites if swissprot is selected

			return result;
		} catch (Exception e) {
			logger.error("Error running database search: " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(DatabaseScanAction action, DatabaseScanResult result, ExecutionContext context)
			throws DispatchException {
	}
}
