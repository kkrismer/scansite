package edu.mit.scansite.server.motifinserter;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.HistogramDao;
import edu.mit.scansite.server.dataaccess.MotifDao;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.dataaccess.file.DirectoryManagement;
import edu.mit.scansite.server.dataaccess.file.ImageInOut;
import edu.mit.scansite.server.dataaccess.file.MotifFileReader;
import edu.mit.scansite.server.dataaccess.file.ScansiteFileFormatException;
import edu.mit.scansite.server.images.histograms.HistogramMaker;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.server.updater.ScansiteUpdaterException;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class RunMotifInserter {
	private static final Logger logger = LoggerFactory
			.getLogger(RunMotifInserter.class);

	private static final String USAGE_TEXT = "USAGE:\n"
			+ "Please enter \n"
			+ "- the (relative) path to the directory of your motifs.xml and "
			+ " motif-files (all in one folder) as a first argument, and \n"
			+ "- a valid user's email address as a second argument\n"
			+ "Example: \n java -Djava.awt.headless=true -Xmx1024m -Xms1024m -jar RunMotifInserter.jar motifsMammals/ krismer@mit.edu\n";

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println(USAGE_TEXT);
			return;
		}
		String dir = args[0];
		String email = args[1];
		if (!dir.endsWith("/")) {
			dir += "/";
		}
		String motifFilePath = dir + "motifs.xml";
        DbConnector.getInstance().resizeConnectionPool(1);
		try {
			DaoFactory factory = ServiceLocator.getDaoFactory();
			try {
				User user = factory.getUserDao().get(email);
				if (user == null) {
					throw new Exception("Failed to find given user in database.\n\n" + USAGE_TEXT);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				return;
			}

			MotifsConfigXmlFileReader reader = new MotifsConfigXmlFileReader();
			List<Motif> motifs = reader.readConfig(motifFilePath);

			MotifFileReader motifFileReader = new MotifFileReader();

			logger.info("disables auto-commit, unique and foreign key checks");
			factory.getDataSourceDao().disableChecks();

			for (int i = 0; i < motifs.size(); ++i) {
				Motif currentMotif = motifs.get(i);
				List<LightWeightMotifGroup> groups =
						factory.getGroupsDao().getAllLightWeight();
				if (groups != null) {
					for (LightWeightMotifGroup group : groups) {
						if (group.getDisplayName().equalsIgnoreCase(
								currentMotif.getGroup().getDisplayName())
								|| group.getShortName().equalsIgnoreCase(
										currentMotif.getGroup().getShortName())) {
							currentMotif.setGroup(group);
						}
					}
				}
				if (currentMotif.getGroup().getId() <= 0) {
					factory.getGroupsDao().add(currentMotif.getGroup());
					logger.info("added motif group: "
							+ currentMotif.getGroup().getDisplayName());
				}

				logger.info("working on motif: " + currentMotif.getShortName());

				Motif fullMotif = motifFileReader.getMotif(dir
						+ currentMotif.getShortName() + ".txt");

				fullMotif.setShortName(currentMotif.getShortName());
				fullMotif.setDisplayName(currentMotif.getDisplayName());
				fullMotif.setGroup(currentMotif.getGroup());
				fullMotif.setPublic(currentMotif.isPublic());
				fullMotif.setSubmitter(email);
				fullMotif.setMotifClass(currentMotif.getMotifClass());
				fullMotif.setIdentifiers(currentMotif.getIdentifiers());
				motifs.set(i, fullMotif);

				ArrayList<Histogram> hists = new ArrayList<Histogram>();
				for (int j = 0; j < ScansiteConstants.HIST_DEFAULT_TAXON_NAMES.length; ++j) {
					hists.add(addHistogram(factory, fullMotif,
							ScansiteConstants.HIST_DEFAULT_TAXON_NAMES[j],
							ScansiteConstants.HIST_DEFAULT_DATASOURCE_SHORTS[j]));
				}

				addMotif(factory, hists, fullMotif);
			}

			logger.info("enabling auto-commit, unique and foreign key checks");
			factory.getDataSourceDao().enableChecks();
		} catch (ScansiteFileFormatException | DatabaseException | ScansiteUpdaterException e) {
			e.printStackTrace();
		}
	}

	private static boolean addMotif(DaoFactory factory,
			List<Histogram> histograms, Motif motif) {
		try {
			MotifDao motifDao = factory.getMotifDao();
			HistogramDao histogramDao = factory.getHistogramDao();
			motifDao.addMotif(motif);
			// save just the plain version of the histograms (no
			// thresholds)
			DirectoryManagement.prepareHistogramDirectory();
			for (Histogram histogram : histograms) {
				ServerHistogram serverHistogram = new ServerHistogram(histogram);
				histogram.setMotif(motif);
				histogramDao.add(serverHistogram);
			}
			return true;
		} catch (Exception e) {
			logger.error("Error adding motif: " + e.toString());
			return false;
		}
	}

	private static Histogram addHistogram(DaoFactory factory, Motif motif,
			String taxonName, String dataSourceShortName) {
		ImageInOut imageIO = new ImageInOut();
		String imagePathClient = "";
		Taxon taxon;
		DataSource dataSource = null;
		try {
			dataSource = factory.getDataSourceDao().get(dataSourceShortName);
			taxon = factory.getTaxonDao().getByName(taxonName, dataSource);
		} catch (DataAccessException e) {
			logger.error("Error accessing database in HistogramCreateHandler: "
					+ e.getMessage());
			return null;
		}

		// use current system time as unique histogram identifier
		long systimeMs = System.currentTimeMillis();

		// create a plain histogram from motif/taxon/datasource-proteins
		HistogramMaker hMaker = new HistogramMaker();
		ServerHistogram serverHistogram;
		try {
			serverHistogram = hMaker.makeServerHistogram(motif, dataSource,
					taxon, false, factory);
		} catch (DataAccessException e) {
			logger.error("Error creating histogram in HistogramCreateHandler: "
					+ e.toString());
			return null;
		}

		// get image path for client histogram
		imagePathClient = FilePaths.getHistogramFilePath("",
				serverHistogram.toString(), systimeMs);

		try {
			// generate and save plain histogram (without thresholds)
			imageIO.saveImage(serverHistogram.getDbHistogramPlot(),
					imagePathClient);
			// create data structure
			serverHistogram.getDbEditHistogramPlot();
		} catch (DataAccessException e) {
			logger.error("Error saving histogram image on file system in HistogramCreateHandler: "
					+ e.toString());
			return null;
		}
		serverHistogram.setImageFilePath(imagePathClient);

		return serverHistogram.toClientHistogram();
	}
}
