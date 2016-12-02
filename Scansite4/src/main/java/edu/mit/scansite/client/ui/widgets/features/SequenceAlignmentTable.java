package edu.mit.scansite.client.ui.widgets.features;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.mit.scansite.shared.dispatch.features.OrthologScanResult;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SequenceAlignmentTable extends Composite {
  private VerticalPanel mainPanel = new VerticalPanel();
  private OrthologScanResult result;

  public SequenceAlignmentTable(OrthologScanResult result) {
    initWidget(mainPanel);
    this.result = result;
    init();
  }

  private void init() {
    mainPanel.setWidth("100%");
    HTML alignment;
    if (result.getSequenceAlignment() != null) {
      alignment = new HTML(result.getSequenceAlignment()
          .getHTMLFormattedAlignment());
    } else {
      alignment = new HTML("Unable to align sequences: Sequence pattern and phosphorylation site mismatch");
    }
    alignment.setStyleName("sequence");
    mainPanel.add(alignment);
  }
}
