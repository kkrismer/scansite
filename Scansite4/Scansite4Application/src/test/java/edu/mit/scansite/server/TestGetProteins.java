//package edu.mit.scansite.test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import junit.framework.Assert;
//import net.customware.gwt.dispatch.shared.DispatchException;
//
//import org.junit.Test;
//
//import edu.mit.scansite.server.ServiceLocator;
//import edu.mit.scansite.server.dispatch.handler.features.SequenceMatchHandler;
//import edu.mit.scansite.shared.DataAccessException;
//import edu.mit.scansite.shared.dispatch.features.SequenceMatchAction;
//import edu.mit.scansite.shared.transferobjects.PatternPosition;
//import edu.mit.scansite.shared.transferobjects.SequencePattern;
//
//public class TestGetProteins {
//
//  @Test
//  public void testGetProteinsRestricted() {
//    try {
//      SequenceMatchAction action = new SequenceMatchAction();
//      action.setDataSource(ServiceLocator.getInstance().getDaoFactory().getDataSourceDao().get("swissprot"));
//      action.setKeywordSearch("cell");
//      action.setMwFrom(100000D);
//      action.setMwTo(200000D);
//      action.setPiFrom(5D);
//      action.setPiTo(9D);
//      action.setSpecies("human");
//      action.setPhosphoSiteCount(2);
//      List<SequencePattern> patterns = new ArrayList<SequencePattern>(1);
//      SequencePattern pattern = new SequencePattern();
//      pattern.addPosition(new PatternPosition("PPPPP", false, false));
//      patterns.add(pattern);
//      action.setSequencePatterns(patterns);
//      SequenceMatchHandler handler = new SequenceMatchHandler();
//      handler.execute(action, null);
//    } catch (DataAccessException e) {
//      e.printStackTrace();
//      Assert.assertFalse(true);
//    } catch (DispatchException e) {
//      e.printStackTrace();
//      Assert.assertFalse(true);
//    }
//  }
//}
