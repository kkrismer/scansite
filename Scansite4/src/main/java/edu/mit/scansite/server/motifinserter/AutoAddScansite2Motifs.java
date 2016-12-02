package edu.mit.scansite.server.motifinserter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Histogram;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.Taxon;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Tobieh
 */
public class AutoAddScansite2Motifs {
	private static final Logger logger = LoggerFactory
			.getLogger(AutoAddScansite2Motifs.class);

	private static final String USAGE_TEXT = "USAGE:\n"
			+ "Please enter the (relative) path to the directory of your motifs.txt and \n"
			+ " motif-files (all in one folder) as a first argument, and a valid user's \n"
			+ " email address as a second argument!\n"
			+ "Example: \n java -Djava.awt.headless=true -Xmx1024m -Xms1024m -jar autoMotifAdder.jar motifsMammals/ tobieh@mit.edu\n";

	private static final String KEY_MOTIF_CLASS = "[MOTIF_CLASS]";
	private static final String KEY_GROUP_SHORT = "[GROUP_SHORT]";
	private static final String KEY_GROUP_NAME = "[GROUP_NAME]";
	private static final String KEY_GENEINFO = "[GENEINFO]";
	private static final String KEY_MOTIF_SHORT = "[MOTIF_SHORT]";
	private static final String KEY_MOTIF_NAME = "[MOTIF_NAME]";
	private static final String KEY_IS_PUBLIC = "[IS_PUBLIC]";

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
		String motifFilePath = dir + "motifs.txt";
		DbConnector dbConnector = null;
		BufferedReader reader = null;
		try {
			dbConnector = new DbConnector(ServiceLocator.getInstance()
					.getDbAccessFile());
			dbConnector.initLongTimeConnection();
			DaoFactory factory = ServiceLocator.getInstance().getDaoFactory(
					dbConnector);
			try {
				User user = factory.getUserDao().get(email);
				if (user == null) {
					System.out
							.println("Failed to find given user in database.\n\n"
									+ USAGE_TEXT);
					return;
				}
			} catch (Exception e) {
				System.out.println("Failed to find given user in database.\n\n"
						+ USAGE_TEXT);
				return;
			}
			MotifFileReader motifFileReader = new MotifFileReader();

			LightWeightMotifGroup g = new LightWeightMotifGroup();
			Motif m = new Motif();
			reader = new BufferedReader(new FileReader(motifFilePath));
			String line = reader.readLine();

			logger.info("disables auto-commit, unique and foreign key checks");
			factory.getDataSourceDao().disableChecks();

			while (line != null) {
				if (line.contains("//")) {
					LightWeightMotifGroup temp = null;
					List<LightWeightMotifGroup> groups = factory.getGroupsDao()
							.getAllLightWeight();
					if (groups != null) {
						for (LightWeightMotifGroup group : groups) {
							if (group.getDisplayName().equalsIgnoreCase(
									g.getDisplayName())
									|| group.getShortName().equalsIgnoreCase(
											g.getShortName())) {
								temp = group;
							}
						}
					}
					if (temp != null && temp.getId() > 0) {
						g = temp;
					} else {
						factory.getGroupsDao().add(g);
					}

					System.out.println("Working on: " + m.getShortName());

					m.setGroup(g);
					Motif tempM = motifFileReader.getMotif(dir
							+ m.getShortName() + ".txt");
					// tempM.setGeneInfo(m.getGeneInfo());
					tempM.setGroup(m.getGroup());
					tempM.setDisplayName(m.getDisplayName());
					tempM.setPublic(m.isPublic());
					tempM.setShortName(m.getShortName());
					tempM.setSubmitter(email);
					tempM.setMotifClass(m.getMotifClass());
					m = tempM;

					ArrayList<Histogram> hists = new ArrayList<Histogram>();
					for (int i = 0; i < ScansiteConstants.HIST_DEFAULT_TAXON_NAMES.length; ++i) {
						hists.add(addHistogram(
								factory,
								i,
								m,
								ScansiteConstants.HIST_DEFAULT_TAXON_NAMES[i],
								ScansiteConstants.HIST_DEFAULT_DATASOURCE_SHORTS[i]));
					}

					addMotif(factory, hists, m);
					m = new Motif();
				} else if (line.startsWith(KEY_MOTIF_NAME)) {
					m.setDisplayName(getContent(line));
				} else if (line.startsWith(KEY_MOTIF_SHORT)) {
					m.setShortName(getContent(line));
				} else if (line.startsWith(KEY_GENEINFO)) {
					String gi = getContent(line);
					if (gi != null && !gi.isEmpty()) {
						// m.setGeneInfo(gi.split(String
						// .valueOf(Motif.MOTIF_INFO_ID_SPLITTER)));
					}
				} else if (line.startsWith(KEY_GROUP_NAME)) {
					g.setDisplayName(getContent(line));
				} else if (line.startsWith(KEY_GROUP_SHORT)) {
					g.setShortName(getContent(line));
				} else if (line.startsWith(KEY_MOTIF_CLASS)) {
					m.setMotifClass(MotifClass.getDbValue(getContent(line)));
				} else if (line.startsWith(KEY_IS_PUBLIC)) {
					try {
						m.setPublic(Boolean.parseBoolean(getContent(line)));
					} catch (Exception e) {
						m.setPublic(false);
					}
				}
				line = reader.readLine();
			}

			logger.info("enabling auto-commit, unique and foreign key checks");
			factory.getDataSourceDao().enableChecks();
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			System.out.println("Can not find or open file: " + motifFilePath);
		} catch (IOException | ScansiteFileFormatException
				| ScansiteUpdaterException | DatabaseException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			try {
				dbConnector.closeLongTimeConnection();
				reader.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private static boolean addMotif(DaoFactory factory,
			List<Histogram> histograms, Motif motif) {
		try {
			DirectoryManagement.prepareHistogramDirectory();
			MotifDao motifDao = factory.getMotifDao();
			HistogramDao histogramDao = factory.getHistogramDao();
			int motifId = motifDao.addMotif(motif);
			motif.setId(motifId);
			// save just the plain version of the histograms (no
			// thresholds).
			for (Histogram histogram : histograms) {
				histogram.setImageFilePath(FilePaths.getHistogramFilePath("",
						null, FilePaths.getFilePathNumber(histogram
								.getImageFilePath())));
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

	private static Histogram addHistogram(DaoFactory factory, int histogramNr,
			Motif motif, String taxonName, String dataSourceShortName) {
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

		// save plain histogram (without thresholds)
		try {
			DirectoryManagement.prepareHistogramDirectory();
			imageIO.saveImage(serverHistogram.getDbHistogramPlot(),
					FilePaths.getHistogramFilePath("", null, systimeMs));
		} catch (DataAccessException e) {
			logger.error("Error creating histogram in HistogramCreateHandler: "
					+ e.toString());
			return null;
		}

		// get imagepath for client-histogram
		imagePathClient = FilePaths.getHistogramFilePath("",
				serverHistogram.toString(), systimeMs);

		// save client-histogram, set file path and create data structure
		try {
			imageIO.saveImage(serverHistogram.getDbEditHistogramPlot(),
					imagePathClient);
		} catch (DataAccessException e) {
			logger.error("Error saving histogram image on file system in HistogramCreateHandler: "
					+ e.toString());
			return null;
		}
		serverHistogram.setImageFilePath(imagePathClient);

		return serverHistogram.toClientHistogram();
	}

	private static String getContent(String line) {
		String s = line.split("\t")[1];
		return s == null ? "" : s.trim();
	}
}
