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
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationMotifProteinPairAction;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationMotifProteinPairResult;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult;
import edu.mit.scansite.shared.dispatch.features.PredictProteinsLocalizationResult;
import edu.mit.scansite.shared.transferobjects.Localization;

/**
 * @author Konstantin Krismer
 */
public class PredictLocalizationMotifProteinPairHandler
		implements
		ActionHandler<PredictLocalizationMotifProteinPairAction, PredictLocalizationResult> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public PredictLocalizationMotifProteinPairHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<PredictLocalizationMotifProteinPairAction> getActionType() {
		return PredictLocalizationMotifProteinPairAction.class;
	}

	@Override
	public PredictLocalizationResult execute(
			PredictLocalizationMotifProteinPairAction action,
			ExecutionContext context) throws DispatchException {
		try {
			PredictLocalizationFeature feature = new PredictLocalizationFeature();
			PredictLocalizationMotifProteinPairResult result = new PredictLocalizationMotifProteinPairResult();
			result.setMotif(action.getMotif());
			result.setLocalizationDataSource(action.getLocalizationDataSource());
			result.setProtein(action.getProtein());
			result.setSuccess(true);

			PredictProteinsLocalizationResult proteinLocalization = (PredictProteinsLocalizationResult) feature
					.doPredictProteinLocalization(
							action.getLocalizationDataSource(),
							action.getProtein());
			if (proteinLocalization.isSuccess()) {
				result.setTotalProteinLocalizations(proteinLocalization
						.getTotalProteinLocalizations());
				result.setProteinLocalization(proteinLocalization
						.getLocalization());
			}

			try {
				Localization motifLocalization = feature
						.doPredictMotifLocalization(
								action.getLocalizationDataSource(),
								action.getMotif());
				result.setMotifLocalization(motifLocalization);
				result.setTotalProteinLocalizations(proteinLocalization
						.getTotalProteinLocalizations());
			} catch (DataAccessException ex) {
				logger.warn("motif localization failed");
				result.setSuccess(false);
				result.setErrorMessage("motif localization failed");
			}
			return result;
		} catch (Exception e) {
			logger.error("Error running localization prediction: "
					+ e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(PredictLocalizationMotifProteinPairAction action,
			PredictLocalizationResult result, ExecutionContext context)
			throws DispatchException {
	}
}
