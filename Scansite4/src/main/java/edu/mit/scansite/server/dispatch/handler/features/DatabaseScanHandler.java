package edu.mit.scansite.server.dispatch.handler.features;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.SiteEvidenceDao;
import edu.mit.scansite.server.dispatch.BootstrapListener;
import edu.mit.scansite.server.dispatch.handler.user.LoginHandler;
import edu.mit.scansite.server.features.DatabaseScanFeature;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanAction;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanResult;
import edu.mit.scansite.shared.transferobjects.DatabaseSearchScanResultSite;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 * @author Thomas Bernwinkler
 */
public class DatabaseScanHandler implements
		ActionHandler<DatabaseScanAction, DatabaseScanResult> {
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
	public DatabaseScanResult execute(DatabaseScanAction action,
			ExecutionContext context) throws DispatchException {
		try {
			DatabaseScanFeature feature = new DatabaseScanFeature(
					BootstrapListener.getDbConnector(contextProvider.get()));
			DatabaseScanResult result = feature.doDatabaseSearch(action.getMotifSelection(), action
					.getDataSource(), action.getRestrictionProperties(), action
					.getOutputListSize(), true, !loginHandler
					.isSessionValidLogin(action.getUserSessionId()), contextProvider.get().getRealPath("/"));

            DaoFactory factory = ServiceLocator.getInstance().getDaoFactory(
                    BootstrapListener.getDbConnector(contextProvider.get()));
            // check if the sites are PhosphoSiteSites if swissprot is selected

            for (DatabaseSearchScanResultSite site : result.getDbSearchSites()) {
				Set<String> accessions = (site.getProtein().getAnnotation("accession"));
                if (accessions != null && !accessions.isEmpty()) {
                    SiteEvidenceDao evidenceDao = factory.getSiteEvidenceDao();
                    try {
                        site.getSite().setEvidence(evidenceDao.getSiteEvidence(
                                accessions, site.getSite().getSite()));
                    } catch (Exception e) {
                        logger.error("Error checking PSP database: "
                                + e.toString());
                    }
                }
            }

			if(action.isPreviouslyMappedSitesOnly()) {
				ArrayList<DatabaseSearchScanResultSite> allHits = new ArrayList<>(result.getDbSearchSites());
				result.getDbSearchSites().clear();
				for (DatabaseSearchScanResultSite hit : allHits) {
					if (hit.getSite().getEvidence() != null && !hit.getSite().getEvidence().isEmpty()) {
						result.getDbSearchSites().add(hit);
					}
				}
			}

			return result;
		} catch (Exception e) {
			logger.error("Error running database search: " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(DatabaseScanAction action, DatabaseScanResult result,
			ExecutionContext context) throws DispatchException {
	}
}
