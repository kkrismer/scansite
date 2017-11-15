package edu.mit.scansite.server.dispatch.handler.features;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.dispatch.features.UtilitiesMwAndPiAction;
import edu.mit.scansite.shared.dispatch.features.UtilitiesMwAndPiResult;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.util.ScansiteAlgorithms;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UtilitiesMwAndPiHandler implements ActionHandler<UtilitiesMwAndPiAction, UtilitiesMwAndPiResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Class<UtilitiesMwAndPiAction> getActionType() {
		return UtilitiesMwAndPiAction.class;
	}

	@Override
	public UtilitiesMwAndPiResult execute(UtilitiesMwAndPiAction action, ExecutionContext context)
			throws DispatchException {
		LightWeightProtein protein = action.getProtein();
		if ((protein.getSequence() == null || protein.getSequence().isEmpty()) && protein.getDataSource() != null) {
			try {
				Protein p = ServiceLocator.getDaoFactory().getProteinDao().get(protein.getIdentifier(),
						protein.getDataSource());
				if (p == null) {
					return new UtilitiesMwAndPiResult(false, "Protein not found in database");
				} else {
					protein.setSequence(p.getSequence());
				}
			} catch (DataAccessException e) {
				logger.error("Error getting protein for Mw/pI calculation: " + e.getMessage());
				throw new ActionException(e.getMessage());
			}
		}
		ScansiteAlgorithms algs = new ScansiteAlgorithms();
		ArrayList<Double> molecularWeights = new ArrayList<Double>();
		ArrayList<Double> isoelectricPoints = new ArrayList<Double>();
		for (int nPhos = 0; nPhos <= action.getMaxSites(); ++nPhos) {
			molecularWeights.add(algs.calculateMolecularWeight(protein.getSequence(), nPhos));
			isoelectricPoints.add(algs.calculateIsoelectricPoint(protein.getSequence(), nPhos));
		}
		return new UtilitiesMwAndPiResult(molecularWeights, isoelectricPoints, protein, action.getMaxSites());
	}

	@Override
	public void rollback(UtilitiesMwAndPiAction action, UtilitiesMwAndPiResult result, ExecutionContext context)
			throws DispatchException {
	}
}
