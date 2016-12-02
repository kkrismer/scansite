//package edu.mit.scansite.test;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import org.junit.Test;
//
//import edu.mit.scansite.server.features.ProteinScanFeature;
//import edu.mit.scansite.shared.DataAccessException;
//import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;
//import edu.mit.scansite.shared.transferobjects.HistogramStringency;
//import edu.mit.scansite.shared.transferobjects.MotifClass;
//
//public class TestProteinScan {
//  @Test
//  public void testProteinScanWebService(){
//    try {
//      @SuppressWarnings("unused")
//      ProteinScanResult res = ProteinScanFeature.doProteinScan(true, 
//          "vav_mouse", null, "swissprot", new ArrayList<String>(Arrays.asList(new String[] {"yeastTest"})), MotifClass.YEAST, HistogramStringency.STRINGENCY_HIGH);
//    } catch (DataAccessException e) {
//      e.printStackTrace();
//    }
//  }
//}
