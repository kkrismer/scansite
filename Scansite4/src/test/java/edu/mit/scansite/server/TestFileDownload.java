//package edu.mit.scansite.test;
//
//import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import edu.mit.scansite.server.dataaccess.file.Downloader;
//import edu.mit.scansite.server.updater.ScansiteUpdaterException;
//
//public class TestFileDownload {
//
//  /**
//   * @param args
//   */
//  public static void main(String[] args) {
//    Downloader downloader = new Downloader();
//    String fileName = "./test.png";
//    
//    File f = new File(fileName);
//    if (!f.exists()) {
//      System.out.println("File does not yet exist!");
//    }
//    try {
//      downloader.downloadFile(new URL("http://www.google.com/intl/en_ALL/images/srpr/logo1w.png"), fileName);
//    } catch (ScansiteUpdaterException e) {
//      e.printStackTrace();
//      System.out.println("ScansiteUpdaterException thrown --- something is wrong in the downloader!");
//    } catch (MalformedURLException e) {
//      e.printStackTrace();
//      System.out.println("MalformedURLException thrown --- specified URL is invalid!");
//    }
//    if (f.exists()) {
//      System.out.println("Congrats! The file exists now!!");
//      if (f.delete()) {
//        System.out.println("File deleted!");
//      } else {
//        System.out.println("Can't delete file");
//      }
//    } else {
//      System.out.println("Awww! The file still does not exist!");
//    }
//  }
//
//}
