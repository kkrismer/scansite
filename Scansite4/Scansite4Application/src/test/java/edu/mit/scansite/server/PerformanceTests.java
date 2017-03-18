//package edu.mit.scansite.server;
//
//import java.util.ArrayList;
//
//import org.junit.Test;
//
//import edu.mit.scansite.shared.transferobjects.Taxon;
//
//public class PerformanceTests {
//  long start = 0;
//  long stop = 0;
//  
//  @Test
//  public void testPerformance() {
//    // Create taxa and measure time
//    int nrOfTaxa = 1000000;
//    start = getTime();
//    ArrayList<Taxon> taxa = new ArrayList<Taxon>();
//    for (int i = 0; i < nrOfTaxa; ++i) {
//      Taxon t = new Taxon(i, "blabla", true);
//      taxa.add(t);
//    }
//    stop = getTime();
//    printTimeInSeconds("Created " + nrOfTaxa + " taxa");
//  }
//  
//  
//  private void printTimeInSeconds(String message) {
//    System.out.println("## " + message + ": " + (stop - start) / 1000.0 + "s");
//  }
//  
//  private long getTime() {
//    return System.currentTimeMillis();
//  }
//}
