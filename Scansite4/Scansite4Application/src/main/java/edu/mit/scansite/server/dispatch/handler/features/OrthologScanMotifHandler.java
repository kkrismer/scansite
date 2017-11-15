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
import edu.mit.scansite.shared.dispatch.features.OrthologScanMotifAction;
import edu.mit.scansite.shared.dispatch.features.OrthologScanMotifResult;
import edu.mit.scansite.shared.transferobjects.DataSource;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Konstantin Krismer
 */
public class OrthologScanMotifHandler implements ActionHandler<OrthologScanMotifAction, OrthologScanMotifResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final LoginHandler loginHandler;

	@Inject
	public OrthologScanMotifHandler(final Provider<LoginHandler> loginHandler) {
		this.loginHandler = loginHandler.get();
	}

	@Override
	public Class<OrthologScanMotifAction> getActionType() {
		return OrthologScanMotifAction.class;
	}

	@Override
	public OrthologScanMotifResult execute(OrthologScanMotifAction action, ExecutionContext context)
			throws DispatchException {
		try {
			OrthologScanFeature feature = new OrthologScanFeature();
			if (action.isMainFormEmission()) {
				// main form: orthology data source and identifier type
				// specified
				return feature.scanOrthologsByMotifGroup(action.getSitePosition(), action.getMotifGroup(),
						action.getOrthologyDataSource(), action.getProtein(), action.getStringency(),
						action.getAlignmentRadius(), !loginHandler.isSessionValidLogin(action.getUserSessionId()));
			} else {
				// protein scan form: orthology data source not specified,
				// identifier
				// type needs to be inferred from protein data source
				IdentifierDao dao = ServiceLocator.getDaoFactory().getIdentifierDao();
				List<DataSource> orthologyDataSources = dao.getCompatibleOrthologyDataSourcesForIdentifierType(
						action.getProtein().getDataSource().getIdentifierType());
				if (orthologyDataSources.size() > 0) {
					// compatible orthology data source available
					return feature.scanOrthologsByMotifGroup(action.getSitePosition(), action.getMotifGroup(),
							orthologyDataSources.get(0), action.getProtein(), action.getStringency(),
							action.getAlignmentRadius(), !loginHandler.isSessionValidLogin(action.getUserSessionId()));
				} else {
					// no compatible orthology data source available, choose
					// first data
					// (homologene)
					// is this intended behavior?
					// return feature.scanOrthologsByMotifGroup("homologene",
					// action.getProteinDataSourceShortName(),
					// action.getIdentifier(),
					// proteinIdentifierType.getId(),
					// action.getAlignmentRadius(),
					// action.getStringency(), action.getSitePosition(),
					// action.getMotifGroupId());
					return null;
				}
			}
		} catch (DatabaseException e) {
			logger.error("Error running sequence match: " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(OrthologScanMotifAction action, OrthologScanMotifResult result, ExecutionContext context)
			throws DispatchException {
	}
}
