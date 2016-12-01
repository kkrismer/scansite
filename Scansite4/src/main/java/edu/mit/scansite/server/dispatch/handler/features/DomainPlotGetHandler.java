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
import edu.mit.scansite.server.images.proteins.DomainPlotPainter;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.dispatch.features.DomainPlotGetAction;
import edu.mit.scansite.shared.dispatch.features.DomainPlotGetResult;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DomainPlotGetHandler implements
		ActionHandler<DomainPlotGetAction, DomainPlotGetResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public DomainPlotGetHandler(final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public DomainPlotGetResult execute(DomainPlotGetAction action,
			ExecutionContext arg1) throws DispatchException {
		try {
			LightWeightProtein protein = action.getProtein();
			if (protein != null && protein.getSequence() != null
					&& !protein.getSequence().isEmpty()) {

				DomainPlotPainter ppPainter = new DomainPlotPainter(
						protein.getSequence(), action.getDomainPositions());
				ppPainter.highlightPositions(action.getCenterAminoAcid(),
						action.getRelativeAminoAcid(), action.getPosition(),
						protein.toString());
				String imagePath = FilePaths
						.getProteinPlotFilePath(contextProvider.get()
								.getRealPath("/"));
				ImageInOut iio = new ImageInOut();
				iio.saveImage(ppPainter.getBufferedImage(), imagePath);

				DomainPlotGetResult result = new DomainPlotGetResult(
						imagePath.replace(contextProvider.get()
								.getRealPath("/"), ""),
						ppPainter.getDomainPositions());
				return result;
			} else {
				return new DomainPlotGetResult(
						"No protein / protein sequence given.");
			}
		} catch (Exception e) {
			logger.error("Error creating domain plot: " + e.toString());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public Class<DomainPlotGetAction> getActionType() {
		return DomainPlotGetAction.class;
	}

	@Override
	public void rollback(DomainPlotGetAction arg0, DomainPlotGetResult arg1,
			ExecutionContext arg2) throws DispatchException {
	}
}
