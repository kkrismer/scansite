package edu.mit.scansite.server.dispatch.handler.motif;

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
import edu.mit.scansite.server.dataaccess.file.ImageInOut;
import edu.mit.scansite.server.dispatch.BootstrapListener;
import edu.mit.scansite.server.images.histograms.HistogramMaker;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.dispatch.motif.HistogramCreateAction;
import edu.mit.scansite.shared.dispatch.motif.HistogramRetrieverResult;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class HistogramCreateHandler implements
		ActionHandler<HistogramCreateAction, HistogramRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public HistogramCreateHandler(final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<HistogramCreateAction> getActionType() {
		return HistogramCreateAction.class;
	}

	@Override
	public HistogramRetrieverResult execute(HistogramCreateAction action,
			ExecutionContext context) throws DispatchException {
		ImageInOut imageIO = new ImageInOut();
		String imagePathClient = "";
		HistogramRetrieverResult result = new HistogramRetrieverResult();
		Taxon taxon;
		try {
			taxon = ServiceLocator
					.getInstance()
					.getDaoFactory(
							BootstrapListener.getDbConnector(contextProvider
									.get())).getTaxonDao()
					.getByName(action.getTaxonName(), action.getDataSource());
			// dataSource = ServiceLocator
			// .getInstance()
			// .getDaoFactory(
			// BootstrapListener.getDbConnector(contextProvider
			// .get())).getDataSourceDao()
			// .get(action.getDataSourceShortName());
		} catch (DataAccessException e) {
			logger.error("Error accessing database in HistogramCreateHandler: "
					+ e.getMessage());
			throw new ActionException("Error accessing database.", e);
		}

		// use current system time as unique histogram identifier
		long systimeMs = System.currentTimeMillis();

		// create a plain histogram from motif/taxon/datasource-proteins
		HistogramMaker hMaker = new HistogramMaker();
		ServerHistogram serverHistogram;
		try {
			serverHistogram = hMaker.makeServerHistogram(action.getMotif(),
					action.getDataSource(), taxon);
		} catch (DataAccessException e) {
			logger.error("Error creating histogram in HistogramCreateHandler: "
					+ e.toString());
			throw new ActionException("Error creating histogram.", e);
		}

		// save plain histogram (without thresholds)
		try {
			imageIO.saveImage(serverHistogram.getDbHistogramPlot(), FilePaths
					.getHistogramFilePath(contextProvider.get()
							.getRealPath("/"), null, systimeMs));
		} catch (DataAccessException e) {
			logger.error("Error creating histogram in HistogramCreateHandler: "
					+ e.toString());
			throw new ActionException("Error saving histogram image.", e);
		}

		// get imagepath for client-histogram
		imagePathClient = FilePaths.getHistogramFilePath(contextProvider.get()
				.getRealPath("/"), serverHistogram.toString(), systimeMs);

		// save client-histogram, set filepath and create datastructure
		try {
			imageIO.saveImage(serverHistogram.getDbEditHistogramPlot(),
					imagePathClient);
		} catch (DataAccessException e) {
			logger.error("Error saving histogram image on file system in HistogramCreateHandler: "
					+ e.toString());
			throw new ActionException("Error saving histogram image.", e);
		}
		serverHistogram.setImageFilePath(imagePathClient.replace(
				contextProvider.get().getRealPath("/"), ""));

		result.setHistogramNr(action.getHistogramNr());
		result.setHistogram(serverHistogram.toClientHistogram());
		return result;
	}

	@Override
	public void rollback(HistogramCreateAction action,
			HistogramRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
