package edu.mit.scansite.shared.dispatch.motif;

import java.util.ArrayList;

import edu.mit.scansite.shared.transferobjects.Motif;

import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifRetrieverResult implements Result {

  private ArrayList<Motif> motifs;
  
  public MotifRetrieverResult() {
  }
  
  public MotifRetrieverResult(ArrayList<Motif> motifs) {
    this.motifs = motifs;
  }
  
  public ArrayList<Motif> getMotifs() {
    return motifs;
  }
  
  public void setMotifs(ArrayList<Motif> motifs) {
    this.motifs = motifs;
  }
  
}
