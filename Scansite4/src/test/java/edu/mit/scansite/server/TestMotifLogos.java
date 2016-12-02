//package edu.mit.scansite.test;
//
//import java.io.File;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import edu.mit.scansite.server.ServiceLocator;
//import edu.mit.scansite.server.dataaccess.MotifDao;
//import edu.mit.scansite.server.dataaccess.file.ImageInOut;
//import edu.mit.scansite.server.images.motifs.MotifLogoPainter;
//import edu.mit.scansite.shared.DataAccessException;
//import edu.mit.scansite.shared.transferobjects.Motif;
//import edu.mit.scansite.shared.transferobjects.MotifClass;
//
//public class TestMotifLogos {
// 
//  @Test
//  public void testMotifLogo() {
//    try {
//      String dirName = "motifLogos/";
//      File f = new File(dirName);
//      if (!f.exists()) {
//        f.mkdir();
//      }
//      MotifDao mDao = ServiceLocator.getInstance().getDaoFactory().getMotifDao();
//      
//      for (Motif m : mDao.getAll(MotifClass.MAMMALIAN, false)) {
//        MotifLogoPainter painter = new MotifLogoPainter(m);
//        ImageInOut iio = new ImageInOut();
//        iio.saveImage(painter.getBufferedImage(), dirName + m.getShortName()+ ".png");
//      }
//    } catch (DataAccessException e) {
//      e.printStackTrace();
//      Assert.assertTrue(false);
//      return ;
//    }
//    Assert.assertTrue(true);
//  }
//}
