package edu.mit.scansite.server.dispatch.handler.features;

import java.util.List;

import javax.servlet.ServletContext;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dispatch.BootstrapListener;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.dispatch.features.ProteinCheckOracleAction;
import edu.mit.scansite.shared.dispatch.features.ProteinCheckOracleResult;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinCheckOracleHandler implements
		ActionHandler<ProteinCheckOracleAction, ProteinCheckOracleResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public ProteinCheckOracleHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public ProteinCheckOracleResult execute(ProteinCheckOracleAction action,
			ExecutionContext context) throws DispatchException {
		try {
			List<String> accList = ServiceLocator
					.getInstance()
					.getDaoFactory(
							BootstrapListener.getDbConnector(contextProvider
									.get()))
					.getProteinDao()
					.getAccessionsStartingWith(
							action.getIdentifier(),
							action.getDataSource(),
							ScansiteConstants.MAX_SUGGESTIONS_PROTEIN_ACCESSIONS);
			return new ProteinCheckOracleResult(accList);
		} catch (DataAccessException e) {
			logger.error("Error getting suggestions for protein accessions from database: "
					+ e.toString());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public Class<ProteinCheckOracleAction> getActionType() {
		return ProteinCheckOracleAction.class;
	}

	@Override
	public void rollback(ProteinCheckOracleAction arg0,
			ProteinCheckOracleResult arg1, ExecutionContext arg2)
			throws DispatchException {
	}
}
