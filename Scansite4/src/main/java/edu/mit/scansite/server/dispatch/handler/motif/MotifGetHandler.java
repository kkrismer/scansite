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
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.file.ImageInOut;
import edu.mit.scansite.server.dispatch.BootstrapListener;
import edu.mit.scansite.server.images.motifs.MotifLogoPainter;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.dispatch.motif.MotifGetAction;
import edu.mit.scansite.shared.dispatch.motif.MotifGetResult;
import edu.mit.scansite.shared.transferobjects.LightWeightMotif;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGetHandler implements
		ActionHandler<MotifGetAction, MotifGetResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public MotifGetHandler(final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public MotifGetResult execute(MotifGetAction action,
			ExecutionContext context) throws DispatchException {
		try {
			DaoFactory fac = ServiceLocator.getInstance().getDaoFactory(
					BootstrapListener.getDbConnector(contextProvider.get()));
			Motif m = fac.getMotifDao().getByShortName(
					action.getMotifShortName());
			if (m != null) {
				m.setGroup(fac.getGroupsDao().get(m.getGroup().getId()));
				MotifLogoPainter logoPainter = new MotifLogoPainter(m);
				String motifLogoUrl = FilePaths.getMotifLogoPath(contextProvider.get().getRealPath("/"), action
						.getMotifShortName());
				ImageInOut iio = new ImageInOut();
				iio.saveImage(logoPainter.getBufferedImage(), motifLogoUrl);
				String clientFilePath = motifLogoUrl.replace(contextProvider.get().getRealPath("/"), "");
				if(clientFilePath.startsWith("/") || clientFilePath.startsWith("\\")) {
					clientFilePath = clientFilePath.substring(1);
				}
				return new MotifGetResult(new LightWeightMotif(m),
						clientFilePath, m.getIdentifiers(), m.getGroup(),
						m.getMotifClass());
			} else {
				logger.error("motif couldn't be retrieved");
				return new MotifGetResult("Motif couldn't be retrieved");
			}
		} catch (DataAccessException e) {
			logger.error("Error retrieving motif: " + e.getMessage());
			return new MotifGetResult("Fetching motif from database failed");
		} catch (Exception e) {
			logger.error("Error creating motiflogo: " + e.getMessage());
			return new MotifGetResult("Creating motif logo failed: "
					+ e.getMessage());
		}
	}

	@Override
	public Class<MotifGetAction> getActionType() {
		return MotifGetAction.class;
	}

	@Override
	public void rollback(MotifGetAction action, MotifGetResult result,
			ExecutionContext context) throws DispatchException {
	}
}
