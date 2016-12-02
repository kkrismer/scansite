package edu.mit.scansite.client.ui.widgets.features;

import java.util.ArrayList;
import java.util.Collections;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.AminoAcid;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * A widget that displays a motif and allows to make changes to it.
 * @author tobieh
 */
public class EditMotifDataWidget extends Composite {
  
  private boolean sortAminoAcids = false; // set to TRUE to display AAs alphabetically sorted
  private String VALUE_FIELD_WIDTH = "25px";
  
  private Motif motif = new Motif();
  private Grid grid; 
  private AminoAcid aas[] = AminoAcid.values(); 
  private TextBox motifValues[][];
  private VerticalPanel mainPanel = new VerticalPanel();
  private boolean showMotifName = false;
  private HTML motifNameLabel = new HTML();

  public EditMotifDataWidget() {
    this(false);
  }
  
  public EditMotifDataWidget(boolean showMotifName) {
    this.showMotifName = showMotifName;
    initWidget(mainPanel);
    init();
  }
  
  private void init() {
    if ( sortAminoAcids ) {
      ArrayList<AminoAcid> aacids = new ArrayList<AminoAcid>();
      for (AminoAcid aa : aas) {
        aacids.add(aa);
      }
      Collections.sort(aacids);
      aas = (AminoAcid []) aacids.toArray();
    }
    
    grid = new Grid(ScansiteConstants.WINDOW_SIZE + 1, aas.length + 1); //  x + 1 for title column/row
    motifValues = new TextBox[aas.length][ScansiteConstants.WINDOW_SIZE];
    
    boolean first = true;
    for (int i = 0; i < aas.length; ++i) {
      HTML htmlLabel= new HTML("<center><b>" + String.valueOf(aas[i].getOneLetterCode()) + "</b></center>");
      htmlLabel.setTitle(aas[i].getFullName() + " (" + aas[i].getThreeLetterCode() + ")");
      grid.setWidget(0, i + 1, htmlLabel);
      for (int j = 0; j < ScansiteConstants.WINDOW_SIZE; ++j) {
        if (first) {
          int position = j - ScansiteConstants.HALF_WINDOW;
          htmlLabel = new HTML("<center><b>" + ((position > 0) ? "+" + String.valueOf(position) : String.valueOf(position)) + "</b></center>");
          grid.setWidget(j + 1, 0, htmlLabel);
        }
        motifValues[i][j] = new TextBox();
        motifValues[i][j].setWidth(VALUE_FIELD_WIDTH);
        grid.setWidget(j+1, i+1, motifValues[i][j]);
      }
      first = false;
    }
    grid.setSize("100%", "100%");
    if (showMotifName) {
      motifNameLabel.setWidth("100%");
      motifNameLabel.addStyleName("subsubtitleLabel");   
      motifNameLabel.addStyleName("alignCenter");   
      mainPanel.add(motifNameLabel);
    }
    mainPanel.add(grid);
    mainPanel.setSize("100%", "100%");
  }
  
  public void setMotif(Motif m) {
    if (m != null) {
      this.motif = m;
      if (showMotifName && motif.getDisplayName() != null) {
        motifNameLabel.setHTML("<b>Motif: </b>" + motif.getDisplayName() + ((motif.getShortName() != null) ? " (" + motif.getShortName() +  ")" : ""));
      }
      setMotifFields();
    }
  }

  public void setEditable(boolean isEditable) {
    for (int i = 0; i < aas.length; ++i) {
      for (int j = 0; j < ScansiteConstants.WINDOW_SIZE; ++j) {
        motifValues[i][j].setEnabled(isEditable);
      }
    }
  }

  public Motif getMotif() {
    readMotifFromFields();
    return motif;
  }
  
  /**
   * Writes the motif's values to the textboxes.
   * @param m The motif that is going to be displayed.
   */
  private void setMotifFields() {
    for (int i = 0; i < aas.length; ++i) {
      for (int j = 0; j < ScansiteConstants.WINDOW_SIZE; ++j) {
        motifValues[i][j].setText(String.valueOf(motif.getValue(aas[i], j)));
      }
    }
  }

  /**
   * Sets the motif's values to those that are being displayed.
   */
  private void readMotifFromFields() {
    for (int j = 0; j < ScansiteConstants.WINDOW_SIZE; ++j) {
      // set values from fields and set minimum to 
      for (int i = 0; i < aas.length; ++i) {
        String value = motifValues[i][j].getText();
        if (value != null && !value.isEmpty()) {
          double nr = Double.valueOf(value);
          motif.setValue(aas[i], j, nr);
        }
      }
    }
    motif.resetNumbers();
  }
}
