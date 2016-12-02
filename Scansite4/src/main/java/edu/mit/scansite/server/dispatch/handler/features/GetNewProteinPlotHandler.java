package edu.mit.scansite.server.dispatch.handler.features;

import java.util.ArrayList;

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
import edu.mit.scansite.server.domains.DomainLocator;
import edu.mit.scansite.server.domains.DomainLocatorException;
import edu.mit.scansite.server.domains.DomainLocatorFactory;
import edu.mit.scansite.server.images.proteins.ProteinPlotPainter;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.dispatch.features.GetNewProteinPlotAction;
import edu.mit.scansite.shared.dispatch.features.GetNewProteinPlotResult;
import edu.mit.scansite.shared.transferobjects.DomainPosition;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.util.ScansiteAlgorithms;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class GetNewProteinPlotHandler implements
		ActionHandler<GetNewProteinPlotAction, GetNewProteinPlotResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public GetNewProteinPlotHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<GetNewProteinPlotAction> getActionType() {
		return GetNewProteinPlotAction.class;
	}

	@Override
	public GetNewProteinPlotResult execute(GetNewProteinPlotAction action,
			ExecutionContext context) throws DispatchException {
		try {
			LightWeightProtein protein = action.getProtein();
			if (protein != null && protein.getSequence() != null
					&& !protein.getSequence().isEmpty()) {
				ArrayList<DomainPosition> domainPositions = null;
				try {
					DomainLocator domainLocator = DomainLocatorFactory
							.getDomainLocator();
					domainLocator.init();
					domainPositions = domainLocator.getDomainPositions(contextProvider.get()
							.getRealPath("/"), protein
							.getSequence());
				} catch (DomainLocatorException e) {
					domainPositions = null;
				}

				ScansiteAlgorithms alg = new ScansiteAlgorithms();
				Double[] saValues = alg.calculateSurfaceAccessibility(protein
						.getSequence());
				ProteinPlotPainter ppPainter = new ProteinPlotPainter(
						protein.toString(), protein.getSequence(), saValues,
						true, domainPositions);
				ppPainter.applyHits(action.getPredictedSites());

				String imagePath = FilePaths
						.getProteinPlotFilePath(contextProvider.get()
								.getRealPath("/"));
				ImageInOut iio = new ImageInOut();
				iio.saveImage(ppPainter.getBufferedImage(), imagePath);

				return new GetNewProteinPlotResult(imagePath.replace(
						contextProvider.get().getRealPath("/"), ""),
						domainPositions);
			} else {
				return new GetNewProteinPlotResult(
						"No protein / protein sequence given.");
			}
		} catch (Exception e) {
			logger.error("Error running creating new protein plot: "
					+ e.toString());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(GetNewProteinPlotAction action,
			GetNewProteinPlotResult result, ExecutionContext context)
			throws DispatchException {
	}

}
