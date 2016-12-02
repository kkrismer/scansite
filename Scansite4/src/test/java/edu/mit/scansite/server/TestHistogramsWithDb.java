//package edu.mit.scansite.test;
//
//import edu.mit.scansite.server.dataaccess.file.MotifFileReader;
//import edu.mit.scansite.server.dispatch.handler.motif.HistogramCreateHandler;
//import edu.mit.scansite.server.dispatch.handler.motif.HistogramGetHandler;
//import edu.mit.scansite.server.dispatch.handler.motif.MotifAddHandler;
//import edu.mit.scansite.server.dispatch.handler.motif.MotifDeleteHandler;
//import edu.mit.scansite.shared.ScansiteConstants;
//import edu.mit.scansite.shared.dispatch.motif.HistogramCreateAction;
//import edu.mit.scansite.shared.dispatch.motif.HistogramGetAction;
//import edu.mit.scansite.shared.dispatch.motif.HistogramRetrieverResult;
//import edu.mit.scansite.shared.dispatch.motif.MotifAddAction;
//import edu.mit.scansite.shared.dispatch.motif.MotifDeleteAction;
//import edu.mit.scansite.shared.transferobjects.Histogram;
//import edu.mit.scansite.shared.transferobjects.Motif;
//import edu.mit.scansite.shared.transferobjects.MotifClass;
//import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
//import edu.mit.scansite.shared.transferobjects.ScanResultSite;
//
//public class TestHistogramsWithDb {
//
//  /**
//   * @param args
//   */
//  public static void main(String[] args) {
//    try {
//      String motifShort = "Amphi_SH3";
//      
//      LightWeightMotifGroup group = new LightWeightMotifGroup(1, "Src homology 3 group", "SH3");
//      MotifFileReader mfr = new MotifFileReader();
//      Motif motif  = mfr.getMotif("test/motifs/"+motifShort+".txt");
//      motif.setShortName(motifShort); motif.setName("Amphiphysin SH3"); 
//      motif.setSubmitter("tobieh@mit.edu"); 
//      motif.setGeneInfo(new String[]{"AMPH"});
//      motif.setGroup(group);
//
//      startStop("starting");
//      HistogramCreateHandler createHandler = new HistogramCreateHandler();
//      HistogramCreateAction createAction = new HistogramCreateAction();
//      createAction.setDataSourceShortName(ScansiteConstants.HIST_DEFAULT_DATASOURCE_SHORTS[ScansiteConstants.HIST_DEFAULT_INDEX]); // datasource shortname
//      createAction.setTaxonName(ScansiteConstants.HIST_DEFAULT_TAXON_NAMES[ScansiteConstants.HIST_DEFAULT_INDEX]); // taxon name
//      createAction.setMotif(motif);
//      createAction.setHistogramNr(0);
//      
//      HistogramRetrieverResult result = createHandler.execute(createAction, null);
//      Histogram hist = result.getHistogram();
//
//      System.out.println(hist.getImageFilePath());
//      startStop("created histogram");
//      
//      MotifAddHandler saveHandler = new MotifAddHandler();
//      MotifAddAction saveAction = new MotifAddAction();
//      saveAction.addHistogram(hist);
//      saveAction.setMotif(motif);
//      
//      saveHandler.execute(saveAction, null);
//      startStop("saved histogram to database");
//      
//      HistogramGetHandler histGetHandler = new HistogramGetHandler();
//      HistogramGetAction getAction = new HistogramGetAction(motif.getId(), ScansiteConstants.HIST_DEFAULT_TAXON_NAMES[ScansiteConstants.HIST_DEFAULT_INDEX],
//          ScansiteConstants.HIST_DEFAULT_DATASOURCE_SHORTS[ScansiteConstants.HIST_DEFAULT_INDEX], 
//          new ScanResultSite(motif, "VAV_HUMAN", "VAV_HUMAN (swissprot)", "AAAAAAAASSAAATAPPAAAAAAYYAAA", 9, 0.38));
//      
//      result = histGetHandler.execute(getAction, null);
//      Histogram histRef = result.getHistogram();
//      if (histRef == null) {
//        System.out.println("No hist like this is available in the database");
//      } else {
//        System.out.println("Db-Histogram @ " + histRef.getImageFilePath());
//      }
//      startStop("created reference histgoram");
//      
//      MotifDeleteAction delAction = new MotifDeleteAction();
//      delAction.setMotifId(motif.getId(), MotifClass.MAMMALIAN);
//      MotifDeleteHandler delHandler = new MotifDeleteHandler();
//      delHandler.execute(delAction, null);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    System.exit(0);
//  }
//
//  private static long lastStart = 0;
//  private static void startStop(String message) {
//    if (lastStart == 0L) {
//      lastStart = System.currentTimeMillis();
//    }
//    
//    long now = System.currentTimeMillis();
//    System.out.println(message + "\nTime elapsed since last message: " + (now - lastStart) / 1000.0 + "s");
//    lastStart = now;
//  }
//  
//}
