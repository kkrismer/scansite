package edu.mit.scansite.server.dispatch.handler.features;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletContext;

import edu.mit.scansite.server.dispatch.handler.user.LoginHandler;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.SiteEvidenceDao;
import edu.mit.scansite.server.dispatch.BootstrapListener;
import edu.mit.scansite.server.features.ProteinScanFeature;
import edu.mit.scansite.shared.dispatch.features.ProteinScanAction;
import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinScanHandler implements
		ActionHandler<ProteinScanAction, ProteinScanResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;
    private final LoginHandler loginHandler;

	@Inject
	public ProteinScanHandler(final Provider<ServletContext> contextProvider,
            final Provider<LoginHandler> loginHandler) {
		this.contextProvider = contextProvider;
        this.loginHandler = loginHandler.get();
	}

	@Override
	public Class<ProteinScanAction> getActionType() {
		return ProteinScanAction.class;
	}

	@Override
	public ProteinScanResult execute(ProteinScanAction action,
			ExecutionContext context) throws DispatchException {
		try {
			DaoFactory factory = ServiceLocator.getDaoFactory();
			LightWeightProtein protein = null;
			Set<String> accessions = null;
			if (action.getProtein().getDataSource() != null) {
				protein = factory.getProteinDao().get(
						action.getProtein().getIdentifier(),
						action.getProtein().getDataSource());
				if (protein == null) {
					return new ProteinScanResult(
							"No protein with protein identifier "
									+ action.getProtein().getIdentifier()
									+ " and data source "
									+ action.getProtein().getDataSource()
											.getDisplayName() + " found");
				}
				// prepare annotations, for later PhosphoSite check
				// (swissprot-only)
				if (protein.getDataSource().getShortName()
						.equalsIgnoreCase("swissprot")) {
					accessions = ((Protein) protein).getAnnotation("accession");
				}
			} else {
				protein = action.getProtein();
			}
			ProteinScanFeature feature = new ProteinScanFeature();
			ProteinScanResult result = feature.doProteinScan(protein,
					action.getMotifSelection(), action.getStringency(),
					action.isShowDomains(),
					action.getHistogramDataSource(), action.getHistogramTaxon(),
					action.getLocalizationDataSource(), true,
                    !loginHandler.isSessionValidLogin(action.getUserSessionId()),
                    contextProvider.get().getRealPath("/"));

			// check if the sites are PhosphoSiteSites if swissprot is selected
			if (accessions != null) {
				accessions.add(protein.getIdentifier());
				SiteEvidenceDao evidenceDao = factory.getSiteEvidenceDao();
				for (ScanResultSite site : result.getResults().getHits()) {
					try {
						site.setEvidence(evidenceDao.getSiteEvidence(
								accessions, site.getSite()));
					} catch (Exception e) {
						logger.error("Error checking PSP database: "
								+ e.toString());
					}
				}
			}

            if(action.isPreviouslyMappedSitesOnly()) {
                ArrayList<ScanResultSite> allHits = new ArrayList<>(result.getResults().getHits());
                result.getResults().getHits().clear();
                for (ScanResultSite hit : allHits) {
                    if (hit.getEvidence() != null && !hit.getEvidence().isEmpty()) {
                        result.getResults().getHits().add(hit);
                    }
                }
            }

			return result;
		} catch (Exception e) {
			logger.error("Error running protein scan: " + e.toString());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(ProteinScanAction action, ProteinScanResult result,
			ExecutionContext context) throws DispatchException {
	}
}
