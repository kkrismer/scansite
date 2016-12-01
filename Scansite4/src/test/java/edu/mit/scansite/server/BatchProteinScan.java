package edu.mit.scansite.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.HistogramDao;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.dataaccess.file.ProteinScanResultFileWriter;
import edu.mit.scansite.server.dataaccess.file.ResultFileWriterException;
import edu.mit.scansite.server.images.histograms.ServerHistogram;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import edu.mit.scansite.shared.transferobjects.Taxon;
import edu.mit.scansite.shared.util.ScansiteAlgorithms;
import edu.mit.scansite.shared.util.ScansiteScoring;

public class BatchProteinScan {

	private static ServiceLocator serviceLocator = ServiceLocator.getInstance();
	private ScansiteAlgorithms alg = new ScansiteAlgorithms();
	private ScansiteScoring scoring = new ScansiteScoring();

	@Test
	public void testProteinScanWebService() {
		String resultPathPrefix = "S:/WS190PeptideScansiteFormat/results/";
		String inputFilePath = "S:/WS190PeptideScansiteFormat/WS190PeptideScansiteFormat.txt";
		HistogramStringency stringency = HistogramStringency.STRINGENCY_MEDIUM;
		String motifName = "Casn_Kin2";
		MotifClass motifClass = MotifClass.MAMMALIAN;
		int referenceHistogram = ScansiteConstants.HIST_DEFAULT_INDEX; // vertebrata
																		// (default)
																		// = 1,
																		// yeast
																		// = 0

		File f = new File(inputFilePath);
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> seqs = new ArrayList<String>();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			System.err.println("File not found in location: "
					+ f.getAbsolutePath());
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		String line;
		try {
			line = reader.readLine();
			while (line != null) {
				String[] lineContent = line.split("\\s");
				names.add(lineContent[0]);
				seqs.add(lineContent[1]);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Error reading file: " + f.getAbsolutePath());
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		ArrayList<ScanResultSite> results = new ArrayList<ScanResultSite>();
		String proteinName = null;
		String seq = null;
		try {
			DbConnector dbConnector = new DbConnector(ServiceLocator
					.getInstance().getDbAccessFile());
			dbConnector.initConnectionPooling();
			DaoFactory daoFac = serviceLocator.getDaoFactory(dbConnector);
			Set<String> motifNicks = new HashSet<String>(new ArrayList<String>(
					Arrays.asList(new String[] { motifName })));
			List<Motif> motifs = new ArrayList<Motif>();
			motifs = daoFac.getMotifDao().getAll(motifNicks, motifClass, false);
			DataSource ds = daoFac
					.getDataSourceDao()
					.get(ScansiteConstants.HIST_DEFAULT_DATASOURCE_SHORTS[referenceHistogram]);
			Taxon t = daoFac
					.getTaxonDao()
					.getByName(
							ScansiteConstants.HIST_DEFAULT_TAXON_NAMES[referenceHistogram],
							ds);
			HistogramDao histDao = daoFac.getHistogramDao();
			List<ServerHistogram> sHists = histDao.getHistograms(motifs, ds,
					t.getId());
			for (int i = 0; i < names.size(); ++i) {
				proteinName = names.get(i);
				seq = seqs.get(i);
				Protein p = new Protein();
				p.setIdentifier(proteinName);
				p.setSequence(seq);

				Double[] saValues = alg.calculateSurfaceAccessibility(seq);
				for (ServerHistogram sh : sHists) {
					double maxScore = sh.getScore(stringency
							.getPercentileValue());
					ArrayList<ScanResultSite> sites = scoring.scoreProtein(
							sh.getMotif(), p, maxScore);
					for (ScanResultSite site : sites) {
						site.setPercentile(sh.getPercentile(site.getScore()));
						site.setSurfaceAccessValue(saValues[site.getPosition()]);
					}
					results.addAll(sites);
				}
			}
			try {
				ProtScanResultFileWriter fileWriter = new ProtScanResultFileWriter(
						resultPathPrefix);
				fileWriter.setMotifName(motifName);
				fileWriter.writeResults("", results); // TODO check - added
														// realPath - realPath
														// should be only
														// relevant in client
														// mode
			} catch (ResultFileWriterException e) {
				System.err.println("Problem with writing results");
				System.err.println(" -- " + e.getMessage() + "\n");
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (DatabaseException e1) {
			e1.printStackTrace();
		}
	}

	private class ProtScanResultFileWriter extends ProteinScanResultFileWriter {

		private String motifName = null;
		private String pathPrefix = null;

		public ProtScanResultFileWriter(String pathPrefix) {
			this.pathPrefix = pathPrefix;
		}

		public void setMotifName(String motifName) {
			this.motifName = motifName;
		}

		@Override
		protected String getFilePath(String realPath) {
			if (motifName == null) {
				return super.getFilePath(realPath);
			} else {
				return pathPrefix + motifName + ".results" + FILE_POSTFIX;
			}
		}

		@Override
		public String writeResults(String realPath, List<ScanResultSite> hits)
				throws ResultFileWriterException {
			String currentFilePath = getFilePath(realPath);
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(currentFilePath));
				// writer.write("MOTIF_NAME"); writer.write(SEPARATOR);
				writer.write("PROTEIN");
				writer.write(SEPARATOR);
				writer.write("SCORE");
				writer.write(SEPARATOR);
				writer.write("PERCENTILE");
				writer.write(SEPARATOR);
				writer.write("SITE");
				writer.write(SEPARATOR);
				writer.write("SITE_SEQUENCE");
				writer.write(SEPARATOR);
				writer.write("SURFACE_ACCESS_VALUE");
				writer.newLine();
				for (ScanResultSite site : hits) {
					// writer.write(site.getMotif().getName());
					// writer.write(SEPARATOR);
					writer.write(site.getProtein().getIdentifier());
					writer.write(SEPARATOR);
					writer.write(String.valueOf(site.getScore()));
					writer.write(SEPARATOR);
					writer.write(String.valueOf(site.getPercentile()));
					writer.write(SEPARATOR);
					writer.write(site.getSite());
					writer.write(SEPARATOR);
					writer.write(site.getSiteSequence());
					writer.write(SEPARATOR);
					writer.write(String.valueOf(site.getSurfaceAccessValue()));
					writer.newLine();
				}
				writer.close();
				return currentFilePath;
			} catch (Exception e) {
				throw new ResultFileWriterException(e);
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (Exception e) {
				}
			}
		}
	}

}
