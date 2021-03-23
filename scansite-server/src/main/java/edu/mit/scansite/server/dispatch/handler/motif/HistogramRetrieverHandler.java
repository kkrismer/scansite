package edu.mit.scansite.server.dispatch.handler.motif;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.file.ImageInOut;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.dispatch.motif.HistogramRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.HistogramRetrieverResult;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramRetrieverHandler implements ActionHandler<HistogramRetrieverAction, HistogramRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public HistogramRetrieverHandler(final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<HistogramRetrieverAction> getActionType() {
		return HistogramRetrieverAction.class;
	}

	@Override
	public HistogramRetrieverResult execute(HistogramRetrieverAction action, ExecutionContext context)
			throws DispatchException {
		try {
			HistogramRetrieverResult result = new HistogramRetrieverResult();
			ServerHistogram sHist = ServiceLocator.getDaoFactory().getHistogramDao().getHistogram(action.getMotifId(),
					action.getTaxonName(), action.getDataSource());
			if (sHist != null) {
				String filePath = FilePaths.getHistogramFilePath(contextProvider.get().getRealPath("/"),
						sHist.toString(), null);
				sHist.setImageFilePath(filePath);
				ImageInOut imageIO = new ImageInOut();
				imageIO.saveImage(sHist.getReferenceHistogramPlot(action.getSite()), filePath);
				String clientFilePath = filePath.replace(contextProvider.get().getRealPath("/"), "");
				if (clientFilePath.startsWith("/") || clientFilePath.startsWith("\\")) {
					clientFilePath = clientFilePath.substring(1);
				}
				sHist.setImageFilePath(clientFilePath);
				result.setHistogram(sHist.toClientHistogram());
				result.setTopPosition(action.getTopPosition());
				result.setLeftPosition(action.getLeftPosition());
			}
			return result;
		} catch (Exception e) {
			logger.error("Error creating reference histogram: " + e.getMessage());
			throw new ActionException("Error creating reference histogram", e);
		}
	}

	@Override
	public void rollback(HistogramRetrieverAction action, HistogramRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
