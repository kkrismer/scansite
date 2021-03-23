//package edu.mit.scansite.test;
//
//import java.awt.image.BufferedImage;
//import java.util.ArrayList;
//
//import org.junit.Test;
//
//import edu.mit.scansite.server.ServiceLocator;
//import edu.mit.scansite.server.dataaccess.file.FilePaths;
//import edu.mit.scansite.server.dataaccess.file.ImageInOut;
//import edu.mit.scansite.server.dispatch.handler.features.ProteinScanHandler;
//import edu.mit.scansite.server.images.proteins.DomainPlotPainter;
//import edu.mit.scansite.server.images.proteins.ProteinPlotPainter;
//import edu.mit.scansite.shared.dispatch.features.ProteinScanAction;
//import edu.mit.scansite.shared.transferobjects.AminoAcid;
//import edu.mit.scansite.shared.transferobjects.DataSource;
//import edu.mit.scansite.shared.transferobjects.DomainPosition;
//import edu.mit.scansite.shared.transferobjects.HistogramStringency;
//import edu.mit.scansite.shared.transferobjects.MotifClass;
//import edu.mit.scansite.shared.transferobjects.Protein;
//import edu.mit.scansite.shared.util.ScansiteAlgorithms;
//
//public class TestProteinPlot {
// 
//  @Test
//  public void testProteinPlot() {
//    try {
//      String prot = "VAV_HUMAN";
//      DataSource ds = ServiceLocator.getInstance().getDaoFactory(null).getDataSourceDao().get("SwissProt");
//      Protein p = ServiceLocator.getInstance().getDaoFactory(null).getProteinDao().get(prot, ds.getShortName());
//      ScansiteAlgorithms alg = new ScansiteAlgorithms();
//      Double sas[] = alg.calculateSurfaceAccessibility(p.getSequence());
//      double max = 0;
//      double min = 2;
//      double devSum = 0;
//      String sequence = p.getSequence();
//      for (int i = 0; i < sas.length ; ++i) {
//        devSum += Math.abs(1 - sas[i]);
//        if (sas[i] < min) {
//          min = sas[i];
//        }
//        if (sas[i] > max) {
//          max = sas[i];
//        }
//        //System.out.println(sequence.charAt(i) + String.valueOf(i +1 ) + " -> " + sas[i]);
//      }
//      System.out.println("Stats for SA values:");
//      System.out.println("Min " + min);
//      System.out.println("Max " + max);
//      System.out.println("MeanDevFrom " + devSum / ((double) sas.length));
//      
//      ProteinPlotPainter painter = new ProteinPlotPainter(prot, sequence, sas, false, null);
//      
//      BufferedImage img = painter.getBufferedImage();
//      ImageInOut iio = new ImageInOut();
//      String filepath = FilePaths.getProteinPlotFilePath();
//      iio.saveImage(img, filepath);
//      System.out.println("protplot image saved to " + filepath);
//      
//      ProteinScanHandler scanActions = new ProteinScanHandler();
//      scanActions.execute(new ProteinScanAction(p, new ArrayList<String>(), MotifClass.MAMMALIAN, HistogramStringency.STRINGENCY_MEDIUM, true, null, ""), null);
//      
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  
//  @Test
//  public void testDomainPlot() {
//    try {
//      String prot = "VAV_HUMAN";
//      DataSource ds = ServiceLocator.getInstance().getDaoFactory(null).getDataSourceDao().get("SwissProt");
//      Protein p = ServiceLocator.getInstance().getDaoFactory(null).getProteinDao().get(prot, ds.getShortName());
//      
//      ArrayList<DomainPosition> positions = new ArrayList<DomainPosition>();
//      positions.add(new DomainPosition(20, 30, "1", "PFAM", "1"));
//      positions.add(new DomainPosition(190, 260, "2", "PFAM", "2"));
//      positions.add(new DomainPosition(80, 175, "3", "PFAM", "3"));
//      
//      DomainPlotPainter painter = new DomainPlotPainter(p.getSequence(), positions);
//      painter.highlightPositions(AminoAcid.M, AminoAcid.M, 0, p.toString());
//      
//      BufferedImage img = painter.getBufferedImage();
//      ImageInOut iio = new ImageInOut();
//      String filepath = FilePaths.getProteinPlotFilePath();
//      iio.saveImage(img, filepath);
//      System.out.println("protplot image saved to " + filepath);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//}
