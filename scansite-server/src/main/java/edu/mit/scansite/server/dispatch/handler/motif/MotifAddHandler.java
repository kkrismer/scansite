package edu.mit.scansite.server.dispatch.handler.motif;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.HistogramDao;
import edu.mit.scansite.server.dataaccess.MotifDao;
import edu.mit.scansite.server.dispatch.handler.user.LoginHandler;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.MotifAddAction;
import edu.mit.scansite.shared.transferobjects.Histogram;
import edu.mit.scansite.shared.transferobjects.LightWeightMotif;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.User;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifAddHandler implements ActionHandler<MotifAddAction, LightWeightMotifRetrieverResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;
	private final LoginHandler loginHandler;

	@Inject
	public MotifAddHandler(final Provider<ServletContext> contextProvider, final Provider<LoginHandler> loginHandler) {
		this.contextProvider = contextProvider;
		this.loginHandler = loginHandler.get();
	}

	@Override
	public Class<MotifAddAction> getActionType() {
		return MotifAddAction.class;
	}

	@Override
	public LightWeightMotifRetrieverResult execute(MotifAddAction action, ExecutionContext context)
			throws DispatchException {
		try {
			DaoFactory factory = ServiceLocator.getDaoFactory();
			MotifDao motifDao = factory.getMotifDao();
			HistogramDao histogramDao = factory.getHistogramDao();

			if (!action.isUpdate()) { // add motif and histograms
				Motif motif = action.getMotif();
				int motifId = motifDao.addMotif(motif);
				motif.setId(motifId);
				// save just the plain version of the histograms (no
				// thresholds).
				for (Histogram histogram : action.getHistograms()) {
					histogram.setImageFilePath(FilePaths.getHistogramFilePath(contextProvider.get().getRealPath("/"),
							null, FilePaths.getFilePathNumber(histogram.getImageFilePath())));
					histogram.setMotif(motif);
					ServerHistogram serverHistogram = new ServerHistogram(histogram);
					histogramDao.add(serverHistogram);
				}
			} else { // update motif and histograms
				motifDao.updateMotifData(action.getMotif());
				for (Histogram histogram : action.getHistograms()) {
					histogram.setImageFilePath(FilePaths.getHistogramFilePath(contextProvider.get().getRealPath("/"),
							null, FilePaths.getFilePathNumber(histogram.getImageFilePath())));
					ServerHistogram serverHistogram = new ServerHistogram(histogram);
					histogramDao.updateHistogram(serverHistogram, true);
				}
			}
			
			// retrieve updated list of motifs
			User user = loginHandler.getUserBySessionId(action.getUserSessionId());
			List<Motif> motifs = ServiceLocator.getDaoFactory().getMotifDao().getAll(action.getMotif().getMotifClass(),
					user, !user.isAdmin());
			List<LightWeightMotif> lightWeightMotifs = new LinkedList<LightWeightMotif>();
			for (Motif motif : motifs) {
				lightWeightMotifs
						.add(new LightWeightMotif(motif.getId(), motif.getDisplayName(), motif.getShortName()));
			}
			return new LightWeightMotifRetrieverResult(lightWeightMotifs);
		} catch (Exception e) {
			logger.error("Error adding motif: " + e.toString());
			throw new ActionException(e.getMessage(), e);
		}
	}

	@Override
	public void rollback(MotifAddAction action, LightWeightMotifRetrieverResult result, ExecutionContext context)
			throws DispatchException {
	}
}
