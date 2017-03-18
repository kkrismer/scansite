package edu.mit.scansite.shared.dispatch.motif;

import java.util.ArrayList;

import edu.mit.scansite.shared.dispatch.BooleanResult;
import edu.mit.scansite.shared.transferobjects.Histogram;
import edu.mit.scansite.shared.transferobjects.Motif;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifAddAction implements Action<BooleanResult> {

  private Motif motif;
  private ArrayList<Histogram> histograms = new ArrayList<Histogram>();
  private boolean isUpdate = false;
  
  public MotifAddAction() {
  }
  
  public void setMotif(Motif motif) {
    this.motif = motif;
  }
  
  public Motif getMotif() {
    return motif;
  }
  
  public void addHistogram(Histogram histogram) {
    histograms.add(histogram);
  }
  
  public void setHistograms(ArrayList<Histogram> histograms) {
    this.histograms = histograms;
  }
  
  public ArrayList<Histogram> getHistograms() {
    return histograms;
  }
  
  public boolean isUpdate() {
    return isUpdate;
  }

  public void setUpdate(boolean isUpdate) {
    this.isUpdate = isUpdate;
  }
}
