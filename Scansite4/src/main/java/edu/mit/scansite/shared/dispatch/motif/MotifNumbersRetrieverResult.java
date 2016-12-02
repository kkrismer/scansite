package edu.mit.scansite.shared.dispatch.motif;

import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifNumbersRetrieverResult implements Result {

  private Integer nMammalMotifs = -1;
  private Integer nYeastMotifs = -1;
  private Integer nOtherMotifs = -1;
  
  public MotifNumbersRetrieverResult() {
  }

  public MotifNumbersRetrieverResult(Integer nMammalMotifs, Integer nYeastMotifs, Integer nOtherMotifs) {
    this.nMammalMotifs = nMammalMotifs;
    this.nYeastMotifs = nYeastMotifs;
    this.nOtherMotifs = nOtherMotifs;
  }

  public Integer getnMammalMotifs() {
    return nMammalMotifs;
  }

  public void setnMammalMotifs(Integer nMammalMotifs) {
    this.nMammalMotifs = nMammalMotifs;
  }

  public Integer getnYeastMotifs() {
    return nYeastMotifs;
  }

  public void setnYeastMotifs(Integer nYeastMotifs) {
    this.nYeastMotifs = nYeastMotifs;
  }

  public Integer getnOtherMotifs() {
    return nOtherMotifs;
  }

  public void setnOtherMotifs(Integer nOtherMotifs) {
    this.nOtherMotifs = nOtherMotifs;
  }

  
  
}
