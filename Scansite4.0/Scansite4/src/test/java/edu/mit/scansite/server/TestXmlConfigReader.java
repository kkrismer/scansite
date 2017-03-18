//package edu.mit.scansite.test;
//
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import edu.mit.scansite.server.ServiceLocator;
//import edu.mit.scansite.server.dataaccess.file.UpdaterConfigXmlFileReader;
//import edu.mit.scansite.server.updater.DbUpdaterConfig;
//
//public class TestXmlConfigReader {
//  
//  @Test
//  public void testXmlConfigReader() {
//    DbUpdaterConfig updaterConfig;
//    try {
//      String configFilePath = ServiceLocator.getInstance().getUpdaterConstantsFilePath();
//      String configDtdPath = ServiceLocator.getInstance().getUpdaterConstantsDtdPath();
//      UpdaterConfigXmlFileReader reader = new UpdaterConfigXmlFileReader();
//      updaterConfig = reader.readConfig(configFilePath, configDtdPath);
//      System.out.println(updaterConfig);
//      Assert.assertTrue(true);
//    } catch (Exception e) {
//      e.printStackTrace();
//      Assert.assertTrue(false);
//    }
//  }
//}
