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

import edu.mit.scansite.server.dataaccess.file.ImageInOut;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.dispatch.features.DbSearchHistogramRetrieverAction;
import edu.mit.scansite.shared.dispatch.features.DbSearchHistogramRetrieverResult;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DbSearchHistogramRetrieverHandler
		implements
		ActionHandler<DbSearchHistogramRetrieverAction, DbSearchHistogramRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public DbSearchHistogramRetrieverHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<DbSearchHistogramRetrieverAction> getActionType() {
		return DbSearchHistogramRetrieverAction.class;
	}

	@Override
	public DbSearchHistogramRetrieverResult execute(
			DbSearchHistogramRetrieverAction action, ExecutionContext context)
			throws DispatchException {
		DbSearchHistogramRetrieverResult result = new DbSearchHistogramRetrieverResult();
		if (action.getHistogramBasePath() != null && action.getSite() != null) {
			String filePath = FilePaths.getHistogramFilePath(contextProvider
					.get().getRealPath("/"));
			ServerHistogram sHist = new ServerHistogram();
			sHist.setImageFilePath(filePath);
			ImageInOut imageIO = new ImageInOut();
			try {
				imageIO.saveImage(
						sHist.getDbSearchReferenceHistogramPlot(
								contextProvider.get().getRealPath("/")
										+ action.getHistogramBasePath(),
								action.getSite()), filePath);
				String clientFilePath = filePath.replace(contextProvider.get().getRealPath("/"), "");
				if(clientFilePath.startsWith("/") || clientFilePath.startsWith("\\")) {
					clientFilePath = clientFilePath.substring(1);
				}
				result.setHistogramFilePath(clientFilePath);
			} catch (DataAccessException e) {
				logger.error("Error retrieving database search reference histogram: "
						+ e.toString());
				throw new ActionException(e.getMessage(), e);
			}
		}
		result.setTopPosition(action.getTopPosition());
		result.setLeftPosition(action.getLeftPosition());
		return result;
	}

	@Override
	public void rollback(DbSearchHistogramRetrieverAction action,
			DbSearchHistogramRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
