package edu.mit.scansite.server.dispatch.handler.features;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.IdentifierDao;
import edu.mit.scansite.server.dispatch.handler.user.LoginHandler;
import edu.mit.scansite.server.features.OrthologScanFeature;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.dispatch.features.OrthologScanSequencePatternAction;
import edu.mit.scansite.shared.dispatch.features.OrthologScanSequencePatternResult;
import edu.mit.scansite.shared.transferobjects.DataSource;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Konstantin Krismer
 */
public class OrthologScanSequencePatternHandler
		implements ActionHandler<OrthologScanSequencePatternAction, OrthologScanSequencePatternResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final LoginHandler loginHandler;

	@Inject
	public OrthologScanSequencePatternHandler(final Provider<LoginHandler> loginHandler) {
		this.loginHandler = loginHandler.get();
	}

	@Override
	public Class<OrthologScanSequencePatternAction> getActionType() {
		return OrthologScanSequencePatternAction.class;
	}

	@Override
	public OrthologScanSequencePatternResult execute(OrthologScanSequencePatternAction action, ExecutionContext context)
			throws DispatchException {
		try {
			OrthologScanFeature feature = new OrthologScanFeature();
			if (action.isMainFormEmission()) {
				// main form: orthology data source and identifier type
				// specified
				return feature.scanOrthologsBySequencePattern(action.getSequencePattern(),
						action.getOrthologyDataSource(), action.getProtein(), action.getStringency(),
						action.getAlignmentRadius(), loginHandler.getUserBySessionId(action.getUserSessionId()));
			} else {
				// sequence pattern scan form: orthology data source not
				// specified,
				// identifier type needs to be inferred from protein data source
				IdentifierDao dao = ServiceLocator.getDaoFactory().getIdentifierDao();
				List<DataSource> orthologyDataSources = dao.getCompatibleOrthologyDataSourcesForIdentifierType(
						action.getProtein().getDataSource().getIdentifierType());
				if (orthologyDataSources.size() > 0) {
					// compatible orthology data source available
					return feature.scanOrthologsBySequencePattern(action.getSequencePattern(),
							orthologyDataSources.get(0), action.getProtein(), action.getStringency(),
							action.getAlignmentRadius(), loginHandler.getUserBySessionId(action.getUserSessionId()));
				} else {
					return new OrthologScanSequencePatternResult(
							"no compatible orthology data source available for data source "
									+ action.getProtein().getDataSource().getDisplayName());
				}
			}
		} catch (DatabaseException e) {
			logger.error("Error running sequence match: " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(OrthologScanSequencePatternAction action, OrthologScanSequencePatternResult result,
			ExecutionContext context) throws DispatchException {
	}
}
