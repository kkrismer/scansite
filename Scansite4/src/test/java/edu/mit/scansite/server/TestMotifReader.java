//package edu.mit.scansite.test;
//
//import java.io.File;
//
//import edu.mit.scansite.server.dataaccess.file.FilePaths;
//import edu.mit.scansite.server.dataaccess.file.MotifFileReader;
//import edu.mit.scansite.server.dataaccess.file.ScansiteFileFormatException;
//import edu.mit.scansite.shared.DataAccessException;
//import edu.mit.scansite.shared.transferobjects.Motif;
//
//public class TestMotifReader {
//  public static void main(String[] args) {
//    String filePath = "test/AMPK18.txt";
//    File f = new File(filePath);
//    FilePaths.prepareDirectory(filePath, true);
//    System.out.println(f.getAbsolutePath());
//    
//    MotifFileReader reader = new MotifFileReader();
//    try {
//      Motif m = reader.getMotif(filePath);
//      System.out.println(m);
//    } catch (DataAccessException e) {
//      e.printStackTrace();
//      System.out.println();
//      System.out.println(e.getMessage());
//    } catch (ScansiteFileFormatException e) {
//      e.printStackTrace();
//      System.out.println();
//      System.out.println(e.getMessage());
//    }
//  }
//}
