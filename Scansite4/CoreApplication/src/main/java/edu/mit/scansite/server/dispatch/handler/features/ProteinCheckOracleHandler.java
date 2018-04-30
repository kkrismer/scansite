package edu.mit.scansite.server.dispatch.handler.features;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.dispatch.features.ProteinCheckOracleAction;
import edu.mit.scansite.shared.dispatch.features.ProteinCheckOracleResult;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinCheckOracleHandler implements ActionHandler<ProteinCheckOracleAction, ProteinCheckOracleResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ProteinCheckOracleResult execute(ProteinCheckOracleAction action, ExecutionContext context)
			throws DispatchException {
		try {
			List<String> accList = ServiceLocator.getDaoFactory().getProteinDao().getAccessionsStartingWith(
					action.getIdentifier(), action.getDataSource(),
					ScansiteConstants.MAX_SUGGESTIONS_PROTEIN_ACCESSIONS);
			return new ProteinCheckOracleResult(accList);
		} catch (DataAccessException e) {
			logger.error("Error getting suggestions for protein accessions from database: " + e.toString());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public Class<ProteinCheckOracleAction> getActionType() {
		return ProteinCheckOracleAction.class;
	}

	@Override
	public void rollback(ProteinCheckOracleAction arg0, ProteinCheckOracleResult arg1, ExecutionContext arg2)
			throws DispatchException {
	}
}
