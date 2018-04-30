package edu.mit.scansite.client.ui.widgets.features;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.transferobjects.MotifSelection;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class ChooseMotifWidget extends ScansiteWidget {
	
	public abstract MotifSelection getMotifSelection();
}
