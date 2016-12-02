//package edu.mit.scansite.test;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.junit.Test;
//
//import edu.mit.scansite.server.ServiceLocator;
//import edu.mit.scansite.server.dataaccess.DaoFactory;
//import edu.mit.scansite.server.dataaccess.HistogramDao;
//import edu.mit.scansite.server.dataaccess.databaseconnector.DatabaseException;
//import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
//import edu.mit.scansite.server.dataaccess.file.ProteinScanResultFileWriter;
//import edu.mit.scansite.server.dataaccess.file.ResultFileWriterException;
//import edu.mit.scansite.server.images.histograms.ServerHistogram;
//import edu.mit.scansite.shared.DataAccessException;
//import edu.mit.scansite.shared.ScansiteConstants;
//import edu.mit.scansite.shared.transferobjects.DataSource;
//import edu.mit.scansite.shared.transferobjects.HistogramStringency;
//import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
//import edu.mit.scansite.shared.transferobjects.Motif;
//import edu.mit.scansite.shared.transferobjects.MotifClass;
//import edu.mit.scansite.shared.transferobjects.OrganismClass;
//import edu.mit.scansite.shared.transferobjects.Protein;
//import edu.mit.scansite.shared.transferobjects.ScanResultSite;
//import edu.mit.scansite.shared.transferobjects.Taxon;
//import edu.mit.scansite.shared.util.ScansiteAlgorithms;
//import edu.mit.scansite.shared.util.ScansiteScoring;
//
//public class SeqMatchPlusProtScan {
//
//  private static ServiceLocator serviceLocator = ServiceLocator.getInstance();
//  private ScansiteAlgorithms alg = new ScansiteAlgorithms();
//  private ScansiteScoring scoring = new ScansiteScoring();
//
//  @Test
//  public void testProteinScanWebService() {
//    try {
//      DbConnector dbConnector = new DbConnector(
//          serviceLocator.getDbAccessFile());
//      dbConnector.connectToDb();
//
//      String resultPathPrefix = "S:/20130316_myaffe/";
//      String sequenceMatchRegex = "[DEST].{2,3}VP[DEST]Y";
//
//      Pattern regex = Pattern.compile(sequenceMatchRegex,
//          Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
//      MotifClass motifClass = MotifClass.MAMMALIAN;
//      LightWeightMotifGroup mGroup = serviceLocator.getDaoFactory()
//          .getGroupsDao().get(2); // Y_kin = 2
//      int referenceHistogram = ScansiteConstants.HIST_DEFAULT_INDEX; // vertebrata
//                                                                     // (default)
//                                                                     // = 1,
//                                                                     // yeast =
//                                                                     // 0
//
//      // get proteins matching regex
//      ArrayList<Protein> proteins = serviceLocator
//          .getDaoFactory()
//          .getProteinDao()
//          .get("swissprot", new String[] { sequenceMatchRegex },
//              OrganismClass.MAMMALIA, "Homo sapiens", null, 0, null, null,
//              null, null, true, false);
//
//      // prepare for protein scans
//      ArrayList<ScanResultSite> results = new ArrayList<ScanResultSite>();
//      DaoFactory daoFac = serviceLocator.getDaoFactory(dbConnector);
//      ArrayList<Motif> motifs = daoFac.getMotifDao().getByGroup(mGroup,
//          motifClass, false);
//      DataSource ds = daoFac.getDataSourceDao().get(
//          ScansiteConstants.HIST_DEFAULT_DATASOURCE_SHORTS[referenceHistogram]);
//      Taxon t = daoFac.getTaxonDao().getByName(
//          ScansiteConstants.HIST_DEFAULT_TAXON_NAMES[referenceHistogram],
//          ds.getShortName());
//      HistogramDao histDao = daoFac.getHistogramDao();
//      List<ServerHistogram> sHists = histDao.getHistograms(motifs, ds.getId(),
//          t.getId());
//      HashSet<Integer> matchIndexes = new HashSet<Integer>();
//      int count = 0;
//      for (HistogramStringency stringency : HistogramStringency.values()) {
//        results.clear();
//        for (Protein p : proteins) {
//          matchIndexes.clear();
//          Matcher matcher = regex.matcher(p.getSequence());
//          while (matcher.find()) {
//            // System.out.println(p.getAccessionNr());
//            // System.out.println(p.getSequence());
//            // System.out.println(matcher.end()-1);
//            // System.out.println(p.getSequenceAsCharArray()[matcher.end()-1]);
//            matchIndexes.add(matcher.end() - 1);
//            ++count;
//          }
//          Double[] saValues = alg
//              .calculateSurfaceAccessibility(p.getSequence());
//          for (ServerHistogram sh : sHists) {
//            double maxScore = sh.getScore(stringency.getPercentileValue());
//            ArrayList<ScanResultSite> sites = scoring.scoreProtein(
//                sh.getMotif(), p, maxScore);
//
//            for (ScanResultSite site : sites) {
//              // System.out.println(site.getPosition());
//              if (matchIndexes.contains(site.getPosition())) {
//                // System.out.println(" -> site matched");
//                site.setPercentile(sh.getPercentile(site.getScore()));
//                site.setSurfaceAccessValue(saValues[site.getPosition()]);
//                results.add(site);
//              }
//            }
//          }
//        }
//        System.out.println(count + " matches found");
//        System.out.println(results.size() + " matching sites found");
//        dbConnector.disconnectFromDb();
//        try {
//          ProtScanResultFileWriter fileWriter = new ProtScanResultFileWriter(
//              resultPathPrefix);
//          fileWriter.setMotifName("yKin.stringency" + stringency.getName()
//              + ".nSites" + results.size());
//          fileWriter.writeResults(results);
//        } catch (ResultFileWriterException e) {
//          System.err.println("Problem with writing results");
//          System.err.println(" -- " + e.getMessage() + "\n");
//        }
//      }
//    } catch (DataAccessException e) {
//      e.printStackTrace();
//    } catch (DatabaseException e1) {
//      e1.printStackTrace();
//    }
//  }
//
//  private class ProtScanResultFileWriter extends ProteinScanResultFileWriter {
//
//    private String motifName = null;
//    private String pathPrefix = null;
//
//    public ProtScanResultFileWriter(String pathPrefix) {
//      this.pathPrefix = pathPrefix;
//    }
//
//    public void setMotifName(String motifName) {
//      this.motifName = motifName;
//    }
//
//    @Override
//    protected String getFilePath() {
//      if (motifName == null) {
//        return super.getFilePath();
//      } else {
//        return pathPrefix + motifName + ".results" + FILE_POSTFIX;
//      }
//    }
//
//    @Override
//    public String writeResults(List<ScanResultSite> hits)
//        throws ResultFileWriterException {
//      String currentFilePath = getFilePath();
//      BufferedWriter writer = null;
//      try {
//        writer = new BufferedWriter(new FileWriter(currentFilePath));
//        writer.write("MOTIF_NAME");
//        writer.write(SEPARATOR);
//        writer.write("MOTIF_GENE_NAME");
//        writer.write(SEPARATOR);
//        writer.write("PROTEIN");
//        writer.write(SEPARATOR);
//        writer.write("SCORE");
//        writer.write(SEPARATOR);
//        writer.write("PERCENTILE");
//        writer.write(SEPARATOR);
//        writer.write("SITE");
//        writer.write(SEPARATOR);
//        writer.write("SITE_SEQUENCE");
//        writer.write(SEPARATOR);
//        writer.write("SURFACE_ACCESS_VALUE");
//        writer.write(SEPARATOR);
//        writer.write("PROTEIN_SEQUENCE");
//        writer.newLine();
//        for (ScanResultSite site : hits) {
//          writer.write(site.getMotif().getName());
//          writer.write(SEPARATOR);
//          writer.write(site.getMotif().getGeneInfo()[0]);
//          writer.write(SEPARATOR);
//          writer.write(site.getProtString());
//          writer.write(SEPARATOR);
//          writer.write(String.valueOf(site.getScore()));
//          writer.write(SEPARATOR);
//          writer.write(String.valueOf(site.getPercentile()));
//          writer.write(SEPARATOR);
//          writer.write(site.getSite());
//          writer.write(SEPARATOR);
//          writer.write(site.getSiteSequence());
//          writer.write(SEPARATOR);
//          writer.write(String.valueOf(site.getSurfaceAccessValue()));
//          writer.write(SEPARATOR);
//          writer.write(String.valueOf(site.getProtSequence()));
//          writer.newLine();
//        }
//        writer.close();
//        return currentFilePath;
//      } catch (Exception e) {
//        throw new ResultFileWriterException(e);
//      } finally {
//        try {
//          if (writer != null) {
//            writer.close();
//          }
//        } catch (Exception e) {
//        }
//      }
//    }
//  }
//
//}
