//package edu.mit.scansite.test;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import edu.mit.scansite.server.ServiceLocator;
//import edu.mit.scansite.server.dataaccess.file.ConfigReader;
//import edu.mit.scansite.server.dataaccess.file.FilePaths;
//import edu.mit.scansite.server.dataaccess.ssh.SshConnector;
//import edu.mit.scansite.server.dataaccess.ssh.SshConnector.SshResult;
//
//public class TestSSH {
//  @Test
//  public void testConnection(){
//    SshConnector conn = new SshConnector();
//    try {
//      String fileName = "test.txt";
//      conn.init(new ConfigReader(ServiceLocator.getInstance().getDomainLocatorConfigFile()));
//      conn.connect();
//      SshResult response = conn.runCommand("ls -la");
//      System.out.println(response);
//      response = conn.runCommand("echo \">PROT\n"
//          +"\" > " + fileName);
//      System.out.println(response);
//      response = conn.runCommand("cat " + fileName);
//      System.out.println(response);
//      response = conn.runCommand("rm " + fileName);
//      System.out.println(response);
//      response = conn.runCommand("ls -la");
//      System.out.println(response);
//      conn.disconnect();
//    } catch (Exception e) {
//      e.printStackTrace();
//      Assert.assertTrue(false);
//    }
//  }
//  
//  @Test
//  public void testCopyFile(){
//    SshConnector conn = new SshConnector();
//    try {
//      String fileName = "test.txt";
//      String filePath = FilePaths.getDomainSeqFilePath(fileName, true);
//      BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
//      writer.write("some test stuff");
//      writer.close();
//      conn.init(new ConfigReader(ServiceLocator.getInstance().getDomainLocatorConfigFile()));
//      conn.connect();
//      SshResult response = conn.runCommand("ls -la");
//      System.out.println(response);
//      File f = new File(filePath);
//      f.createNewFile();
//      conn.copyFileLocalToRemote(filePath, fileName);
//      response = conn.runCommand("ls -la");
//      System.out.println(response);
//      response = conn.runCommand("cat " + fileName);
//      System.out.println(response);
//      response = conn.runCommand("rm " + fileName);
//      System.out.println(response);
//      response = conn.runCommand("ls -la");
//      System.out.println(response);
//      f.delete();
//      conn.disconnect();
//    } catch (Exception e) {
//      e.printStackTrace();
//      Assert.assertTrue(false);
//    }
//  }
//  @Test
//  public void testIprScan(){
//    SshConnector conn = new SshConnector();
//    try {
//      conn.init(new ConfigReader(ServiceLocator.getInstance().getDomainLocatorConfigFile()));
//      conn.connect();
////      SshResult response = conn.runCommand("echo $PATH"); // which command checks if something is in filepath
////      SshResult response = conn.runCommand("which iprscan"); // which command checks if something is in filepath
//      SshResult response = conn.runCommand("/usr/local/pkg/iprscan-4.8/bin/iprscan -cli -i test/vav_caeel.fasta -o out.txt -appl hmmpfam -seqtype p -format raw");
//      System.out.println(response);
//
//      conn.disconnect();
//    } catch (Exception e) {
//      e.printStackTrace();
//      Assert.assertTrue(false);
//    }
//  }
//  
//}
