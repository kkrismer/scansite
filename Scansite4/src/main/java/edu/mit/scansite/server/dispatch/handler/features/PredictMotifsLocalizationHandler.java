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
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult;
import edu.mit.scansite.shared.dispatch.features.PredictMotifsLocalizationAction;

/**
 * @author Konstantin Krismer
 */
public class PredictMotifsLocalizationHandler
		implements
		ActionHandler<PredictMotifsLocalizationAction, PredictLocalizationResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public PredictMotifsLocalizationHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<PredictMotifsLocalizationAction> getActionType() {
		return PredictMotifsLocalizationAction.class;
	}

	@Override
	public PredictLocalizationResult execute(
			PredictMotifsLocalizationAction action, ExecutionContext context)
			throws DispatchException {
		try {
			PredictLocalizationFeature feature = new PredictLocalizationFeature(
					BootstrapListener.getDbConnector(contextProvider.get()));
			return feature.doPredictMotifsLocalization(
					action.getLocalizationDataSource(), action.getMotifClass());
		} catch (Exception e) {
			logger.error("Error running localization prediction: "
					+ e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(PredictMotifsLocalizationAction action,
			PredictLocalizationResult result, ExecutionContext context)
			throws DispatchException {
	}
}
