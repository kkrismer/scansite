//package edu.mit.scansite.test;
//
//import java.util.ArrayList;
//
//import net.customware.gwt.dispatch.shared.DispatchException;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import edu.mit.scansite.server.ServiceLocator;
//import edu.mit.scansite.server.dataaccess.file.MotifFileReader;
//import edu.mit.scansite.server.dataaccess.file.ScansiteFileFormatException;
//import edu.mit.scansite.server.dispatch.handler.features.ProteinScanHandler;
//import edu.mit.scansite.shared.DataAccessException;
//import edu.mit.scansite.shared.dispatch.features.ProteinScanAction;
//import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;
//import edu.mit.scansite.shared.transferobjects.HistogramStringency;
//import edu.mit.scansite.shared.transferobjects.Motif;
//import edu.mit.scansite.shared.transferobjects.MotifClass;
//import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
//import edu.mit.scansite.shared.transferobjects.Protein;
//import edu.mit.scansite.shared.transferobjects.ScanResultSite;
//import edu.mit.scansite.shared.util.ScansiteAlgorithms;
//import edu.mit.scansite.shared.util.ScansiteScoring;
//
//public class TestScoring {
//
//  @Test
//  public void testScoring() {
//    try {
//      String prot = "ncf1_human";
//      String dbShort = "swissprot";
//      String motifShortName = "PDZ_class1";// PDZ_class1  Amphi_SH3 // check histogram of this very motif (last line is odd)
//  
//      LightWeightMotifGroup group = new LightWeightMotifGroup(1, "bla", "bla");
//      MotifFileReader mfr = new MotifFileReader();
//      Motif motif = mfr.getMotif("test/motifs/"+motifShortName+".txt");
//      motif.setShortName(motifShortName); motif.setName(motifShortName); 
//      motif.setSubmitter("tobieh@mit.edu"); 
//      motif.setGroup(group);
//      
//      Protein p = ServiceLocator.getInstance().getDaoFactory().getProteinDao().get(prot, dbShort);
//      
//      ScansiteScoring scoring = new ScansiteScoring();
//      ArrayList<ScanResultSite> sites = scoring.scoreProtein(motif, p, 2);
//      ScansiteAlgorithms algs = new ScansiteAlgorithms();
//      Double[] sas = algs.calculateSurfaceAccessibility(p.getSequence());
//      for (ScanResultSite site : sites) {
//        System.out.printf("%s\t%s\t%.3f\t%.4f\n", site.getSite(), site.getSiteSequence(), sas[site.getPosition()], site.getScore());
//      }
//      Assert.assertTrue(true);
//    } catch (DataAccessException e) {
//      Assert.assertTrue(false);
//      e.printStackTrace();
//    } catch (ScansiteFileFormatException e) {
//      Assert.assertTrue(false);
//      e.printStackTrace();
//    }
//    
//  }
//  
//  @Test
//  public void testScanHandler() {
//    try {
//      String prot = "VAV_HUMAN";
//      String dbShort = "swissprot";
//      
//      Protein p = ServiceLocator.getInstance().getDaoFactory().getProteinDao().get(prot, dbShort);
//
//      ProteinScanHandler handler = new ProteinScanHandler();
//      ProteinScanResult result = handler.execute(new ProteinScanAction(p, new ArrayList<String>(), MotifClass.MAMMALIAN, HistogramStringency.STRINGENCY_HIGH, false, null, ""), null);
//      System.out.println(result.getResults().getImagePath());
//      System.out.println(result.getResults().getHits().size());
//      Assert.assertTrue(true);
//    } catch (DataAccessException e) {
//      Assert.assertTrue(false);
//      e.printStackTrace();
//    } catch (DispatchException e) {
//      Assert.assertTrue(false);
//      e.printStackTrace();
//    }
//  }
//  
//}
