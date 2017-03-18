package edu.mit.scansite.server.dispatch.handler.motif;

import java.io.File;

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
import edu.mit.scansite.shared.dispatch.motif.HistogramRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.HistogramUpdateAction;
import edu.mit.scansite.shared.transferobjects.Histogram;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramUpdateHandler implements
		ActionHandler<HistogramUpdateAction, HistogramRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public HistogramUpdateHandler(final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<HistogramUpdateAction> getActionType() {
		return HistogramUpdateAction.class;
	}

	@Override
	public HistogramRetrieverResult execute(HistogramUpdateAction action,
			ExecutionContext context) throws DispatchException {
		ImageInOut imageIO = new ImageInOut();
		String imagePathClient = "";
		HistogramRetrieverResult result = new HistogramRetrieverResult();
		Histogram histogram = action.getHistogram();

		// use the client-histogram's filename to find the plain version of the
		// histogram
		long systimeMs = FilePaths.getFilePathNumber(histogram
				.getImageFilePath());

		// set imagefilepath to that of the plain histogram (that way, this
		// image is
		// loaded into the serverhistogram)
		histogram.setImageFilePath(FilePaths.getHistogramFilePath(
				contextProvider.get().getRealPath("/"), null, systimeMs));
		ServerHistogram serverHistogram = new ServerHistogram(histogram);
		serverHistogram.setPlot(imageIO.getImage(histogram.getImageFilePath()));

		deleteFile(histogram.getImageFilePath());
		deleteFile(FilePaths.getHistogramFilePath(contextProvider.get()
				.getRealPath("/"), null, systimeMs));

		// save plain histogram (without thresholds) - new fileName, since the
		// browser caches the other image
		systimeMs = System.currentTimeMillis();
		try {
			imageIO.saveImage(serverHistogram.getPlot(), FilePaths
					.getHistogramFilePath(contextProvider.get()
							.getRealPath("/"), null, systimeMs));
		} catch (DataAccessException e) {
			logger.error("Error saving histogram image: " + e.toString());
			throw new ActionException("Error saving histogram image.", e);
		}

		imagePathClient = FilePaths.getHistogramFilePath(contextProvider.get()
				.getRealPath("/"), histogram.toString(), systimeMs);

		// save client-histogram, set filepath and create datastructure
		try {
			imageIO.saveImage(serverHistogram.getDbEditHistogramPlot(),
					imagePathClient);
		} catch (DataAccessException e) {
			logger.error("Error saving histogram image: " + e.getMessage());
			throw new ActionException("Error saving histogram image.", e);
		}
		serverHistogram.setImageFilePath(imagePathClient.replace(
				contextProvider.get().getRealPath("/"), ""));

		result.setHistogramNr(action.getHistogramNr());
		result.setHistogram(serverHistogram.toClientHistogram());
		return result;
	}

	@Override
	public void rollback(HistogramUpdateAction action,
			HistogramRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}

	/**
	 * deletes the given file.
	 * 
	 * @param filePath
	 *            A path that leads to the file.
	 */
	private void deleteFile(String filePath) {
		File f = new File(filePath);
		if (f.exists() && f.isFile()) {
			f.delete();
		}
	}

}
