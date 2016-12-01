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

import edu.mit.scansite.server.dispatch.BootstrapListener;
import edu.mit.scansite.server.features.PredictLocalizationFeature;
import edu.mit.scansite.shared.dispatch.features.PredictProteinsLocalizationAction;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult;

/**
 * @author Konstantin Krismer
 */
public class PredictProteinsLocalizationHandler implements
		ActionHandler<PredictProteinsLocalizationAction, PredictLocalizationResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public PredictProteinsLocalizationHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<PredictProteinsLocalizationAction> getActionType() {
		return PredictProteinsLocalizationAction.class;
	}

	@Override
	public PredictLocalizationResult execute(PredictProteinsLocalizationAction action,
			ExecutionContext context) throws DispatchException {
		try {
			PredictLocalizationFeature feature = new PredictLocalizationFeature(
					BootstrapListener.getDbConnector(contextProvider.get()));
			return feature.doPredictProteinLocalization(
					action.getLocalizationDataSource(), action.getProtein());
		} catch (Exception e) {
			logger.error("Error running localization prediction: "
					+ e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(PredictProteinsLocalizationAction action,
			PredictLocalizationResult result, ExecutionContext context)
			throws DispatchException {
	}
}
