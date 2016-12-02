package edu.mit.scansite.server.dispatch.handler.motif;

import javax.servlet.ServletContext;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.HistogramDao;
import edu.mit.scansite.server.dataaccess.MotifDao;
import edu.mit.scansite.server.dispatch.BootstrapListener;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.dispatch.BooleanResult;
import edu.mit.scansite.shared.dispatch.motif.MotifAddAction;
import edu.mit.scansite.shared.transferobjects.Histogram;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifAddHandler implements
		ActionHandler<MotifAddAction, BooleanResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public MotifAddHandler(final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<MotifAddAction> getActionType() {
		return MotifAddAction.class;
	}

	@Override
	public BooleanResult execute(MotifAddAction action, ExecutionContext context)
			throws DispatchException {
		try {
			MotifDao motifDao = ServiceLocator
					.getInstance()
					.getDaoFactory(
							BootstrapListener.getDbConnector(contextProvider
									.get())).getMotifDao();
			HistogramDao histogramDao = ServiceLocator
					.getInstance()
					.getDaoFactory(
							BootstrapListener.getDbConnector(contextProvider
									.get())).getHistogramDao();

			if (!action.isUpdate()) { // ADD NEW HISTOGRAM TO DATABASE
				Motif motif = action.getMotif();
				int motifId = motifDao.addMotif(motif);
				motif.setId(motifId);
				// save just the plain version of the histograms (no
				// thresholds).
				for (Histogram histogram : action.getHistograms()) {
					histogram.setImageFilePath(FilePaths.getHistogramFilePath(
							contextProvider.get().getRealPath("/"), null,
							FilePaths.getFilePathNumber(histogram
									.getImageFilePath())));
					ServerHistogram serverHistogram = new ServerHistogram(
							histogram);
					histogram.setMotif(motif);
					histogramDao.add(serverHistogram);
				}
				return new BooleanResult(true);
			} else { // IS UPDATE !
				motifDao.updateMotifData(action.getMotif());
				for (Histogram histogram : action.getHistograms()) {
					histogram.setImageFilePath(FilePaths.getHistogramFilePath(
							contextProvider.get().getRealPath("/"), null,
							FilePaths.getFilePathNumber(histogram
									.getImageFilePath())));
					ServerHistogram serverHistogram = new ServerHistogram(
							histogram);
					histogramDao.updateHistogram(serverHistogram, true);
				}
				return new BooleanResult(true);
			}
		} catch (Exception e) {
			logger.error("Error adding motif: " + e.toString());
			return new BooleanResult(false);
		}
	}

	@Override
	public void rollback(MotifAddAction action, BooleanResult result,
			ExecutionContext context) throws DispatchException {
	}

}
